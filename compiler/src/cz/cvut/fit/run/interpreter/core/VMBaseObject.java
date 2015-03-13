package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.VMType;
import cz.cvut.fit.run.parser.JavaParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBaseObject extends VMReference {
    HashMap<String, VMObject> fields;
    HashMap<String, VMMethod> methods;

    public VMBaseObject() {
        fields = new HashMap<>();
        methods = new HashMap<>();

        initMethods();
    }

    public abstract VMType getType();

    public void callMethod(String name, VMObject ... args) throws VMException {
        VMMethod method = lookupMethod(name);

        if (method.getReturnType() == VMType.VOID) {
            method.invoke(this, args);
        } else {
            VMMachine.push(method.invoke(this, args));
        }
    }

    public VMMethod lookupMethod(String name) {
        return methods.get(name);
    };

    /**
     * Initialize builtin methods
     */
    public void initMethods() {

    }

    public void declareMethod(VMMethod method) {
        methods.put(method.getName(), method);
    }
}
