package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.exceptions.TypeMismatchException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMNullInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class TypeValuePair {
    private VMType type;
    private VMObject value;

    public TypeValuePair(VMType type) throws VMException {
        this.type = type;
        this.value = VMNullInstance.getInstance();
    }

    public void setValue(VMObject value) throws VMException {
        if (!value.canBeAssignedTo(getType())) {
            throw new TypeMismatchException(value.getType().getName() + " to " + getType().getName());
        }

        this.value = value;
    }

    public VMObject getValue() {
        return value;
    }

    public VMType getType() {
        return type;
    }
}
