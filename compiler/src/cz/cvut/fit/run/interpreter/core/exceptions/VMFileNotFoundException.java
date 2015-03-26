package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class VMFileNotFoundException extends VMException {
    public VMFileNotFoundException() {
    }

    public VMFileNotFoundException(String message) {
        super(message);
    }

    public VMFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VMFileNotFoundException(Throwable cause) {
        super(cause);
    }
}
