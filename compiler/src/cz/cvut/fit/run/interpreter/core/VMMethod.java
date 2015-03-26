package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.ArgumentException;
import cz.cvut.fit.run.interpreter.core.exceptions.MethodNotFoundException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.parser.JavaParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMMethod extends VMReference {
    private String name;
    private VMType returnType;
    private JavaParser.MethodBodyContext code;
    private Method nativeCode;

    private VMType[] argTypes;
    private String[] argNames; // Separated because names are used only for VM methods

    private Modifiers modifiers;

    boolean nativeMethod;

    public VMMethod(String name, Modifiers modifiers,
                    VMType returnType, JavaParser.MethodBodyContext code, VMType[] argTypes, String[] argNames) {
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

    private void checkNumberOfArguments(VMObject ... args) throws ArgumentException {
        if (args.length != argTypes.length)
            throw new ArgumentException("Given " + args.length + " arguments, but " + argTypes.length + " are required.");
    }

    public VMObject invoke(VMBaseObject onObject, VMObject ... args) throws VMException {
        // TODO create frame (check)


        checkNumberOfArguments(args);

        if (nativeMethod) {
            return invokeNative(onObject, args);
        } else {
            return invokeVM(args);
        }
    }

    private void loadArgs(VMObject ... args) throws VMException {
        VMMachine vm = VMMachine.getInstance();

        for (int i = 0; i < args.length; i++) {
            String argName = argNames[i];
            vm.getCurrentFrame().
                    declareVariable(vm.getID(argName), argTypes[i]).
                    setValue(args[i]);
        }
    }

    private VMObject invokeVM(VMObject ... args) throws VMException {
        VMMachine vm = VMMachine.getInstance();

        loadArgs(args);

        vm.evalMethod(code);
        if (returnType != VMType.VOID) {
            return vm.popValue();
        }

        return null;
    }

    private VMObject invokeNative(VMBaseObject onObject, VMObject ... args) throws VMException {
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
        return modifiers.isStaticFlag();
    }

    public Modifiers getModifiers() {
        return modifiers;
    }
}
