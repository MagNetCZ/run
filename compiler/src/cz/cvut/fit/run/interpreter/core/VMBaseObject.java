package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.parser.JavaParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMBaseObject extends VMReference {
    HashMap<String, VMObject> fields;
    HashMap<String, VMMethod> methods;

    public void callMethod(String name, VMObject ... args) throws VMException {
        throw new NotImplementedException();
    }
    public void declareMethod(String name, JavaParser.MethodBodyContext methodReference) {
        // TODO
        throw new NotImplementedException();
    }
}
