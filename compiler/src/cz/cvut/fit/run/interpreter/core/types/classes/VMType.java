package cz.cvut.fit.run.interpreter.core.types.classes;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public class VMType {
    public final static VMType INT = new VMType("Integer");
    public final static VMType STRING = new VMType("String");
    public final static VMType BOOLEAN = new VMType("Boolean");
    public final static VMType ID = new VMType("ID");
    public final static VMType CLASS = new VMType("Class");

    public final static VMType REFERENCE = new VMType("<<REFERENCE>>");
    public final static VMType VOID = new VMType("<<VOID>>");

//    VOID(""),
//    REFERENCE(""),
//    INT("int"),
//    CHAR("char"),
//    BYTE("byte"),
//    DOUBLE("double"),
//    LONG("long"),
//    STRING("string"),
//    BOOLEAN("boolean"),
//    ARRAY("array"),
//    ID(""),
//    CLASS("");

    private String name;

    // TODO nested type (for array)

    VMType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // TODO equals for type checking
}
