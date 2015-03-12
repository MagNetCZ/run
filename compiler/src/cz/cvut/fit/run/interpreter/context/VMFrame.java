package cz.cvut.fit.run.interpreter.context;

import cz.cvut.fit.run.interpreter.core.VMObject;
import cz.cvut.fit.run.interpreter.core.VMReference;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMFrame {
    HashMap<String, VMObject> localVariables;
    Stack<VMReference> opStack;
    
    VMFrame parent;

    public VMFrame() {
        this(null);
    }

    public VMFrame(VMFrame parent) {
        this.localVariables = new HashMap<>();
        this.opStack = new Stack<>();
        this.parent = parent;
    }
}
