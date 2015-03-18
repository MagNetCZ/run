package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 18. 3. 2015.
 */
public class FlowException extends VMException {
    public FlowException() {
    }

    public FlowException(String message) {
        super(message);
    }

    public FlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowException(Throwable cause) {
        super(cause);
    }
}
