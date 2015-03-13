package cz.cvut.fit.run.interpreter.core.helpers;

import cz.cvut.fit.run.interpreter.core.VMBaseObject;
import cz.cvut.fit.run.interpreter.core.VMObject;
import cz.cvut.fit.run.interpreter.core.types.VMBoolean;
import cz.cvut.fit.run.interpreter.core.types.VMInteger;
import cz.cvut.fit.run.interpreter.core.types.VMString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MagNet on 13. 3. 2015.
 */
public class LiteralParser {
    private static final Pattern STRING_PATTERN = Pattern.compile("^\".*\"$");
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(false|true)$");

    public static VMObject parseLiteral(String literalString) {
        // String
        Matcher m = STRING_PATTERN.matcher(literalString);
        if (m.matches()) return new VMString(literalString);

        // Boolean
        m = BOOLEAN_PATTERN.matcher(literalString);
        if (m.matches()) return parseBoolean(literalString);

        // Integer
        return parseInt(literalString);

        // TODO  Ints    ^[-+]?\d+$
        // TODO  floats  ^[-+]?[0-9]*\.?[0-9]+$
    }

    public static VMBoolean  parseBoolean(String string) {
        switch (string) {
            case "false": return VMBoolean.FALSE;
            case "true": return VMBoolean.TRUE;
            default: throw new IllegalArgumentException();
        }
    }

    public static VMInteger parseInt(String string) {
        return new VMInteger(Integer.parseInt(string));
    }
}
