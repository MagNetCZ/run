package cz.fit.cvut.cz.run.examples.tests;

/**
 * Created by MagNet on 26. 3. 2014.
 */
class A {
    public void foo() { System.println("foo"); }
}

class B extends A  {
    public void foo(String hello) { System.println("baz"); }
}

class C extends A  {
    public static void foo(A a) { System.println("baz"); }
}

public class Main {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        C c = new C();
    }
}
