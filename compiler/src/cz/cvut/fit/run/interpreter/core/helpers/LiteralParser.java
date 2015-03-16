package cz.cvut.fit.run.interpreter.core.helpers;

import cz.cvut.fit.run.interpreter.context.VMMachine;
import cz.cvut.fit.run.interpreter.core.types.instances.VMBooleanInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMIntegerInstance;
import cz.cvut.fit.run.interpreter.core.types.instances.VMObject;
import cz.cvut.fit.run.interpreter.core.types.classes.VMBoolean;
import cz.cvut.fit.run.interpreter.core.types.classes.VMInteger;
import cz.cvut.fit.run.interpreter.core.types.classes.VMString;
import cz.cvut.fit.run.interpreter.core.types.instances.VMStringInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MagNet on 13. 3. 2015.
 */
public class LiteralParser {
    private static final Pattern STRING_PATTERN = Pattern.compile("^\".*\"$"); // TODO '
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(false|true)$");

    public static VMObject parseLiteral(String literalString) {
        // String
        Matcher m = STRING_PATTERN.matcher(literalString);
        if (m.matches()) return parseString(literalString);

        // Boolean
        m = BOOLEAN_PATTERN.matcher(literalString);
        if (m.matches()) return parseBoolean(literalString);

        // Integer
        return parseInt(literalString);

        // TODO  Ints    ^[-+]?\d+$
        // TODO  floats  ^[-+]?[0-9]*\.?[0-9]+$
    }

    public static VMBooleanInstance parseBoolean(String string) {
        switch (string) {
            case "false": return VMBoolean.FALSE;
            case "true": return VMBoolean.TRUE;
            default: throw new IllegalArgumentException();
        }
    }

    public static VMStringInstance parseString(String string) {
        String strippedString = string.substring(1, string.length() - 2); // TODO \ sequences
        return ((VMString)VMMachine.getInstance().getClazz("String")).createInstance(strippedString);
    }

    public static VMIntegerInstance parseInt(String string) {
        int intValue = Integer.parseInt(string);
        return ((VMInteger)VMMachine.getInstance().getClazz("Integer")).createInstance(intValue);
    }
}
