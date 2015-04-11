package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.exceptions.TypeMismatchException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMNullInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class TypeValuePair {
    private VMType type;
    private VMPointer value;

    public TypeValuePair(VMType type) throws VMException {
        this.type = type;
        this.value = VMPointer.NULL_POINTER;
    }

    public TypeValuePair(VMType type, VMPointer value) {
        this.type = type;
        this.value = value;
    }

    public void setValue(VMPointer value) throws VMException {
        if (!value.getObject().canBeAssignedTo(getType())) {
            throw new TypeMismatchException(value.getObject().getType().getName() + " to " + getType().getName());
        }

        this.value = value;
    }

    public VMPointer getPointer() throws VMException {
        return value;
    }

    public VMObject getValue() throws VMException {
        return value.getObject();
    }

    public VMType getType() {
        return type;
    }

    public TypeValuePair copy() {
        return new TypeValuePair(type, value);
    }
}
