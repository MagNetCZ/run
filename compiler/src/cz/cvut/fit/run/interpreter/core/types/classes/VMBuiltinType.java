package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.modifiers.Scope;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBuiltinInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBuiltinType<T, InstanceType extends VMBuiltinInstance<T>> extends VMClass {
    protected VMBuiltinType(VMType type) throws VMException {
        super(type);
    }

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

    public abstract InstanceType createInstance(T value) throws VMException;

    @Override
    public void initMethods() throws VMException {
        registerBuiltinMethods();
        super.initMethods();
    }

    public List<BuiltinMethodIdentifier> getBuiltinMethods() {
        return new ArrayList<BuiltinMethodIdentifier>();
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

    @Override
    public VMObject createInstance(VMObject... args) throws VMException {
        return createInstance(((InstanceType)args[0]).getValue());
    }
}
