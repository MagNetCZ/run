package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class OutOfMemoryException extends VMException {
    public OutOfMemoryException() {
    }

    public OutOfMemoryException(String message) {
        super(message);
    }

    public OutOfMemoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfMemoryException(Throwable cause) {
        super(cause);
    }
}
