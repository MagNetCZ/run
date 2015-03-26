package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class ProgramEndException extends VMException {
    public ProgramEndException() {
    }

    public ProgramEndException(String message) {
        super(message);
    }

    public ProgramEndException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgramEndException(Throwable cause) {
        super(cause);
    }
}
