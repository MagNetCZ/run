package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBooleanInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 13. 3. 2015.
 */
public class VMBoolean extends VMBuiltinType<Boolean, VMBooleanInstance> {
    public static VMBooleanInstance FALSE;
    public static VMBooleanInstance TRUE;

    public VMBoolean() throws VMException {
        super(VMType.BOOLEAN);

        FALSE = new VMBooleanInstance(this, false);
        TRUE = new VMBooleanInstance(this, true);
    }

    public static VMBooleanInstance getBool(boolean value) {
        return value ? TRUE : FALSE;
    }

    public VMBooleanInstance negate(VMObject instance) {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance;
        return getBool(!boolInstance.getValue());
    }

    public VMBooleanInstance boolEquals(VMObject instance, VMObject other) {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance;
        return getBool(boolInstance.getValue() == ((VMBooleanInstance) other).getValue());
    }

    public VMBooleanInstance boolNotEquals(VMObject instance, VMObject other) {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance;
        return getBool(boolInstance.getValue() != ((VMBooleanInstance) other).getValue());
    }

    public VMBooleanInstance or(VMObject instance, VMObject other) {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance;
        return getBool(boolInstance.getValue() || ((VMBooleanInstance) other).getValue());
    }

    public VMBooleanInstance and(VMObject instance, VMObject other) {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance;
        return getBool(boolInstance.getValue() && ((VMBooleanInstance) other).getValue());
    }

    public VMBooleanInstance xor(VMObject instance, VMObject other) {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance;
        return getBool(boolInstance.getValue() ^ ((VMBooleanInstance) other).getValue());
    }

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        builtinMethods.add(new BuiltinMethodIdentifier("negate", "!", VMType.BOOLEAN));
        builtinMethods.add(new BuiltinMethodIdentifier("boolEquals", "==", VMType.BOOLEAN, VMType.BOOLEAN));
        builtinMethods.add(new BuiltinMethodIdentifier("boolNotEquals", "!=", VMType.BOOLEAN, VMType.BOOLEAN));
        builtinMethods.add(new BuiltinMethodIdentifier("or", "||", VMType.BOOLEAN, VMType.BOOLEAN));
        builtinMethods.add(new BuiltinMethodIdentifier("and", "&&", VMType.BOOLEAN, VMType.BOOLEAN));
        builtinMethods.add(new BuiltinMethodIdentifier("xor", "^", VMType.BOOLEAN, VMType.BOOLEAN));

        return builtinMethods;
    }

    @Override
    public VMType getType() {
        return VMType.BOOLEAN;
    }

    @Override
    public VMBooleanInstance createInstance(Boolean value) {
        return new VMBooleanInstance(this, value);
    }
}
