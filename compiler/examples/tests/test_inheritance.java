package cz.fit.cvut.cz.run.examples.tests;

/**
 * Created by MagNet on 26. 3. 2014.
 */
class A {
    String aVariable = "Hi, I'm from A";

    public void foo() { System.println("foo"); }
    public void bar() { System.println("bar"); }
}

class B extends A  {
    public void foo() { System.println("baz"); }
    public void printA() { System.println(this.aVariable); }
}

public class Main {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();

        a.foo();
        a.bar();

        b.foo();
        b.bar();
        b.printA();

        a = b;
        a.foo();
    }
}
