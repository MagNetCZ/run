package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMStringInstance;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMString extends VMBuiltinType<String, VMStringInstance> {
    public VMString() throws VMException {
        super(VMType.STRING);
    }

    @Override
    public VMStringInstance createInstance(String value) throws VMException {
        return new VMStringInstance(this, value);
    }
}
