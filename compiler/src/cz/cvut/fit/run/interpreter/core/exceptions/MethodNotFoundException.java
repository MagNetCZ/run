package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 13. 3. 2015.
 */
public class MethodNotFoundException extends VMException {
    public MethodNotFoundException() {
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }
}
