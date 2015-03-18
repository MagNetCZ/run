package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 18. 3. 2015.
 */
public class RedeclarationException extends VMException {
    public RedeclarationException() {
    }

    public RedeclarationException(String message) {
        super(message);
    }

    public RedeclarationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedeclarationException(Throwable cause) {
        super(cause);
    }
}
