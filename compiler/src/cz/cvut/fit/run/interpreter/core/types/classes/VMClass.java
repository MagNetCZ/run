package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.parser.JavaParser;

import java.util.HashMap;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMClass extends VMBaseObject {
    JavaParser.ClassBodyContext source;

    private VMClass superClass = null;

    @Override
    public VMType getType() {
        return VMType.CLASS;
    }

    HashMap<String, VMObject> static_fields;
    HashMap<String, VMMethod> methods;

    public VMClass(VMClass superClass) {
        this.superClass = superClass;

        static_fields = new HashMap<>();
        methods = new HashMap<>();

        initMethods();
    }

    public VMClass() {
        this(null);
    }

    private VMObject[] injectTarget(VMObject instance, VMObject ... args) {
        VMObject[] injectedArgs = new VMObject[args.length + 1];
        injectedArgs[0] = instance;
        for (int i = 0; i < args.length; i++) {
            injectedArgs[i + 1] = args[i];
        }

        return injectedArgs;
    }

    public void callMethod(String name, VMObject target, VMObject ... args) throws VMException {
        VMMethod method = lookupMethod(name);

        VMObject[] injectedArgs = injectTarget(target, args);
        if (method.getReturnType() == VMType.VOID) {
            method.invoke(this, injectedArgs);
        } else {
            VMMachine.push(method.invoke(this, injectedArgs));
        }
    }

    public VMMethod lookupMethod(String name) {
        // TODO static vs instance methods
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

    public VMObject createInstance(VMObject ... args) {
        return new VMObject(this); // TODO constructor arguments
    }
}
