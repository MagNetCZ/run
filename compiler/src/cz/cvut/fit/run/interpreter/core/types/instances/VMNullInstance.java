package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMNull;

/**
 * Created by MagNet on 20. 3. 2015.
 */
public final class VMNullInstance extends VMObject{

    public static VMNullInstance instance;

    public VMNullInstance() throws VMException {
        super(VMNull.getInstance());
    }

    public static VMNullInstance getInstance() throws VMException {
        if (instance == null)
            instance = new VMNullInstance();

        return instance;
    }
}
