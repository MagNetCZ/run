package cz.cvut.fit.run.interpreter.core.helpers;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.TypeMismatchException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMArrayInstance;
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

    public void declareVariable(VMIdentifierInstance identifier, VMType type) throws VMException {
        if (isVariableDeclared(identifier)) {
            throw new RedeclarationException(identifier.getValue());
        }

        put(identifier, new TypeValuePair(type));
    }

    public void assignVariable(VMIdentifierInstance identifier, VMObject value) throws VMException {
        TypeValuePair tvp = getFullVariable(identifier);

        if (!identifier.isArrayIndex()) {
            // Non arrays
            if (!tvp.getType().canBeAssignedTo(value)) {
                throw new TypeMismatchException();
            }

            tvp.setValue(value);
        } else {
            // Array index assignment
            VMArrayType arrayType = (VMArrayType)tvp.getType();
            if (!arrayType.getContentType().canBeAssignedTo(value)) {
                throw new TypeMismatchException();
            }

            VMArrayInstance array = (VMArrayInstance)tvp.getValue();
            array.set(identifier.getArrayIndex(), value);
        }

    }

    public VMObject getVariable(VMIdentifierInstance identifier) throws NotDeclaredException {
        TypeValuePair fullVariable =  getFullVariable(identifier);

        if (!identifier.isArrayIndex()) {
            // Non arrays
            return fullVariable.getValue();
        } else {
            // Array index access
            VMArrayInstance array = (VMArrayInstance)fullVariable.getValue();
            return array.get(identifier.getArrayIndex());
        }
    }

    protected TypeValuePair getVariablePair(VMIdentifierInstance identifier) {
        if (containsKey(identifier))
            return get(identifier);

        return null;
    }

    protected TypeValuePair getFullVariable(VMIdentifierInstance identifier) throws NotDeclaredException {
        TypeValuePair variablePair = getVariablePair(identifier);

        if (variablePair == null)
            throw new NotDeclaredException("The variable " + identifier + " has not been yet declared.");

        return variablePair;
    }
}
