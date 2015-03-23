package cz.cvut.fit.run.interpreter.core.helpers;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MagNet on 20. 3. 2015.
 */
public class VariableHash extends HashMap<VMIdentifierInstance, TypeValuePair> {
    public VariableHash(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public VariableHash(int initialCapacity) {
        super(initialCapacity);
    }

    public VariableHash() {
    }

    public VariableHash(Map<? extends VMIdentifierInstance, ? extends TypeValuePair> m) {
        super(m);
    }

    public boolean isVariableDeclared(VMIdentifierInstance identifier) {
        return containsKey(identifier);
    }

    public TypeValuePair declareVariable(VMIdentifierInstance identifier, VMType type) throws VMException {
        if (isVariableDeclared(identifier)) {
            throw new RedeclarationException(identifier.getValue());
        }

        TypeValuePair newPair = new TypeValuePair(type);
        put(identifier, newPair);

        return newPair;
    }

    public VMObject getVariable(VMIdentifierInstance identifier) throws NotDeclaredException {
        TypeValuePair fullVariable =  getPair(identifier);

        return fullVariable.getValue();
    }

    protected TypeValuePair getVariablePair(VMIdentifierInstance identifier) {
        if (containsKey(identifier))
            return get(identifier);

        return null;
    }

    public TypeValuePair getPair(VMIdentifierInstance identifier) throws NotDeclaredException {
        TypeValuePair variablePair = getVariablePair(identifier);

        if (variablePair == null)
            throw new NotDeclaredException("The variable " + identifier + " has not been yet declared.");

        return variablePair;
    }
}
