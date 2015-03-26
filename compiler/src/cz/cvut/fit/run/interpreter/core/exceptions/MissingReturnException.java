package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class MissingReturnException extends VMException {
    public MissingReturnException() {
    }

    public MissingReturnException(String message) {
        super(message);
    }

    public MissingReturnException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingReturnException(Throwable cause) {
        super(cause);
    }
}
