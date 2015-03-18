package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMBuiltinType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMIdentifier extends VMBuiltinType<String, VMIdentifierInstance> {
    public VMIdentifier() throws VMException {
    }

    @Override
    public VMIdentifierInstance createInstance(String value) {
        return new VMIdentifierInstance(this, value);
    }
}
