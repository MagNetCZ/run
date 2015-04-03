package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.types.type.VMType;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class VMExceptionClass extends VMClass {
    public VMExceptionClass() throws cz.cvut.fit.run.interpreter.core.exceptions.VMException {
        super(VMType.EXCEPTION, null);
    }
}
