package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.modifiers.Scope;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBuiltinInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBuiltinType<T> extends VMClass {
    protected VMBuiltinType(VMType type) throws VMException {
        super(type);
    }



    public abstract VMPointer createInstance(T value) throws VMException;

    @Override
    public VMPointer createInstance(VMPointer... args) throws VMException {
        return createInstance(((VMBuiltinInstance<T>)args[0].getObject()).getValue());
    }
}
