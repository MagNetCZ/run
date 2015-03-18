package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {

    public static void testSwitch() {
        boolean b = true;
        switch (b) {
            case true:
                console("true");
            case false:
                console("false");
                break;
            default:
                console("default");
        }
    }

    public static void testWhile() {
        boolean a = true;
        int i = 0;
        while (a) {
            console(i);
            i = i + 1;
            a = false;
        }

        do {
            console(2);
        } while (false);
    }

    public static void testFor() {
        for (int i = 1; i < 10; i++) {
            console(i);
        }
    }

    public static void testIf() {
        for (int i = 0; true; i = i + 1) {
            console(i);
        }
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

    public static void testBoolean() {
        boolean a = false;
        boolean b = true;
        console(!a); // True
        console(a == a); // True
        console(a == b); // False
        console(!a == a); // False
    }

}
