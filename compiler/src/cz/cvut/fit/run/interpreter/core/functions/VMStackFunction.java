package cz.cvut.fit.run.interpreter.core.functions;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public interface VMStackFunction {
    Object call() throws VMException;
}
