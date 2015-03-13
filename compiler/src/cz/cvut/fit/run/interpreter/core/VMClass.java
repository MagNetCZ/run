package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.types.VMType;

import java.util.List;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMClass extends VMBaseObject {
    @Override
    public VMType getType() {
        return VMType.CLASS;
    }
}
