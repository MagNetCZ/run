package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.*;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIntegerInstance;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.memory.VMMemory;
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

    private int numArgs() {
        return argTypes.length;
    }

    private void checkNumberOfArguments() throws VMException {
        VMObject quantifier = VMMachine.popValue(); // Arg quantifier
        int argNum = ((VMIntegerInstance)quantifier).getValue();

        if (argNum != numArgs())
            throw new ArgumentException(name + ": Given " + argNum + " arguments, but " + numArgs() + " are required.");
    }

    public void invoke(VMClass onObject) throws VMException {
        checkNumberOfArguments();

        if (nativeMethod) {
            invokeNative(onObject);
        } else {
            invokeVM();
        }
    }

    private void loadArgs(VMPointer[] args) throws VMException {
        VMMachine vm = VMMachine.getInstance();

        for (int i = 0; i < args.length; i++) {
            String argName = argNames[i];
            vm.getCurrentFrame().
                    declareVariable(vm.getID(argName), argTypes[i]).
                    setValue(args[i]);
        }
    }

    private VMPointer[] popArgs(boolean reversed) throws VMException {
        VMPointer[] args = new VMPointer[numArgs()];

        int first = 0;
        if (!isStaticMethod()) {
            args[0] = VMMachine.pop(); // Instance "this" is at the top of the stack
            first = 1;
        }
        for (int i = numArgs() - 1; i >= first ; i--)
            args[i] = VMMachine.pop(); // While the rest of the args is ordered last to first

        return args;
    }

    private void invokeVM() throws VMException {
        VMMachine vm = VMMachine.getInstance();
        VMMemory mem = VMMemory.getInstance();

        mem.disableGC();

        VMPointer[] args = popArgs(true);
        vm.enterFrame();

        loadArgs(args);

        mem.enableGC();

        try {
            vm.evalMethod(code);
        } catch (ReturnException ret) {
            if (returnType != VMType.VOID) {
                if (ret.getValue() == null)
                    throw new MissingReturnException();

                if (!ret.getValue().canBeAssignedTo(returnType))
                    throw new TypeMismatchException("Wrong return type");

                VMMachine.push(ret.getValue().getPointer());
            }

            return;
        }

        throw new RuntimeException("Should not ever get here");
    }

    private void invokeNative(VMClass onObject) throws VMException {
        VMMachine vm = VMMachine.getInstance();
        VMMemory mem = VMMemory.getInstance();

        VMPointer[] args;
        try {
            mem.disableGC();

            args = popArgs(false);
            vm.enterFrame();
            Object result = nativeCode.invoke(onObject, args);

            if (returnType != VMType.VOID) {
                if (result == null)
                    VMMachine.push(VMPointer.NULL_POINTER);
                VMMachine.push((VMPointer)result);
            }
        } catch (IllegalAccessException e) {
            throw new MethodNotFoundException(e);
        } catch (InvocationTargetException e) {
            throw new MethodNotFoundException(e);
        } finally {
            mem.enableGC();
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
