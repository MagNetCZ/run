package cz.cvut.fit.run.interpreter.core.exceptions;

/**
 * Created by MagNet on 11. 4. 2015.
 */
public class VMSegmentationFault extends VMException {
    public VMSegmentationFault() {
    }

    public VMSegmentationFault(String message) {
        super(message);
    }

    public VMSegmentationFault(String message, Throwable cause) {
        super(message, cause);
    }

    public VMSegmentationFault(Throwable cause) {
        super(cause);
    }
}
