package cz.cvut.fit.run.interpreter.core;

import cz.cvut.fit.run.interpreter.core.types.VMType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMObject extends VMBaseObject {
    VMClass clazz;

    @Override
    public VMType getType() {
        return VMType.REFERENCE;
    }
}
