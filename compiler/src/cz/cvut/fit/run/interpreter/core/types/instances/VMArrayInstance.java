package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMArrayInstance extends VMBuiltinInstance<VMObject[]> {
    public VMArrayInstance(VMClass clazz, Integer size) {
        super(clazz, new VMObject[size]);
    }
}
