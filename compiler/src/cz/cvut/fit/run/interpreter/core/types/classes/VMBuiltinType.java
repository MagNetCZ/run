package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.ArgumentException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBuiltinInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIntegerInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBuiltinType<T> extends VMClass {
    protected VMBuiltinType(VMType type) throws VMException {
        super(type);
    }



    public abstract VMPointer createInstance(T value) throws VMException;

    protected void checkNumberOfArguments() throws VMException {
        VMObject quantifier = VMMachine.popValue(); // Arg quantifier
        int argNum = ((VMIntegerInstance)quantifier).getValue();

        if (argNum != 1)
            throw new ArgumentException("Builtin Constructor - Given " + argNum + " arguments, but 1 is required.");
    }

    @Override
    public VMPointer createInstance() throws VMException {
        checkNumberOfArguments();
        VMPointer initValuePointer = VMMachine.pop();
        return createInstance(((VMBuiltinInstance<T>)initValuePointer.getObject()).getValue());
    }
}
