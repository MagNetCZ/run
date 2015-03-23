package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMArrayInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.instances.VMStringInstance;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMArray extends VMBuiltinType<TypeValuePair[], VMArrayInstance> {
    public VMArray(VMType contentType) throws VMException {
        super(new VMArrayType(contentType));
    }

    public VMArrayInstance createInstance(Integer size) throws VMException {
        return new VMArrayInstance(this, size);
    }

    @Override
    public VMArrayInstance createInstance(TypeValuePair... args) {
        throw new NotImplementedException();
    }
}
