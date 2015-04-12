package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.memory.VMPointer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class VMSystem extends VMClass {
    public VMSystem() throws VMException {
        super(VMType.SYSTEM, null);
    }

    public void println(VMPointer obj) throws VMException {
        System.out.println(obj.getObject().toString()); // TODO own to string method
    }

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        builtinMethods.add(new BuiltinMethodIdentifier(true, "println", "println", VMType.VOID, VMType.STRING));

        return builtinMethods;
    }
}
