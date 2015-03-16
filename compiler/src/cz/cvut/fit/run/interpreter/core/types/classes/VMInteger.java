package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIntegerInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMInteger extends VMBuiltinType<Integer, VMIntegerInstance> {
    public static VMIntegerInstance convertToInt(VMObject object) {
        // TODO support for other numeric types
        return (VMIntegerInstance)object;
    }

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

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        builtinMethods.add(new BuiltinMethodIdentifier("sum", "+", VMType.INT, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("subtract", "-", VMType.INT, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("multiply", "*", VMType.INT, VMType.INT));
        builtinMethods.add(new BuiltinMethodIdentifier("divide", "/", VMType.INT, VMType.INT));

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
