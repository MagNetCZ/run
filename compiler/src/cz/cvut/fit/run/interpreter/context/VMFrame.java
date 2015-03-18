package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
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

    public void assignVariable(VMIdentifierInstance identifier, VMObject value) throws NotDeclaredException {
        getFullVariable(identifier).setValue(value);
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
        return getFullVariable(identifier).getValue();
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
