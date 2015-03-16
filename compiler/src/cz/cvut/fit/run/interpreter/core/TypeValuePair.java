package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class TypeValuePair {
    private VMType type;
    private VMObject value;

    public TypeValuePair(VMType type) {
        this.type = type;
    }

    public void setValue(VMObject value) {
        this.value = value;
    }

    public VMObject getValue() {
        return value;
    }

    public VMType getType() {
        return type;
    }
}
