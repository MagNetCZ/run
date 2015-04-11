package cz.cvut.fit.run.interpreter.memory;

import cz.cvut.fit.run.interpreter.context.VMFrame;
import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.exceptions.WrongAllocationException;
import cz.cvut.fit.run.interpreter.core.helpers.VariableHash;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.instances.VMArrayInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

import java.util.Collections;
import java.util.Stack;

/**
 * Created by MagNet on 11. 4. 2015.
 */
public class VMGarbageCollector {
    private VMMemory memory;
    private boolean disabled;
    private int runs;

    public VMGarbageCollector(VMMemory memory) {
        this.memory = memory;
        disabled = false;
    }

    public void garbageCollect() throws VMException {
        if (disabled) {
            VMMachine.logger.severe("GC Disabled");
            return;
        }

        VMMachine.logger.severe("GC Starting");

        try {
            memory.disableGC();
            memory.switchMemory();
            VMMachine vm = VMMachine.getInstance();
            // Can't use forEach because exception handling for functional interfaces is just silly

            for (VMClass clazz : vm.getClasses()) { collectClass(clazz); }
            for (VMFrame frame : vm.getFrames()) { collectFrame(frame); }
//        vm.getClasses().forEach(this::collectClass);
//        vm.getFrames().forEach(this::collectFrame);

            memory.wipeUnused();
        } catch (final VMException e) {
            e.printStackTrace();
            System.out.println("Caught exception");
            throw e;
        } finally {
            memory.enableGC();
        }

        VMMachine.logger.severe("GC Ended - run " + runs);
        runs++;
    }

    private void collectFrame(VMFrame frame) throws VMException {
        collectFrameStack(frame);
        for (VariableHash vh : frame.getLocalVariableStack()) { collectVariableHash(vh); }
//        frame.getLocalVariableStack().forEach(this::collectVariableHash);
    }

    private void collectFrameStack(VMFrame frame) throws VMException {
        Stack<VMPointer> newStack = new Stack<>();
        Stack<VMPointer> frameStack = frame.getOpStack();

        while (!frameStack.empty()) {
            newStack.push(collectPointer(frameStack.pop()));
        }

        Collections.reverse(newStack);

        frame.setOpStack(newStack);
    }

    private void collectVariableHash(VariableHash hash) throws VMException {
        for (TypeValuePair tvp : hash.values()) { collectValuePair(tvp); }
//        hash.values().forEach(this::collectValuePair);
    }

    private void collectClass(VMClass clazz) throws VMException {
        collectVariableHash(clazz.getFields());
    }

    private VMPointer collectPointer(VMPointer pointer) throws VMException {
        if (pointer == null) return null;
        VMObject oldObject = pointer.getRawObject();
        if (oldObject == null) return pointer;
        if (oldObject.isRelocated()) return oldObject.getRelocatedPointer(); // Object has already been copied

        VMPointer pointerToNew = oldObject.copy();
        oldObject.setRelocatedPointer(pointerToNew);

        collectVariableHash(pointerToNew.getObject().getFields());
        VMObject newObject = pointerToNew.getRawObject();

        // Special cases
        if (newObject instanceof VMArrayInstance) collectArray(newObject);
        if (newObject instanceof VMIdentifierInstance) collectID(newObject);

        return pointerToNew;
    }

    private void collectArray(VMObject newObject) throws VMException {
        VMArrayInstance array = (VMArrayInstance)newObject;
        for (TypeValuePair tvp : array.getValue()) {
            collectValuePair(tvp);
        }
    }

    private void collectID(VMObject newObject) throws VMException {
        VMIdentifierInstance id = (VMIdentifierInstance)newObject;
        VMPointer newFieldPointer = collectPointer(id.getFieldPointer());
        id.setFieldPointer(newFieldPointer);
    }

    private void collectValuePair(TypeValuePair pair) throws VMException {
        VMPointer newPointer = collectPointer(pair.getPointer());
        if (newPointer.pointsToObject() && !memory.inActive(newPointer))
            throw new WrongAllocationException("Object has been allocated outside of working memory");

        pair.setValue(newPointer);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
