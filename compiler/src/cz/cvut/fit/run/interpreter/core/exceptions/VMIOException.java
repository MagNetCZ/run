package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class VMIOException extends VMException {
    public VMIOException() {
    }

    public VMIOException(String message) {
        super(message);
    }

    public VMIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public VMIOException(Throwable cause) {
        super(cause);
    }
}
