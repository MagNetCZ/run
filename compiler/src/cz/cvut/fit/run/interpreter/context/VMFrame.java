package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.TypeMismatchException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMArrayInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMFrame {
    class VariableHash extends HashMap<VMIdentifierInstance, TypeValuePair> {
        VariableHash(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        VariableHash(int initialCapacity) {
            super(initialCapacity);
        }

        VariableHash() {
        }

        VariableHash(Map<? extends VMIdentifierInstance, ? extends TypeValuePair> m) {
            super(m);
        }
    }

    private Stack<VariableHash> localVariableStack;
    private Stack<VMObject> opStack;
    
    VMFrame parent;

    public VMFrame() {
        this(null);
    }

    public VMFrame(VMFrame parent) {
        this.opStack = new Stack<>();
        this.parent = parent;
        this.localVariableStack = new Stack<>();
        enterScope();
    }

    public void declareVariable(VMIdentifierInstance identifier, VMType type) throws RedeclarationException {
        TypeValuePair valuePair = getVariablePair(identifier);
        if (valuePair != null) {
            throw new RedeclarationException(identifier.getValue());
        }

        localVariableStack.peek().put(identifier, new TypeValuePair(type));
    }

    public boolean canAssignTo(VMObject value, VMType type) {
        if (!value.getType().equals(type)) {
            return false;
            // TODO inherited type checking (iterate through object type and its super types)
        }

        return true;
    }

    public void assignVariable(VMIdentifierInstance identifier, VMObject value) throws VMException {
        // TODO array

        TypeValuePair tvp = getFullVariable(identifier);

        if (!identifier.isArrayIndex()) {
            // Non arrays
            if (!canAssignTo(value, tvp.getType())) {
                throw new TypeMismatchException();
            }

            tvp.setValue(value);
        } else {
            // Array index assignment
            VMArrayType arrayType = (VMArrayType)tvp.getType();
            if (!canAssignTo(value, arrayType.getContentType())) {
                throw new TypeMismatchException();
            }

            VMArrayInstance array = (VMArrayInstance)tvp.getValue();
            array.set(identifier.getArrayIndex(), value);
        }

    }

    private TypeValuePair getVariablePair(VMIdentifierInstance identifier) {
        for (VariableHash variableHash : localVariableStack) {
            if (variableHash.containsKey(identifier))
                return variableHash.get(identifier);
        }

        return null;
    }

    public TypeValuePair getFullVariable(VMIdentifierInstance identifier) throws NotDeclaredException {
        TypeValuePair variablePair = getVariablePair(identifier);

        if (variablePair == null)
            throw new NotDeclaredException("The variable " + identifier + " has not been yet declared.");

        return variablePair;
    }

    public VMObject getVariable(VMIdentifierInstance identifier) throws NotDeclaredException {
        // TODO use array
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

    public void push(VMObject value) {
        VMMachine.logger.log(Level.INFO, "Push " + value);
        opStack.push(value);
    }

    public VMObject pop() {
        VMObject object = opStack.pop();
        VMMachine.logger.log(Level.INFO, "Pop " + object);
        return object;
    }

    public void enterScope() {
        localVariableStack.push(new VariableHash());
    }

    public void exitScope() {
        localVariableStack.pop();
    }
}
