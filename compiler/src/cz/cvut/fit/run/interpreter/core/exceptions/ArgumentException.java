package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class ArgumentException extends VMException {
    public ArgumentException() {
    }

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }
}
