package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMOutOfBoundsException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMIdentifier;
import cz.cvut.fit.run.interpreter.core.types.classes.VMInteger;
import cz.cvut.fit.run.interpreter.core.types.type.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

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
}
