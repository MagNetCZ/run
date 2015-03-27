package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBuiltinInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.instances.VMStringInstance;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class VMSystem extends VMClass {
    public VMSystem() throws VMException {
        super(VMType.SYSTEM, null);
    }

    public void println(VMObject obj) {
        System.out.println(obj.toString()); // TODO own to string method
    }

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        builtinMethods.add(new BuiltinMethodIdentifier(true, "println", "println", VMType.VOID, VMType.STRING));

        return builtinMethods;
    }
}
