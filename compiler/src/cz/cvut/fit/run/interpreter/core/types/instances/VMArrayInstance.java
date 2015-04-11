package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMOutOfBoundsException;
import cz.cvut.fit.run.interpreter.core.types.classes.*;
import cz.cvut.fit.run.interpreter.core.types.type.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMArrayInstance extends VMBuiltinInstance<TypeValuePair[]> {
    public VMArrayInstance(VMClass clazz, Integer size) throws VMException {
        super(clazz, new TypeValuePair[size]);
        VMType contentType = ((VMArrayType)clazz.getType()).getContentType();

        for (int i = 0; i < value.length; i++) {
            value[i] = new TypeValuePair(contentType);
        }

        VMMachine vm = VMMachine.getInstance();
        VMInteger intClass = (VMInteger)vm.getClazz("Integer");

        declareField(vm.getID("length"),VMType.INT)
                .setValue(intClass.createInstance(value.length));
    }

    public TypeValuePair get(int index) throws VMException {
        if (index < 0 || index >= value.length)
            throw new VMOutOfBoundsException(Integer.toString(index));
        return value[index];
    }

    @Override
    public VMPointer copy() throws VMException {
        int length = value.length;
        VMPointer newArrayPointer = ((VMArray)clazz).createInstance(length);
        VMArrayInstance newArray = (VMArrayInstance)newArrayPointer.getObject();
        for (int i = 0; i < value.length; i++) {
            newArray.value[i] = value[i].copy();
        }

        return newArrayPointer;
    }
}
