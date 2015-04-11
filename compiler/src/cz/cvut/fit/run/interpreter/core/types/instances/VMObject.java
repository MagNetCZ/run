package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.NotAllocatedException;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.classes.VMInteger;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMMemory;
import cz.cvut.fit.run.interpreter.memory.VMPointer;
import cz.cvut.fit.run.interpreter.traversion.FieldInitializeVisitorBuilder;
import cz.cvut.fit.run.interpreter.traversion.ModifierFilter;

import java.util.List;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMObject extends VMBaseObject {
    protected VMClass clazz;
    protected VMPointer pointer = null;
    protected VMPointer relocated = null;

    public VMPointer getPointer() throws VMException {
        if (pointer == null)
            throw new NotAllocatedException(this.toString());
        return pointer;
    }

    public void setPointer(VMPointer pointer) throws VMException {
        if (this.pointer != null)
            throw new RedeclarationException("Cannot reset an object's pointer. The object has to be copied.");
        this.pointer = pointer;
    }

    public boolean isRelocated() {
        return relocated != null;
    }

    public void setRelocatedPointer(VMPointer relocated) throws VMException {
        if (this.relocated != null)
            throw new RedeclarationException("Cannot reset an object's relocation pointer. Trying to copy again?");
        this.relocated = relocated;
    }

    public VMPointer getCurrentPointer() {
        if (relocated != null)
            return relocated;

        return pointer;
    }

    public VMPointer getRelocatedPointer() {
        return relocated;
    }

    @Override
    public VMType getType() {
        return getClazz().getType();
    }

    public VMObject(VMClass clazz) throws VMException {
        this.clazz = clazz;

        initialize();
    }

    private void injectSelf() throws VMException {
//        VMMemory.getInstance().disableGC();

        int argNum = ((VMIntegerInstance) VMMachine.popValue()).getValue();

        // Inject itself (this)
        VMMachine.push(getPointer());
        VMPointer argNumPlusOne = VMMachine.getInstance().getInteger(argNum + 1);
        VMMachine.push(argNumPlusOne);

//        VMMachine.push(getCurrentPointer());

//        VMMemory.getInstance().enableGC();
    }

    public void callMethod(String name) throws VMException {
        injectSelf();
        getClazz().callMethod(name);
    }

    public void callMethod(VMMethod method) throws VMException {
        injectSelf();
        getClazz().callMethod(method);
    }

    public VMClass getClazz() {
        return clazz;
    }

    public boolean isDescendantOf(VMType superClass) {
        return getClazz().isDescendantOf(superClass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMObject)) return false;

        VMObject vmObject = (VMObject) o;

        if (!clazz.equals(vmObject.clazz)) return false;

        return true;

        // TODO equals should use superclass (fields)
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    @Override
    public String toString() {
        return "<Instance: " + getType().getName() + ">";
    }

    public void initialize() throws VMException {
        if (getClazz() == null || getClazz().getSource() == null)
            return;

        FieldInitializeVisitorBuilder builder =
                new FieldInitializeVisitorBuilder(this, new ModifierFilter("static", true));

        List<VMClass> classList = getClazz().getClassHierarchy();
        // Initialize fields from the top of the class hierarchy (most super)
        for (VMClass clazz : classList) {
            if (clazz.getSource() == null)
                continue;
            VMException ex = builder.visit(clazz.getSource());

            if (ex != null)
                throw ex;
        }
    }

    public boolean canBeAssignedTo(VMType type) {
        return clazz.canBeAssignedTo(type);
    }

    @Override
    public VMPointer copy() throws VMException {
        VMObject newObject = new VMObject(clazz);
        newObject.fields = fields.copy();
        return VMMemory.allocate(newObject);
    }
}
