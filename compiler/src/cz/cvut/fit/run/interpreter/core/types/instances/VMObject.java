package cz.cvut.fit.run.interpreter.core.types.instances;

import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.exceptions.VMException;
import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.type.VMType;
import cz.cvut.fit.run.interpreter.traversion.FieldInitializeVisitorBuilder;
import cz.cvut.fit.run.interpreter.traversion.ModifierFilter;

import java.util.List;

/**
 * Created by MagNet on 9. 3. 2015.
 */
public class VMObject extends VMBaseObject {
    VMClass clazz;

    @Override
    public VMType getType() {
        return getClazz().getType();
    }

    public VMObject(VMClass clazz, VMObject ... args) throws VMException {
        this.clazz = clazz;

        initialize();
    }

    public void callMethod(String name, VMObject ... args) throws VMException {
        getClazz().callMethod(name, this, args);
    }

    public VMClass getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMObject)) return false;

        VMObject vmObject = (VMObject) o;

        if (!clazz.equals(vmObject.clazz)) return false;

        return true;

        // TODO equals should use superclass (fields)
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    @Override
    public String toString() {
        return "<" + getType().getName() + ">";
    }

    public void initialize() throws VMException {
        if (getClazz().getSource() == null)
            return;

        FieldInitializeVisitorBuilder builder =
                new FieldInitializeVisitorBuilder(this, new ModifierFilter("static", true));

        List<VMClass> classList = getClazz().getClassHierarchy();
        // Initialize fields from the top of the class hierarchy (most super)
        for (VMClass clazz : classList) {
            VMException ex = builder.visit(clazz.getSource());

            if (ex != null)
                throw ex;
        }
    }

    public boolean canBeAssignedTo(VMType type) {
        return clazz.canBeAssignedTo(type);
    }
}
