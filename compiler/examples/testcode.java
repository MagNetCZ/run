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
//        console(Test.staticVariable);
//        int a = 10;
//        a = null;
//        a = 10;
//        console(a);
//
//        Test.staticVariable = 5;
//        console(Test.staticVariable);
//
//        int[] array = new int[a];
//        array[5] = 1;
//        console(array[5]);

        // console(Test.staticVariable);

        Test testInstance = new Test();
//
        testInstance.instanceVariable = 5;
        console(testInstance.instanceVariable);

        Test testInstance2 = new Test();

        testInstance2.instanceVariable = 10;
        console(testInstance2.instanceVariable);

        testInstance.instanceVariable++;
        console(testInstance.instanceVariable);

        testInstance = testInstance2;
        console(testInstance.instanceVariable);

//        testInstance.staticVariable = 5; // Should throw error

        // TODO scopes
    }
}
