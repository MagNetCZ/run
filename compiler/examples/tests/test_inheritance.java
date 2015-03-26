package cz.fit.cvut.cz.run.examples.tests;

/**
 * Created by MagNet on 26. 3. 2014.
 */
class A {
    public void foo() { console("foo"); }
    public void bar() { console("bar"); }
}

class B extends A  {
    public void foo() { console("baz"); }
}

public class Main {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();

        a.foo();
        a.bar();

        b.foo();
        b.bar();

        a = b;
        a.foo();
    }
}
