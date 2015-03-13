package cz.cvut.fit.run.interpreter.core.types;

import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 13. 3. 2015.
 */
public class VMBoolean extends VMBuiltinType<Boolean> {
    public final static VMBoolean FALSE = new VMBoolean(false);
    public final static VMBoolean TRUE = new VMBoolean(true);

    protected VMBoolean(Boolean contents) {
        super(contents);
    }

    public VMBoolean getFor(boolean value) {
        return value ? TRUE : FALSE;
    }

    public VMBoolean negate() {
        return getFor(!contents);
    }

    public VMBoolean boolEquals(VMObject other) {
        return getFor(contents == ((VMBoolean)other).contents);
    }

    public VMBoolean boolNotEquals(VMObject other) {
        return getFor(contents != ((VMBoolean)other).contents);
    }

    public VMBoolean or(VMObject other) {
        return getFor(contents || ((VMBoolean)other).contents);
    }

    public VMBoolean and(VMObject other) {
        return getFor(contents && ((VMBoolean)other).contents);
    }

    public VMBoolean xor(VMObject other) {
        return getFor(contents ^ ((VMBoolean)other).contents);
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
}
