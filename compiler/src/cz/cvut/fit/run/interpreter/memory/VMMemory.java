package cz.cvut.fit.run.interpreter.memory;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.OutOfMemoryException;
import cz.cvut.fit.run.interpreter.core.exceptions.MemoryNotInitializedException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMOutOfBoundsException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * The Heap
 */
public class VMMemory {
    private static VMMemory instance;
    private VMObject[] memory;
    private VMGarbageCollector gc;
    private int gcDisableCounter;

    class MemoryInfo {
        // Points to the next free memory
        private int current;
        private final int bottom;
        private final int top;

        MemoryInfo(int bottom, int top) {
            this.bottom = bottom;
            this.current = bottom;
            this.top = top;
        }

        public int getBottom() {
            return bottom;
        }

        public int getTop() {
            return top;
        }

        public void reset() {
            current = bottom;
        }

        public int getNext() throws VMException {
            if (current > top)
                throw new OutOfMemoryException();

            current++;
            return current - 1;
        }
    }

    MemoryInfo leftInfo;
    MemoryInfo rightInfo;

    enum Memory {
        LEFT,
        RIGHT
    }

    private Memory currentMemory;

    public void init(int size) {
        memory = new VMObject[size];

        leftInfo = new MemoryInfo(0, size / 2);
        rightInfo = new MemoryInfo(size / 2, size);

        currentMemory = Memory.LEFT;
    }

    public VMMemory() {
        gc = new VMGarbageCollector(this);
        gcDisableCounter = 0;
    }

    public static VMMemory getInstance() {
        if (instance == null)
            instance = new VMMemory();
        return instance;
    }

    public void checkMemory() throws VMException {
        if (memory == null)
            throw new MemoryNotInitializedException();
    }

    public VMObject get(VMPointer pointer) throws VMException {
        checkMemory();

        int location = pointer.getLocation();
        if (location < 0 || location >= memory.length)
            throw new VMOutOfBoundsException("Trying to access outside of allocated memory");
        return memory[location];
    }

    private MemoryInfo currentInfo() {
        switch (currentMemory) {
            case LEFT:
                return leftInfo;
            case RIGHT:
                return rightInfo;
        }

        throw new IndexOutOfBoundsException();
    }

    private MemoryInfo otherInfo() {
        switch (currentMemory) {
            case LEFT:
                return rightInfo;
            case RIGHT:
                return leftInfo;
        }

        throw new IndexOutOfBoundsException();
    }

    public VMPointer alloc(VMObject object) throws VMException {
        gcPoint();

        int pointer = currentInfo().getNext();
        memory[pointer] = object;
        VMPointer objectPointer = new VMPointer(pointer);
        object.setPointer(objectPointer);

        VMMachine.logger.severe("Allocating [" + pointer + "] " + object);
        VMMachine.logger.severe("Available " + memoryAvailable());

        return objectPointer;
    }

    public static VMPointer allocate(VMObject object) throws VMException {
        return getInstance().alloc(object);
    }

    public int memoryAvailable() {
        MemoryInfo current = currentInfo();
        return current.top - current.current;
    }

    public int memoryCapacity() {
        MemoryInfo current = currentInfo();
        return current.top - current.bottom;
    }

    public void gcPoint() throws VMException {
        if (memoryAvailable() < memoryCapacity() * 0.25)
            gc.garbageCollect();
    }

    /**
     * Switch between using left or right memory
     */
    public void switchMemory() {
        currentMemory = currentMemory == Memory.LEFT ? Memory.RIGHT : Memory.LEFT;
    }

    /**
     * Wipe the currently unused part of memory
     */
    public void wipeUnused() {
        MemoryInfo toWipeInfo = otherInfo();
        for (int i = toWipeInfo.bottom; i < toWipeInfo.top; i++)
            memory[i] = null;

        toWipeInfo.reset();
    }

    public boolean inActive(VMPointer pointer) {
        int location = pointer.getLocation();
        MemoryInfo curInfo = currentInfo();
        return location >= curInfo.bottom && location < curInfo.top;
    }

    public void disableGC() {
        gcDisableCounter++;
        gc.setDisabled(gcDisableCounter > 0);
    }

    public void enableGC() throws VMException {
        gcDisableCounter--;
        gc.setDisabled(gcDisableCounter > 0);
        gcPoint();
    }
}
