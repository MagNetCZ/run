package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class MemoryNotInitializedException extends VMException {
    public MemoryNotInitializedException() {
    }

    public MemoryNotInitializedException(String message) {
        super(message);
    }

    public MemoryNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemoryNotInitializedException(Throwable cause) {
        super(cause);
    }
}
