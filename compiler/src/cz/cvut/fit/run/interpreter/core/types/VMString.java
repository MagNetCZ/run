package cz.cvut.fit.run.interpreter.core.types;

import cz.cvut.fit.run.interpreter.core.VMObject;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMString extends VMSimpleType<String> {
    public VMString(String contents) {
        super(contents);
    }
}
