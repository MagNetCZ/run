package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.*;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.memory.VMPointer;
import cz.cvut.fit.run.parser.JavaParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMMethod extends VMReference {
    private String name;
    private VMType returnType;
    private JavaParser.BlockContext code;
    private Method nativeCode;

    private VMType[] argTypes;
    private String[] argNames; // Separated because names are used only for VM methods

    private Modifiers modifiers;

    boolean nativeMethod;

    public VMMethod(String name, Modifiers modifiers,
                    VMType returnType, JavaParser.BlockContext code, VMType[] argTypes, String[] argNames) {
        this.name = name;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.code = code;
        this.argTypes = argTypes;
        this.argNames = argNames;

        nativeMethod = false;
    }

    public VMMethod(String name, Modifiers modifiers,
                    VMType returnType, Method nativeCode, VMType ... argTypes) {
        this.name = name;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.nativeCode = nativeCode;
        this.argTypes = argTypes;

        nativeMethod = true;
    }

    private void checkNumberOfArguments(VMPointer ... args) throws ArgumentException {
        if (args.length != argTypes.length)
            throw new ArgumentException(name + ": Given " + args.length + " arguments, but " + argTypes.length + " are required.");
    }

    public VMObject invoke(VMBaseObject onObject, VMPointer ... args) throws VMException {
        checkNumberOfArguments(args);

        if (nativeMethod) {
            return invokeNative(onObject, args);
        } else {
            return invokeVM(args);
        }
    }

    private void loadArgs(VMPointer... args) throws VMException {
        VMMachine vm = VMMachine.getInstance();

        for (int i = 0; i < args.length; i++) {
            String argName = argNames[i];
            vm.getCurrentFrame().
                    declareVariable(vm.getID(argName), argTypes[i]).
                    setValue(args[i]);
        }
    }

    private VMObject invokeVM(VMPointer ... args) throws VMException {
        VMMachine vm = VMMachine.getInstance();

        loadArgs(args);

        try {
            vm.evalMethod(code);
        } catch (ReturnException ret) {
            if (returnType != VMType.VOID) {
                if (ret.getValue() == null)
                    throw new MissingReturnException();

                if (!ret.getValue().canBeAssignedTo(returnType))
                    throw new TypeMismatchException("Wrong return type");
            }

            return ret.getValue();
        }

        throw new RuntimeException("Should not ever get here");
    }

    private VMObject invokeNative(VMBaseObject onObject, VMPointer ... args) throws VMException {
        try {
            return (VMObject)nativeCode.invoke(onObject, args);
        } catch (IllegalAccessException e) {
            throw new MethodNotFoundException(e);
        } catch (InvocationTargetException e) {
            throw new MethodNotFoundException(e);
        }
    }

    public String getName() {
        return name;
    }

    public VMType getReturnType() {
        return returnType;
    }

    public boolean isStaticMethod() {
        return modifiers.isStatic();
    }

    public Modifiers getModifiers() {
        return modifiers;
    }

    public boolean headersMatch(VMMethod other) {
        if (!modifiers.equals(other.modifiers))
            return false;

        int i = modifiers.isStatic() ? 0 : 1;

        if (argTypes.length != other.argTypes.length)
            return false;

        for (; i < argTypes.length; i++)
            if (!argTypes[i].equals(other.argTypes[i]))
                return false;

        return returnType.equals(other.returnType);
    }
}
