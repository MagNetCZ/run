package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.IDType;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;

/**
 * Created by MagNet on 16. 3. 2015.
 */
public class VMIdentifierInstance extends VMBuiltinInstance<String> {
    private Integer arrayIndex = null;
    private VMIdentifierInstance field = null;

    public VMIdentifierInstance(VMClass clazz, String value) throws VMException {
        super(clazz, value);
    }

    public Integer getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(Integer arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public boolean isArrayIndex() {
        return arrayIndex != null;
    }

    public VMIdentifierInstance getField() {
        return field;
    }

    public void setField(VMIdentifierInstance field) {
        this.field = field;
    }

    public boolean isFieldIdentifier() {
        return field != null;
    }

    public IDType getIDType() {
        if (isArrayIndex())
            return IDType.ARRAY_ACCESS;

        if (isFieldIdentifier())
            return IDType.FIELD_ACCESS;

        return IDType.LOCAL_VARIABLE;
    }

    @Override
    public String toString() {
        return "<ID: " +
                value +
                ", ai=" + arrayIndex +
                ", f=" + field +
                '>';
    }
}
