package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.helpers.VariableHash;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;

import java.util.Stack;
import java.util.logging.Level;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMFrame {
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

    public TypeValuePair declareVariable(VMIdentifierInstance identifier, VMType type) throws VMException {
        if (isVariableDeclared(identifier)) {
            throw new RedeclarationException(identifier.getValue());
        }

        return localVariableStack.peek().declareVariable(identifier, type);
    }

    private boolean isVariableDeclared(VMIdentifierInstance identifier) {
        for (VariableHash variableHash : localVariableStack) {
            if (variableHash.isVariableDeclared(identifier))
                return true;
        }

        return false;
    }

    private VariableHash getHashWithVariable(VMIdentifierInstance identifier) throws NotDeclaredException {
        for (VariableHash variableHash : localVariableStack) {
            if (variableHash.containsKey(identifier))
                return variableHash;
        }

        throw new NotDeclaredException("The variable " + identifier + " has not been yet declared.");
    }

    public VMObject getVariable(VMIdentifierInstance identifier) throws NotDeclaredException {
        return getHashWithVariable(identifier).getVariable(identifier);
    }

    public TypeValuePair getPair(VMIdentifierInstance identifier) throws NotDeclaredException {
        return getHashWithVariable(identifier).getPair(identifier);
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

    public int stackSize() {
        return opStack.size();
    }

    public VMObject peek() {
        return opStack.peek();
    }

    public void clearStack() {
        opStack.clear();
    }


    public void enterScope() {
        localVariableStack.push(new VariableHash());
    }

    public void exitScope() {
        localVariableStack.pop();
    }
}
