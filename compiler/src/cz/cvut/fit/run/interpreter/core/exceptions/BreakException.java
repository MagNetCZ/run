package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class BreakException extends VMException {
    public BreakException() {
    }

    public BreakException(String message) {
        super(message);
    }

    public BreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public BreakException(Throwable cause) {
        super(cause);
    }
}
