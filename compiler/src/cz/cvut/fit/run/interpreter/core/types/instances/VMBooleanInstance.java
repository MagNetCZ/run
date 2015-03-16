package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMBooleanInstance extends VMBuiltinInstance<Boolean> {
    @Override
    public VMType getType() {
        return VMType.BOOLEAN;
    }

    public VMBooleanInstance(VMClass clazz, Boolean value) {
        super(clazz, value);
    }
}
