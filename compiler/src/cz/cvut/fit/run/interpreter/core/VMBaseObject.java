package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBaseObject extends VMReference {
    public abstract VMType getType();
}
