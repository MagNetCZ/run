package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class IllegalOperationException extends VMException {
    public IllegalOperationException() {
    }

    public IllegalOperationException(String message) {
        super(message);
    }

    public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOperationException(Throwable cause) {
        super(cause);
    }
}
