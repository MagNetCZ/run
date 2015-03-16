package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.types.instances.VMStringInstance;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMString extends VMBuiltinType<String, VMStringInstance> {
    @Override
    public VMStringInstance createInstance(String value) {
        return new VMStringInstance(this, value);
    }
}
