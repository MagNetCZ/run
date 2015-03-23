package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.MethodNotFoundException;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.traversion.FieldInitializeVisitorBuilder;
import cz.cvut.fit.run.interpreter.traversion.MethodInitializeVisitorBuilder;
import cz.cvut.fit.run.interpreter.traversion.ModifierFilter;
import cz.cvut.fit.run.parser.JavaParser;

import java.util.HashMap;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMClass extends VMBaseObject {
    private JavaParser.ClassBodyContext source;
    private boolean initialized;

    private VMClass superClass = null;

    private VMType type;

    HashMap<String, VMMethod> methods;

    public VMClass(VMType type, VMClass superClass) throws VMException {
        this.superClass = superClass;
        this.type = type;

        methods = new HashMap<>();

        initMethods();
    }

    private VMClass() throws VMException {
        this(VMType.CLASS, null);
    }

    public VMClass(VMType type) throws VMException {
        this(type, null);
    }

    public VMClass(VMType type, VMClass superClass, JavaParser.ClassBodyContext source) throws VMException {
        this(type, superClass);
        this.source = source;
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

    public void callMethod(String name, VMObject ... args) throws VMException {
        VMMethod method = lookupMethod(name);
        if (!method.isStaticMethod())
            throw new MethodNotFoundException("Trying to call instance method from static context");

        invokeMethod(method, args);
    }

    public void callMethod(String name, VMObject target, VMObject ... args) throws VMException {
        VMMethod method = lookupMethod(name);
        if (method.isStaticMethod())
            throw new MethodNotFoundException("Trying to call static method from instance context");

        VMObject[] injectedArgs = injectTarget(target, args);
        invokeMethod(method, injectedArgs);
    }

    private void invokeMethod(VMMethod method, VMObject ... args) throws VMException {
        // TODO check argument types
        if (method.getReturnType() == VMType.VOID) {
            method.invoke(this, args);
        } else {
            VMMachine.push(method.invoke(this, args));
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
    protected void initMethods() throws VMException {

    }

    public void declareMethod(VMMethod method) {
        methods.put(method.getName(), method);
    }

    public VMObject createInstance(VMObject ... args) throws VMException {
        return new VMObject(this, args);
    }

    public void initialize() throws VMException {
        if (initialized)
            return;

        // TODO get methods from source
        if (source != null) {
            FieldInitializeVisitorBuilder builder =
                    new FieldInitializeVisitorBuilder(this, new ModifierFilter("static", false));
            VMException ex = builder.visit(source);
            if (ex != null)
                throw ex;

            MethodInitializeVisitorBuilder methodBuilder =
                    new MethodInitializeVisitorBuilder(this);
            ex = methodBuilder.visit(source);
            if (ex != null)
                throw ex;
        }

        initialized = true;
    }

    public JavaParser.ClassBodyContext getSource() {
        return source;
    }
}
