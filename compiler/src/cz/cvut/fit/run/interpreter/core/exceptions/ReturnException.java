package cz.cvut.fit.run.interpreter.core.exceptions;

import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class ReturnException extends VMException {
    private final VMObject value;

    public ReturnException(VMObject value) {
        this.value = value;
    }

    public VMObject getValue() {
        return value;
    }
}
