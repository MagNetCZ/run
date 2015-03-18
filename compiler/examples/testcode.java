package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {

    public static void testTypeMismatch() {
        Integer a = 5;
        console(a);
        a = false;
        console(a);
    }

    public static void testNew() {
        Integer b = new Boolean(false);
        console(b);
    }

    public static void testArray() {
        boolean[] boolArray = new Boolean[10];
        // TODO accessing
    }

    public static void testCompare() {
        int i = 10;
        console(i == 10); // True
        console(i != 10); // False
        console(i != 5); // True
        console(i > 5); // True
        console(i > 10); // False
        console(i < 5); // False
        console(i < 10); // False
        console(i <= 5); // False
        console(i >= 5); // True
        console(i <= 10); // True
        console(i >= 10); // True
    }
}
