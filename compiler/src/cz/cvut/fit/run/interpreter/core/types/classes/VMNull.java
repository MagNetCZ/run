package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMNullInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 20. 3. 2015.
 */
public final class VMNull extends VMClass {
    private static VMNull instance;

    public VMNull() throws VMException {
        super(VMType.NULL);
    }

    public static VMNull getInstance() throws VMException {
        if (instance == null)
            instance = new VMNull();

        return instance;
    }

    @Override
    public VMObject createInstance(VMObject... args) throws VMException {
        return VMNullInstance.getInstance();
    }
}
