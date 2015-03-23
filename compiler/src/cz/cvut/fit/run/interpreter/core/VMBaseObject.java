package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.helpers.VariableHash;
import cz.cvut.fit.run.interpreter.core.types.classes.VMIdentifier;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Stack;
import java.util.logging.Level;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBaseObject extends VMReference {
    private VariableHash fields;

    protected VMBaseObject() throws VMException {
        fields = new VariableHash();
    }

    public abstract VMType getType();

    public void declareField(VMIdentifierInstance identifier, VMType type) throws VMException {
        VMMachine.logger.info("Declaring field " + identifier);

        fields.declareVariable(identifier, type);
    }

    public TypeValuePair getField(VMIdentifierInstance identifier) throws VMException {
        return fields.getPair(identifier);
    }

    public abstract void callMethod(String name, VMObject ... args) throws VMException;
}
