package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMBuiltinType;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMBuiltinInstance<T> extends VMObject {
    protected T value;

    public VMBuiltinInstance(VMClass clazz, T value) throws VMException {
        super(clazz);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + getType().getName() + ": " + value.toString() + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMBuiltinInstance)) return false;

        VMBuiltinInstance that = (VMBuiltinInstance) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public VMPointer copy() throws VMException {
        return ((VMBuiltinType<T>)clazz).createInstance(value);
    }
}
