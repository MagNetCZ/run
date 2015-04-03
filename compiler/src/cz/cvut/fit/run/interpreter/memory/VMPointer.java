package cz.cvut.fit.run.interpreter.memory;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMBoolean;
import cz.cvut.fit.run.interpreter.core.types.instances.VMNullInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 3. 4. 2015.
 */
public class VMPointer {
    private final static int NULL_LOCATION = 1;
    private final static int TRUE_LOCATION = 2;
    private final static int FALSE_LOCATION = 3;

    public final static VMPointer NULL_POINTER = new VMPointer(NULL_LOCATION, true);
    public final static VMPointer TRUE_POINTER = new VMPointer(TRUE_LOCATION, true);
    public final static VMPointer FALSE_POINTER = new VMPointer(FALSE_LOCATION, true);

    private int location;

    public VMPointer(int memoryLocation) {
        if (location < 0)
            throw new IllegalArgumentException("Memory location cannot be below zero");
        this.location = memoryLocation;
    }

    private VMPointer(int memoryLocation, boolean negative) {
        if (negative)
            this.location = -memoryLocation;
        else
            this.location = memoryLocation;
    }


    public int getLocation() {
        return location;
    }

    public VMObject getObject() throws VMException {
        switch (location) {
            case -NULL_LOCATION:
                return VMNullInstance.getInstance();
            case -TRUE_LOCATION:
                return VMBoolean.TRUE;
            case -FALSE_LOCATION:
                return VMBoolean.FALSE;
        }

        return VMMemory.getInstance().get(this);
    }
}
