package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.TypeMismatchException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.helpers.VariableHash;
import cz.cvut.fit.run.interpreter.core.types.classes.VMArrayType;
import cz.cvut.fit.run.interpreter.core.types.classes.VMIdentifier;
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

    public void declareVariable(VMIdentifierInstance identifier, VMType type) throws VMException {
        if (isVariableDeclared(identifier)) {
            throw new RedeclarationException(identifier.getValue());
        }

        localVariableStack.peek().declareVariable(identifier, type);
    }

    public void assignVariable(VMIdentifierInstance identifier, VMObject value) throws VMException {
        getHashWithVariable(identifier).assignVariable(identifier, value);
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
