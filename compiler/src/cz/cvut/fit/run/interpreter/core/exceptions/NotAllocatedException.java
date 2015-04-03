package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class NotAllocatedException extends VMException {
    public NotAllocatedException() {
    }

    public NotAllocatedException(String message) {
        super(message);
    }

    public NotAllocatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllocatedException(Throwable cause) {
        super(cause);
    }
}
