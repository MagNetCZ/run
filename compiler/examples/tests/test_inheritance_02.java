package cz.fit.cvut.cz.run.examples.tests;

/**
 * Created by MagNet on 26. 3. 2014.
 */
class A {
    public void foo() { System.println("foo"); }
}

class B extends A  {
    public void foo() { System.println("baz"); }
}

public class Main {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
    }
}
