package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.TypeValuePair;
import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.MethodNotFoundException;
import cz.cvut.fit.run.interpreter.core.exceptions.NotDeclaredException;
import cz.cvut.fit.run.interpreter.core.exceptions.RedeclarationException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.modifiers.Scope;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIdentifierInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.traversion.FieldInitializeVisitorBuilder;
import cz.cvut.fit.run.interpreter.traversion.MethodInitializeVisitorBuilder;
import cz.cvut.fit.run.interpreter.traversion.ModifierFilter;
import cz.cvut.fit.run.parser.JavaParser;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMClass extends VMBaseObject {
    private JavaParser.ClassBodyContext source;
    private boolean initialized;

    private VMClass superClass = null;
    private JavaParser.TypeContext superType = null;

    private VMType type;

    HashMap<String, VMMethod> methods;
    VMMethod constructor;

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

    public VMClass(VMType type, JavaParser.TypeContext superType, JavaParser.ClassBodyContext source) throws VMException {
        this(type, null);
        this.superType = superType;
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
        callMethod(method, target, args);
    }

    public void callMethod(VMMethod method, VMObject target, VMObject ... args) throws VMException {
        if (method.isStaticMethod())
            throw new MethodNotFoundException("Trying to call static method from instance context");

        VMObject[] injectedArgs = injectTarget(target, args);
        invokeMethod(method, injectedArgs);
    }

    private void invokeMethod(VMMethod method, VMObject ... args) throws VMException {
        VMMachine vm = VMMachine.getInstance();
        vm.enterFrame();

        VMObject methodResult = null;
        VMException exception = null;

        try {
            methodResult = method.invoke(this, args);
        } catch (VMException ex) {
            exception = ex;
        } finally {
            vm.exitFrame(exception);
        }

        if (method.getReturnType() != VMType.VOID)
            VMMachine.push(methodResult);
    }

    public VMMethod lookupMethod(String name) throws VMException {
        if (!methods.containsKey(name)) {
            if (superClass == null)
                throw new NotDeclaredException(name);

            superClass.initialize();
            return superClass.lookupMethod(name);
        }
        return methods.get(name);
    }

    public void declareMethod(VMMethod method) {
        // TODO check overriding methods for matching headers
        methods.put(method.getName(), method);
    }

    public void setConstructor(VMMethod constructor) {
        this.constructor = constructor;
    }

    public VMObject createInstance(VMObject ... args) throws VMException {
        VMObject newInstance = new VMObject(this, args);

        if (constructor != null)
            callMethod(constructor, newInstance, args);

        return newInstance;
    }

    public void initialize() throws VMException {
        if (initialized)
            return;

        if (superType != null && superClass == null) {
            superClass = VMMachine.getInstance().getClazz(superType);
        }

        if (source != null) {
            FieldInitializeVisitorBuilder builder =
                    new FieldInitializeVisitorBuilder(this, new ModifierFilter("static", false));
            VMException ex = builder.visit(source);
            if (ex != null)
                throw ex;

            MethodInitializeVisitorBuilder methodBuilder =
                    new MethodInitializeVisitorBuilder(this);

            methodBuilder.visit(source);

            if (methodBuilder.exception != null)
                throw methodBuilder.exception;
        }

        initialized = true;
    }

    private void setSuperClass(VMClass superClass) throws VMException {
        if (hasSuper())
            throw new RedeclarationException("Super class already defined");
        this.superClass = superClass;
    }

    public VMClass getSuperClass() {
        return superClass;
    }

    public boolean isDescendantOf(VMType superType) {
        for (VMClass clazz : getClassHierarchy()) {
            if (clazz.getType().equals(superType))
                return true;
        }

        return false;
    }

    public JavaParser.ClassBodyContext getSource() {
        return source;
    }

    public boolean canBeAssignedTo(VMType type) {
        if (getType() == VMType.NULL)
            return true;

        if (getType().equals(type))
            return true;

        if (hasSuper())
            return superClass.canBeAssignedTo(type);

        return false;
    }

    public boolean hasSuper() {
        return superClass != null;
    }

    // Builtin Methods

    public void registerBuiltinMethods() throws VMException {
        Class<?> c = this.getClass();

        try {
            List<BuiltinMethodIdentifier> builtinMethods = getBuiltinMethods();

            for (BuiltinMethodIdentifier builtinMethod : builtinMethods) {
                Modifiers modifiers = builtinMethod.modifiers;
                boolean instanceMethod = !modifiers.isStatic();

                LinkedList<Class<?>> argClasses = new LinkedList<Class<?>>();
                LinkedList<VMType> argTypes = new LinkedList<>();

                if (instanceMethod) {
                    argClasses.add(VMObject.class);
                    argTypes.add(getType());
                }

                for (int i = 0; i < builtinMethod.argTypes.length; i++) {
                    argTypes.add(builtinMethod.argTypes[i]);
                    argClasses.add(VMObject.class);
                }

                Class<?>[] argClassArray = argClasses.toArray(new Class<?>[argClasses.size()]);
                VMType[] argTypeArray = argTypes.toArray(new VMType[argTypes.size()]);

                Method method = c.getDeclaredMethod(builtinMethod.nameNative, argClassArray);

                VMMethod vmMethod = new VMMethod(builtinMethod.nameVM, modifiers,
                        builtinMethod.returnType, method, argTypeArray);

                declareMethod(vmMethod);
            }

        } catch (NoSuchMethodException e) {
            throw new VMException(e);
        }
    }

    public void initMethods() throws VMException {
        registerBuiltinMethods();
    }

    @Override
    public TypeValuePair getField(VMIdentifierInstance identifier) throws VMException {
        try {
            return super.getField(identifier);
        } catch (NotDeclaredException e) {
            if (hasSuper())
                return superClass.getField(identifier);
            throw e;
        }
    }

    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        return new ArrayList<>();
    };

    class BuiltinMethodIdentifier {

        String nameNative;
        String nameVM;
        VMType returnType;
        VMType[] argTypes;
        Modifiers modifiers;
        BuiltinMethodIdentifier(String nameNative, String nameVM, VMType returnType, VMType ... argTypes) {
            this.nameNative = nameNative;
            this.nameVM = nameVM;
            this.returnType = returnType;
            this.argTypes = argTypes;
            this.modifiers = new Modifiers();
        }

        BuiltinMethodIdentifier(boolean staticFlag, String nameNative, String nameVM, VMType returnType, VMType ... argTypes) {
            this.nameNative = nameNative;
            this.nameVM = nameVM;
            this.returnType = returnType;
            this.argTypes = argTypes;
            this.modifiers = new Modifiers(staticFlag, true, Scope.PUBLIC);
        }
    }

    /**
     * Get a list of connected superclasses, starting with the topmost class and including this class.
     * @return
     */
    public List<VMClass> getClassHierarchy() {
        LinkedList<VMClass> classList = new LinkedList<>();

        VMClass curClass = this;
        while (curClass != null) {
            classList.add(curClass);
            curClass = curClass.superClass;
        }

        Collections.reverse(classList);

        return classList;
    }
}
