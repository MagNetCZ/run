package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {

    public static int staticVariable;
    public int instanceVariable;

//    public static int staticInitializedVariable = 0;
//    public int instanceInitializedVariable = 10;

}

public class Main {
    public static void main(String[] args) {
        console(Test.staticVariable);
        Test.staticVariable = 5;
        // console(Test.staticVariable);

        // Test testInstance = new Test();

        // testInstance.instanceVariable = 5;
        // testInstance.staticVariable = 5; // Should throw error

        // TODO scopes
    }
}
