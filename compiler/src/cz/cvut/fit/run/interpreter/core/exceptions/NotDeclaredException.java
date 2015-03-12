package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class NotDeclaredException extends VMException {
    public NotDeclaredException() {
    }

    public NotDeclaredException(String message) {
        super(message);
    }
}
