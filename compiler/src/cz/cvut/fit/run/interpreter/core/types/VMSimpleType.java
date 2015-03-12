package cz.cvut.fit.run.interpreter.core.types;

import cz.cvut.fit.run.interpreter.core.VMObject;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMSimpleType<T> extends VMObject {
    protected T contents;

    public VMSimpleType(T contents) {
        this.contents = contents;
    }

    public T getContents() {
        return contents;
    }

    public void setContents(T contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return contents.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMSimpleType)) return false;

        VMSimpleType that = (VMSimpleType) o;

        if (contents != null ? !contents.equals(that.contents) : that.contents != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return contents != null ? contents.hashCode() : 0;
    }
}
