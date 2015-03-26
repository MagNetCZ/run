package cz.cvut.fit.run.interpreter.core.types.type;

import cz.cvut.fit.run.interpreter.core.types.classes.VMClass;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMType {
    public final static VMType INT = new VMType("Integer");
    public final static VMType STRING = new VMType("String");
    public final static VMType BOOLEAN = new VMType("Boolean");
    public final static VMType ID = new VMType("ID");
    public final static VMType CLASS = new VMType("Class");

    public final static VMType FILE = new VMType("File");
    public final static VMType SYSTEM = new VMType("System");

    public final static VMType REFERENCE = new VMType("<<REFERENCE>>");
    public final static VMType VOID = new VMType("<<VOID>>");
    public final static VMType NULL = new VMType("<<NULL>>");

    private String name;

    public VMType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "VMType{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VMType)) return false;

        VMType vmType = (VMType) o;

        if (!name.equals(vmType.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
