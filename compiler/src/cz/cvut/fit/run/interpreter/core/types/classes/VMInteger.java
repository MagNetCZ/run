package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBooleanInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIntegerInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMInteger extends VMBuiltinType<Integer, VMIntegerInstance> {
    public VMInteger() throws VMException {
    }

    public static VMIntegerInstance convertToInt(VMObject object) {
        // TODO support for other numeric types
        return (VMIntegerInstance)object;
    }

    // Binary operators

    public VMIntegerInstance sum(VMObject instance, VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Summing");
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return new VMIntegerInstance(this, firstOperand.getValue() + secondOperand.getValue());
    }

    public VMIntegerInstance subtract(VMObject instance, VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Subtracting");
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return new VMIntegerInstance(this, firstOperand.getValue() - secondOperand.getValue());
    }

    public VMIntegerInstance multiply(VMObject instance, VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Multiplying");
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return new VMIntegerInstance(this, firstOperand.getValue() * secondOperand.getValue());
    }

    public VMIntegerInstance divide(VMObject instance, VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Dividing");
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return new VMIntegerInstance(this, firstOperand.getValue() / secondOperand.getValue());
    }

    // Unary operators

    public VMIntegerInstance plusOne(VMObject instance) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        firstOperand.setValue(firstOperand.getValue() + 1);

        return firstOperand;
    }

    // Comparision operators

    public VMBooleanInstance compareEquals(VMObject instance, VMObject operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() == secondOperand.getValue());
    }

    public VMBooleanInstance compareNotEquals(VMObject instance, VMObject operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() != secondOperand.getValue());
    }

    public VMBooleanInstance compareGreaterThan(VMObject instance, VMObject operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() > secondOperand.getValue());
    }

    public VMBooleanInstance compareLessThan(VMObject instance, VMObject operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() < secondOperand.getValue());
    }

    public VMBooleanInstance compareGreaterEquals(VMObject instance, VMObject operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
        VMIntegerInstance secondOperand = convertToInt(operand);

        return VMBoolean.getBool(firstOperand.getValue() >= secondOperand.getValue());
    }

    public VMBooleanInstance compareLessEquals(VMObject instance, VMObject operand) throws VMException {
        VMIntegerInstance firstOperand = (VMIntegerInstance)instance;
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
    public VMIntegerInstance createInstance(Integer value) {
        return new VMIntegerInstance(this, value);
    }

    @Override
    public VMType getType() {
        return VMType.INT;
    }
}
