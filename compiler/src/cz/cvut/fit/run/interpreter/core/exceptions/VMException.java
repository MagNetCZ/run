package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMException extends Exception {
    public VMException() {
    }

    public VMException(String message) {
        super(message);
    }

    public VMException(String message, Throwable cause) {
        super(message, cause);
    }

    public VMException(Throwable cause) {
        super(cause);
    }
}
