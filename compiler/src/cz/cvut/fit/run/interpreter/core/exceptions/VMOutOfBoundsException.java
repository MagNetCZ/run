package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 23. 3. 2015.
 */
public class VMOutOfBoundsException extends VMException {
    public VMOutOfBoundsException() {
    }

    public VMOutOfBoundsException(String message) {
        super(message);
    }

    public VMOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public VMOutOfBoundsException(Throwable cause) {
        super(cause);
    }
}
