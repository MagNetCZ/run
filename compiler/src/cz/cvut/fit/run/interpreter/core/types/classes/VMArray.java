package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMArrayInstance;
import cz.cvut.fit.run.interpreter.core.types.type.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMMemory;
import cz.cvut.fit.run.interpreter.memory.VMPointer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMArray extends VMBuiltinType<TypeValuePair[]> {
    public VMArray(VMType contentType) throws VMException {
        super(new VMArrayType(contentType));
    }

    public VMPointer createInstance(Integer size) throws VMException {
        return VMMemory.allocate(new VMArrayInstance(this, size));
    }

    @Override
    public VMPointer createInstance(TypeValuePair... args) {
        throw new NotImplementedException();
    }
}
