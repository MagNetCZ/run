package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBooleanInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMMemory;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

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

    public static VMPointer getBool(boolean value) {
        return value ? VMPointer.TRUE_POINTER : VMPointer.FALSE_POINTER;
    }

    public VMPointer negate(VMPointer instance) throws VMException {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance.getObject();
        return getBool(!boolInstance.getValue());
    }

    public VMPointer boolEquals(VMPointer instance, VMPointer other) throws VMException {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance.getObject();
        return getBool(boolInstance.getValue() == ((VMBooleanInstance) other.getObject()).getValue());
    }

    public VMPointer boolNotEquals(VMPointer instance, VMPointer other) throws VMException {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance.getObject();
        return getBool(boolInstance.getValue() != ((VMBooleanInstance) other.getObject()).getValue());
    }

    public VMPointer or(VMPointer instance, VMPointer other) throws VMException {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance.getObject();
        return getBool(boolInstance.getValue() || ((VMBooleanInstance) other.getObject()).getValue());
    }

    public VMPointer and(VMPointer instance, VMPointer other) throws VMException {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance.getObject();
        return getBool(boolInstance.getValue() && ((VMBooleanInstance) other.getObject()).getValue());
    }

    public VMPointer xor(VMPointer instance, VMPointer other) throws VMException {
        VMBooleanInstance boolInstance = (VMBooleanInstance)instance.getObject();
        return getBool(boolInstance.getValue() ^ ((VMBooleanInstance) other.getObject()).getValue());
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
    public VMPointer createInstance(Boolean value) throws VMException {
        return VMMemory.allocate(new VMBooleanInstance(this, value));
    }
}
