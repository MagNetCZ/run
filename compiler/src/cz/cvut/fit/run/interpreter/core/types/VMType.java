package cz.cvut.fit.run.interpreter.core.types;

/**
 * Created by MagNet on 12. 3. 2015.
 */
public enum VMType {
    REFERENCE(""),
    INT("int"),
    CHAR("char"),
    BYTE("byte"),
    DOUBLE("double"),
    LONG("long"),
    STRING("string"),
    ARRAY("array");

    private String typeName;

    VMType(String typeName) {
        this.typeName = typeName;
    }
}
