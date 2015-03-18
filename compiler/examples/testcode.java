package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {

    // Curly bracket scope basic
    public static void testVariableScope() {
        if (true) {
            int a = 10;
            console(a);
        }

        console(a); // Exception, should not be found
    }

    // Curly bracket and for scope
    public static void testVariableScope2() {
        for (int i = 1; i < 10; i++) {
            console(i); // 1
            i = 15; // Should be ok
            console(i); // 15
        }

        i = 20; // Should not be found
    }

    // Scope redeclaration
    public static void testVariableScope3() {
        // TODO
    }

    public static void testFor() {
        for (int i = 1; i < 10; i++) {
            console(i);
        }
    }
}
