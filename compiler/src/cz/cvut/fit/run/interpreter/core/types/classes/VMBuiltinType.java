package cz.cvut.fit.run.interpreter.core.types.classes;

import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBuiltinInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
                int argsSize = builtinMethod.argTypes.length + 1; // TODO +1 only for instance methods

                Class<?>[] argClasses = new Class[argsSize];
                for (int i = 0; i < argsSize; i++)
                    argClasses[i] = VMObject.class;

                Method method = c.getDeclaredMethod(builtinMethod.nameNative, argClasses);

                VMMethod vmMethod = new VMMethod(builtinMethod.nameVM,
                        builtinMethod.returnType, method, builtinMethod.argTypes);

                declareMethod(vmMethod);
            }

        } catch (NoSuchMethodException e) {
            throw new VMException(e);
        }
    }

    public abstract InstanceType createInstance(T value);

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

        BuiltinMethodIdentifier(String nameNative, String nameVM, VMType returnType, VMType ... argTypes) {
            this.nameNative = nameNative;
            this.nameVM = nameVM;
            this.returnType = returnType;
            this.argTypes = argTypes;
        }
    }

    @Override
    public VMObject createInstance(VMObject... args) {
        return createInstance(((InstanceType)args[0]).getValue());
    }
}
