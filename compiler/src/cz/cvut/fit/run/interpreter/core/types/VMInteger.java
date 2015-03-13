package cz.cvut.fit.run.interpreter.core.types;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.VMObject;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMInteger extends VMBuiltinType<Integer> {
    public VMInteger(Integer contents) {
        super(contents);
    }

    public static VMInteger convertToInt(VMObject object) {
        // TODO support for other numeric types
        return (VMInteger)object;
    }

    public VMInteger sum(VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Summing");
        VMInteger secondOperand = convertToInt(operand);

        return new VMInteger(contents + secondOperand.contents);
    }

    public VMInteger subtract(VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Subtracting");
        VMInteger secondOperand = convertToInt(operand);

        return new VMInteger(contents - secondOperand.contents);
    }

    public VMInteger multiply(VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Multiplying");
        VMInteger secondOperand = convertToInt(operand);

        return new VMInteger(contents * secondOperand.contents);
    }

    public VMInteger divide(VMObject operand) throws VMException {
        VMMachine.logger.log(Level.INFO, "Dividing");
        VMInteger secondOperand = convertToInt(operand);

        return new VMInteger(contents / secondOperand.contents);
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
    public VMType getType() {
        return VMType.INT;
    }
}
