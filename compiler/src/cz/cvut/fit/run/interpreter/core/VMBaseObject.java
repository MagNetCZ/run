package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.helpers.VariableHash;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.memory.VMPointer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBaseObject extends VMReference {
    protected VariableHash fields;

    protected VMBaseObject() throws VMException {
        fields = new VariableHash();
    }

    public abstract VMType getType();

    public TypeValuePair declareField(VMIdentifierInstance identifier, VMType type) throws VMException {
        VMMachine.logger.info("Declaring field " + identifier);

        return fields.declareVariable(identifier, type);
    }

    public TypeValuePair getField(VMIdentifierInstance identifier) throws VMException {
        // TODO deal with inheritance
        return fields.getPair(identifier);
    }

    public abstract void callMethod(String name, VMPointer... args) throws VMException;

    // For GC

    public VariableHash getFields() {
        return fields;
    }

    /**
     * Reallocate the object
     * @return a new instance of the object with the same values on a new memory location
     * @throws VMException
     */
    public VMPointer copy() throws VMException {
        throw new VMException("This method should be redefined for BaseObject subclasses.");
    }
}
