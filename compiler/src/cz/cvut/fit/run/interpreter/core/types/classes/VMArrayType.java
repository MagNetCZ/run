package cz.cvut.fit.run.interpreter.core.types.classes;

/**
 * Created by MagNet on 18. 3. 2015.
 */
public class VMArrayType extends VMType {
    private VMType contentType;

    public VMArrayType(VMType contentType) {
        super("Array");
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMArrayType)) return false;
        if (!super.equals(o)) return false;

        VMArrayType that = (VMArrayType) o;

        if (!contentType.equals(that.contentType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + contentType.hashCode();
        return result;
    }

    public VMType getContentType() {
        return contentType;
    }
}
