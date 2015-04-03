package cz.cvut.fit.run.interpreter.core.exceptions;

import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class ThrowException extends VMException {
    VMObject exception;

    public ThrowException(VMObject exception) throws VMException {
        if (!exception.isDescendantOf(VMType.EXCEPTION))
            throw new ArgumentException("Trying to throw a non exception");
        this.exception = exception;
    }

    public VMObject getException() {
        return exception;
    }
}
