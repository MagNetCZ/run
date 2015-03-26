package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;

import java.io.BufferedReader;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class VMFileInstance extends VMBuiltinInstance<BufferedReader> {
    public VMFileInstance(VMClass clazz, BufferedReader value) throws VMException {
        super(clazz, value);
    }
}
