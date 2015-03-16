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

}
