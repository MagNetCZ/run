package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMIntegerInstance extends VMBuiltinInstance<Integer> {
    public VMIntegerInstance(VMClass clazz, Integer value) throws VMException {
        super(clazz, value);
    }
}
