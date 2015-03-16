package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMStringInstance extends VMBuiltinInstance<String> {
    @Override
    public VMType getType() {
        return VMType.STRING;
    }

    public VMStringInstance(VMClass clazz, String value) {
        super(clazz, value);
    }
}
