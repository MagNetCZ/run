package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 11. 4. 2015.
 */
public class WrongAllocationException extends VMException {
    public WrongAllocationException() {
    }

    public WrongAllocationException(String message) {
        super(message);
    }

    public WrongAllocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongAllocationException(Throwable cause) {
        super(cause);
    }
}
