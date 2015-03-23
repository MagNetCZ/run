package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.MethodNotFoundException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.modifiers.Modifiers;
import cz.cvut.fit.run.interpreter.core.types.classes.VMType;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.parser.JavaParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    private VMType[] arguments;

    private Modifiers modifiers;

    boolean nativeMethod;

    public VMMethod(String name, Modifiers modifiers,
                    VMType returnType, JavaParser.MethodBodyContext code, VMType ... arguments) {
        this.name = name;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.code = code;
        this.arguments = arguments;

        nativeMethod = false;
    }

    public VMMethod(String name, Modifiers modifiers,
                    VMType returnType, Method nativeCode, VMType ... arguments) {
        this.name = name;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.nativeCode = nativeCode;
        this.arguments = arguments;

        nativeMethod = true;
    }

    public VMObject invoke(VMBaseObject onObject, VMObject ... args) throws VMException {
        if (nativeMethod) {
            return invokeNative(onObject, args);
        } else {
            return invokeVM(onObject, args);
        }
    }

    private VMObject invokeVM(VMBaseObject onObject, VMObject ... args) throws VMException {
        VMMachine vm = VMMachine.getInstance();

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
