package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMFileNotFoundException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMIOException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMFileInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.instances.VMStringInstance;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 26. 3. 2015.
 */
public class VMFile extends VMBuiltinType<BufferedReader, VMFileInstance> {
    public VMFile() throws VMException {
        super(VMType.FILE);
    }

    @Override
    public VMFileInstance createInstance(BufferedReader value) throws VMException {
        return new VMFileInstance(this, value);
    }

    @Override
    public VMObject createInstance(VMObject... args) throws VMException {
        VMStringInstance filenameString = (VMStringInstance)args[0];
        String filename = filenameString.getValue();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            return createInstance(br);
        } catch (FileNotFoundException ex) {
            throw new VMFileNotFoundException(filename);
        }
    }

    public VMStringInstance readLine(VMObject instance) throws VMException {
        VMFileInstance typedInstance = (VMFileInstance)instance;

        try {
            VMMachine vm = VMMachine.getInstance();
            VMString stringClass = (VMString)(vm.getClazz("String"));
            return stringClass.createInstance(typedInstance.getValue().readLine());
        } catch (IOException ex) {
            throw new VMIOException(ex);
        }
    }

    @Override
    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        List<BuiltinMethodIdentifier> builtinMethods = new LinkedList<>();

        builtinMethods.add(new BuiltinMethodIdentifier("readLine", "readLine", VMType.STRING));

        return builtinMethods;
    }
}
