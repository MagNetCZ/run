package cz.cvut.fit.run.interpreter.memory;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMSegmentationFault;
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

    private VMObject alloc_object;

    public VMPointer(int memoryLocation) throws VMException {
        if (location < 0)
            throw new IllegalArgumentException("Memory location cannot be below zero");
        this.location = memoryLocation;
        alloc_object = getObject(); // TODO REMOVE
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

        VMMemory memory = VMMemory.getInstance();
        VMObject object = memory.get(this);

        if (object == null)
            throw new VMSegmentationFault("Trying to access unitialized (deleted) memory at " + location);

        if (object.isRelocated())
            return memory.get(object.getRelocatedPointer());

        return object;
    }

    public boolean pointsToObject() {
        return location >= 0;
    }

    public VMObject getRawObject() throws VMException {
        if (location < 0)
            return null;

        VMMemory memory = VMMemory.getInstance();
        return memory.get(this);
    }

    @Override
    public String toString() {
        return "Pointer -> " + location;
    }
}
