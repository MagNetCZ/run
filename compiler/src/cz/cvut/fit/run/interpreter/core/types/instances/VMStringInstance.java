package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMStringInstance extends VMBuiltinInstance<String> {
    public VMStringInstance(VMClass clazz, String value) {
        super(clazz, value);
    }
}
