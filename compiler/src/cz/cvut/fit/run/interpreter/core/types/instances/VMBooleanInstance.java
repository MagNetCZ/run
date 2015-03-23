package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMBooleanInstance extends VMBuiltinInstance<Boolean> {
    public VMBooleanInstance(VMClass clazz, Boolean value) {
        super(clazz, value);
    }
}
