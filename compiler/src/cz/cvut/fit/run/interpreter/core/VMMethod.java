package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.exceptions.MethodNotFoundException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.VMType;
import cz.cvut.fit.run.parser.JavaParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMMethod extends VMReference {
    private String name;
    private VMType returnType;
    private JavaParser.MethodBodyContext code;
    private Method nativeCode;
    private VMType[] arguments;

    boolean nativeMethod;

    public VMMethod(String name, VMType returnType, JavaParser.MethodBodyContext code, VMType ... arguments) {
        this.name = name;
        this.returnType = returnType;
        this.code = code;
        this.arguments = arguments;

        nativeMethod = false;
    }

    public VMMethod(String name, VMType returnType, Method nativeCode, VMType ... arguments) {
        this.name = name;
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

    private VMObject invokeVM(VMBaseObject onObject, VMObject ... args) {
        throw new NotImplementedException();
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
}
