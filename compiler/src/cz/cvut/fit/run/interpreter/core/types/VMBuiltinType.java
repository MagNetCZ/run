package cz.cvut.fit.run.interpreter.core.types;

import cz.cvut.fit.run.interpreter.core.VMMethod;
import cz.cvut.fit.run.interpreter.core.VMObject;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public abstract class VMBuiltinType<T> extends VMObject {
    protected T contents;

    public VMBuiltinType(T contents) {
        this.contents = contents;
    }

    public T getContents() {
        return contents;
    }

    public void setContents(T contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "<" + getType().toString() + ": " + contents.toString() + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMBuiltinType)) return false;

        VMBuiltinType that = (VMBuiltinType) o;

        if (contents != null ? !contents.equals(that.contents) : that.contents != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return contents != null ? contents.hashCode() : 0;
    }

    public void registerBuiltinMethods() {
        Class<?> c = this.getClass();

        try {
            List<BuiltinMethodIdentifier> builtinMethods = getBuiltinMethods();

            for (BuiltinMethodIdentifier builtinMethod : builtinMethods) {
                int argsSize = builtinMethod.argTypes.length;

                Class<?>[] argClasses = new Class[argsSize];
                for (int i = 0; i < argsSize; i++)
                    argClasses[i] = VMObject.class;

                Method method = c.getDeclaredMethod(builtinMethod.nameNative, argClasses);

                VMMethod vmMethod = new VMMethod(builtinMethod.nameVM,
                        builtinMethod.returnType, method, builtinMethod.argTypes);

                declareMethod(vmMethod);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            // TODO propagate
        }
    }

    @Override
    public void initMethods() {
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
}
