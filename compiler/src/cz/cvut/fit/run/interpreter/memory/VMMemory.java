package cz.cvut.fit.run.interpreter.memory;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.exceptions.OutOfMemoryException;
import cz.cvut.fit.run.interpreter.core.exceptions.MemoryNotInitializedException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.exceptions.VMOutOfBoundsException;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * The Heap
 */
public class VMMemory {
    private static VMMemory instance;
    private VMObject[] memory;

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

        leftInfo = new MemoryInfo(0, size / 2 - 1);
        rightInfo = new MemoryInfo(size / 2, size - 1);

        currentMemory = Memory.LEFT;
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

    public MemoryInfo currentInfo() {
        switch (currentMemory) {
            case LEFT:
                return leftInfo;
            case RIGHT:
                return rightInfo;
        }

        throw new IndexOutOfBoundsException();
    }

    public VMPointer alloc(VMObject object) throws VMException {
        int pointer = currentInfo().getNext();
        memory[pointer] = object;
        VMPointer objectPointer = new VMPointer(pointer);
        object.setPointer(objectPointer);

        VMMachine.logger.severe("Allocating [" + pointer + "] " + object);

        return objectPointer;
    }

    public static VMPointer allocate(VMObject object) throws VMException {
        return getInstance().alloc(object);
    }

    public int memoryAvailable() {
        MemoryInfo current = currentInfo();
        return current.top - current.current;
    }
}
