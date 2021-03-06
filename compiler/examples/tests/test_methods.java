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
        return;
    }

    public String returnMethod() {
        return "What what";
    }
}

public class Main {
    public static void main(String[] args) {
//        Test a = new Test();
        Test.staticMethod();
        Test testInstance = new Test();
        String argument = testInstance.returnMethod();
//        String argument = testInstance.returnMethod();
        testInstance.instanceMethod(argument);
    }
}
