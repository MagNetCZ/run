package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.*;
import cz.cvut.fit.run.interpreter.core.types.type.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMMemory;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMString extends VMBuiltinType<String, VMStringInstance> {
    public VMString() throws VMException {
        super(VMType.STRING);
    }

    @Override
    public VMPointer createInstance(String value) throws VMException {
        return VMMemory.allocate(new VMStringInstance(this, value));
    }

    public VMPointer split(VMPointer instance, VMPointer operand) throws VMException {
        VMStringInstance stringValue = (VMStringInstance)instance.getObject();
        VMStringInstance splitArgument = (VMStringInstance)operand.getObject();

        String[] splitResult = stringValue.getValue().split(splitArgument.getValue());
        VMMachine vm = VMMachine.getInstance();
        VMArrayInstance splitArray = (VMArrayInstance)vm.getArrayClazz(VMType.STRING)
                .createInstance(splitResult.length).getObject();

        for (int i = 0; i < splitResult.length; i++) {
            splitArray.get(i).setValue(createInstance(splitResult[i]));
        }

        return splitArray.getPointer();
    }

    public VMPointer concat(VMPointer instance, VMPointer operand) throws VMException {
        VMStringInstance stringValue = (VMStringInstance)instance.getObject();
        VMStringInstance castedOperand = (VMStringInstance)operand.getObject();

        return createInstance(stringValue.getValue() + castedOperand.getValue());
    }

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        builtinMethods.add(
                new BuiltinMethodIdentifier("split", "split",
                        new VMArrayType(VMType.STRING), VMType.STRING));

        builtinMethods.add(new BuiltinMethodIdentifier("concat", "+", VMType.STRING, VMType.STRING));

        return builtinMethods;
    }
}
