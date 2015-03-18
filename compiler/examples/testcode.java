package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {

    public static void testNew() {
        Integer a = new Integer(5);
        int a = false;
        Integer b = new Boolean(false);
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
