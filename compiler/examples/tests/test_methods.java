package cz.fit.cvut.cz.run.examples.tests;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {
    int instanceVar = 5;

    public static void staticMethod() {
        console("Static method");
    }

    public void instanceMethod(String arg) {
        console("Instance method");
        console(this.instanceVar);
        console(arg);
    }
}

public class Main {
    public static void main(String[] args) {
        Test.staticMethod();
        Test testInstance = new Test();
        String argument = "Hello";
        testInstance.instanceMethod(argument);
    }
}
