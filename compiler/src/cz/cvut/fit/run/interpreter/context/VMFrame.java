package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.VMObject;
import cz.cvut.fit.run.interpreter.core.VMReference;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.types.VMIdentifier;
import cz.cvut.fit.run.interpreter.core.types.VMString;
import cz.cvut.fit.run.interpreter.core.types.VMType;

import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMFrame {
    private HashMap<VMIdentifier, TypeValuePair> localVariables;
    private Stack<VMObject> opStack;
    
    VMFrame parent;

    public VMFrame() {
        this(null);
    }

    public VMFrame(VMFrame parent) {
        this.localVariables = new HashMap<>();
        this.opStack = new Stack<>();
        this.parent = parent;
    }

    public void declareVariable(VMIdentifier identifier, VMType type) {
        localVariables.put(identifier, new TypeValuePair(type));
    }

    public void assignVariable(VMIdentifier identifier, VMObject value) throws NotDeclaredException {
        getFullVariable(identifier).setValue(value);
    }

    public TypeValuePair getFullVariable(VMIdentifier identifier) throws NotDeclaredException {
        if (!localVariables.containsKey(identifier))
            throw new NotDeclaredException("The variable " + identifier + " has not been yet declared.");

        return localVariables.get(identifier);
    }

    public VMObject getVariable(VMIdentifier identifier) throws NotDeclaredException {
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
}
