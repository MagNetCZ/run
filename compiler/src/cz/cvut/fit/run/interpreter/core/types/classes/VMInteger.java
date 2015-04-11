package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBooleanInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIntegerInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.instances.VMStringInstance;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMMemory;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMInteger extends VMBuiltinType<Integer> {
    public VMInteger() throws VMException {
        super(VMType.INT);
    }

    public static VMIntegerInstance convertToInt(VMPointer object) throws VMException {
        // TODO support for other numeric types
        return (VMIntegerInstance)object.getObject();
    }

    // Binary operators

    public VMPointer sum(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return createInstance(firstOperand.getValue() + secondOperand.getValue());
    }

    public VMPointer subtract(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return createInstance(firstOperand.getValue() - secondOperand.getValue());
    }

    public VMPointer multiply(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return createInstance(firstOperand.getValue() * secondOperand.getValue());
    }

    public VMPointer divide(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return createInstance(firstOperand.getValue() / secondOperand.getValue());
    }

    // Unary operators

    public VMPointer plusOne(VMPointer instance) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        firstOperand.setValue(firstOperand.getValue() + 1);

        return firstOperand.getPointer();
    }

    // Static methods

    public VMPointer parseInt(VMPointer string) throws VMException {
        VMStringInstance firstOperand = (VMStringInstance)string.getObject();
        return createInstance(Integer.parseInt(firstOperand.getValue()));
    }

    // Comparision operators

    public VMPointer compareEquals(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() == secondOperand.getValue());
    }

    public VMPointer compareNotEquals(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() != secondOperand.getValue());
    }

    public VMPointer compareGreaterThan(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() > secondOperand.getValue());
    }

    public VMPointer compareLessThan(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() < secondOperand.getValue());
    }

    public VMPointer compareGreaterEquals(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() >= secondOperand.getValue());
    }

    public VMPointer compareLessEquals(VMPointer instance, VMPointer operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance.getObject();
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() <= secondOperand.getValue());
    }

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        // Math
        builtinMethods.add(new BuiltinMethodIdentifier("sum", "+", VMType.INT, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("subtract", "-", VMType.INT, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("multiply", "*", VMType.INT, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("divide", "/", VMType.INT, VMType.INT));

        builtinMethods.add(new BuiltinMethodIdentifier("plusOne", "++", VMType.INT));

        // Static (parsing, etc.)

        builtinMethods.add(new BuiltinMethodIdentifier(true, "parseInt", "parseInt", VMType.INT, VMType.STRING));

        // Comparison
        builtinMethods.add(new BuiltinMethodIdentifier("compareEquals", "==", VMType.BOOLEAN, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("compareNotEquals", "!=", VMType.BOOLEAN, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("compareGreaterThan", ">", VMType.BOOLEAN, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("compareLessThan", "<", VMType.BOOLEAN, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("compareGreaterEquals", ">=", VMType.BOOLEAN, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("compareLessEquals", "<=", VMType.BOOLEAN, VMType.INT));

        return builtinMethods;
    }

    @Override
    public VMPointer createInstance(Integer value) throws VMException {
        return VMMemory.allocate(new VMIntegerInstance(this, value));
    }

    @Override
    public VMType getType() {
        return VMType.INT;
    }
}
