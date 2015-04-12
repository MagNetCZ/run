package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {
    public static int staticVariable;
    public int instanceVariable;
    public Integer[] array;
}

public class Main {
    public static void main(String[] args) {
        Test testInstance = new Test();
        testInstance.array = new Integer[10];
        Integer[] array = testInstance.array;
        array[1] = 5;
        array = testInstance.array;
        System.println(array[1]);
    }
}
