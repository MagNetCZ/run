package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMIdentifier extends VMBuiltinType<String, VMIdentifierInstance> {
    public VMIdentifier() throws VMException {
        super(VMType.ID);
    }

    @Override
    public VMIdentifierInstance createInstance(String value) throws VMException {
        return new VMIdentifierInstance(this, value);
    }
}
