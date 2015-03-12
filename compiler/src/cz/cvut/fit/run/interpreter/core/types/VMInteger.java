package cz.cvut.fit.run.interpreter.core.types;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMObject;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;

import java.util.ArrayList;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMInteger extends VMSimpleType<Integer> {
    public VMInteger(String stringValue) {
        super(Integer.parseInt(stringValue));
    }

    public VMInteger(Integer contents) {
        super(contents);
    }

    public static VMInteger convertToInt(VMObject object) {
        // TODO support for other numeric types
        return (VMInteger)object;
    }

    public void sum(VMObject operand) throws VMException {
        System.out.println("summing");
        VMInteger secondOperand = convertToInt(operand);

        VMMachine.push(new VMInteger(contents + secondOperand.contents));
    }

    @Override
    public void callMethod(String name, VMObject ... args) throws VMException {
        switch (name) {
            case "+":
                sum(args[0]);
                return;
        }
        super.callMethod(name, args);
    }
}
