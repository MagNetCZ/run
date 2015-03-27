package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.*;
import cz.cvut.fit.run.interpreter.core.types.type.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

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
    public VMStringInstance createInstance(String value) throws VMException {
        return new VMStringInstance(this, value);
    }

    public VMArrayInstance split(VMObject instance, VMObject operand) throws VMException {
        VMStringInstance stringValue = (VMStringInstance)instance;
        VMStringInstance splitArgument = (VMStringInstance)operand;

        String[] splitResult = stringValue.getValue().split(splitArgument.getValue());
        VMMachine vm = VMMachine.getInstance();
        VMArrayInstance splitArray = vm.getArrayClazz(VMType.STRING).createInstance(splitResult.length);

        for (int i = 0; i < splitResult.length; i++) {
            splitArray.get(i).setValue(createInstance(splitResult[i]));
        }

        return splitArray;
    }

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        builtinMethods.add(
                new BuiltinMethodIdentifier("split", "split",
                        new VMArrayType(VMType.STRING), VMType.STRING));

        return builtinMethods;
    }
}
