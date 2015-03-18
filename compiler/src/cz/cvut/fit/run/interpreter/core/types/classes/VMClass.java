package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
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

    private VMType type;

    HashMap<String, VMObject> static_fields;

    HashMap<String, VMMethod> methods;
    public VMClass(VMType type, VMClass superClass) throws VMException {
        this.superClass = superClass;
        this.type = type;

        static_fields = new HashMap<>();
        methods = new HashMap<>();

        initMethods();
    }

    private VMClass() throws VMException {
        this(VMType.CLASS, null);
    }

    public VMClass(VMType type) throws VMException {
        this(type, null);
    }

    public VMType getType() {
        return type;
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

        // TODO check argument types

        VMObject[] injectedArgs = injectTarget(target, args);
        if (method.getReturnType() == VMType.VOID) {
            method.invoke(this, injectedArgs);
        } else {
            VMMachine.push(method.invoke(this, injectedArgs));
        }
    }

    public VMMethod lookupMethod(String name) throws VMException {
        // TODO static vs instance methods
        if (!methods.containsKey(name))
            throw new NotDeclaredException(name); // TODO method lookup in source
        return methods.get(name);
    };

    /**
     * Initialize builtin methods
     */
    public void initMethods() throws VMException {

    }

    public void declareMethod(VMMethod method) {
        methods.put(method.getName(), method);
    }

    public VMObject createInstance(VMObject ... args) {
        return new VMObject(this, args);
    }
}
