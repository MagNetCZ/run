package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class SyntaxException extends VMException {
    public SyntaxException() {
    }

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxException(Throwable cause) {
        super(cause);
    }
}
