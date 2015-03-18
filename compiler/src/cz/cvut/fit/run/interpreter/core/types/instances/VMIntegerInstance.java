package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMIntegerInstance extends VMBuiltinInstance<Integer> {
    public VMIntegerInstance(VMClass clazz, Integer value) {
        super(clazz, value);
    }
}
