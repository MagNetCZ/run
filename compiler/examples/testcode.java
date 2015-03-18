package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {

    public static void testNew() {
        Boolean a = new Boolean(false);
        console(a);
    }

    public static void testArray() {
        Boolean[] a = new Boolean[20];
        a[0] = 1;
        console(a[0]);
    }

}
