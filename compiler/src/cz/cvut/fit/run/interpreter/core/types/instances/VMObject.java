package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

import java.util.HashMap;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMObject extends VMBaseObject {
    VMClass clazz;
    boolean nil;

    HashMap<String, VMMethod> instance_fields;

    @Override
    public VMType getType() {
        return VMType.REFERENCE;
    }

    public VMObject(VMClass clazz) {
        this.clazz = clazz;

        instance_fields = new HashMap<>();
    }

    public void callMethod(String name, VMObject ... args) throws VMException {
        getClazz().callMethod(name, this, args);
    }

    public VMClass getClazz() {
        return clazz;
    }

    public boolean isNil() {
        return nil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMObject)) return false;

        VMObject vmObject = (VMObject) o;

        if (nil != vmObject.nil) return false;
        if (!clazz.equals(vmObject.clazz)) return false;
        if (instance_fields != null ? !instance_fields.equals(vmObject.instance_fields) : vmObject.instance_fields != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = 31 * result + (nil ? 1 : 0);
        result = 31 * result + (instance_fields != null ? instance_fields.hashCode() : 0);
        return result;
    }
}
