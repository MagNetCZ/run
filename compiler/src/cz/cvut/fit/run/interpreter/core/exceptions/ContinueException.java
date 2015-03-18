package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 18. 3. 2015.
 */
public class ContinueException extends FlowException {
    public ContinueException() {
    }

    public ContinueException(String message) {
        super(message);
    }

    public ContinueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContinueException(Throwable cause) {
        super(cause);
    }
}
