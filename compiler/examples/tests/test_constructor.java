package cz.fit.cvut.cz.run.examples.tests;

/**
 * Created by MagNet on 26. 3. 2014.
 */
public class Test {
    int instanceVar;

    public Test(int instanceVar) {
        this.instanceVar = instanceVar;
    }
}

public class Main {
    public static void main(String[] args) {
        Test a = new Test(5);
        console(a.instanceVar);
    }
}
