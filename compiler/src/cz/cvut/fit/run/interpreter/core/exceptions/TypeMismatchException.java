package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 18. 3. 2015.
 */
public class TypeMismatchException extends VMException {
    public TypeMismatchException() {
    }

    public TypeMismatchException(String message) {
        super(message);
    }

    public TypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeMismatchException(Throwable cause) {
        super(cause);
    }
}
