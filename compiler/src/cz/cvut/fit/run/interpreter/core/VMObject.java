package cz.cvut.fit.run.interpreter.core;

import java.util.HashMap;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMObject extends VMReference {
    VMClass clazz;

    HashMap<String, VMObject> fields;
    HashMap<String, VMMethod> methods;
}
