package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {
    int number = 3, sec, third = 5;

    public Test(int number_a) {
        this.number = number;
    }

    public void increment() {
        this.number = this.number + getNumber(15);
    }

    public int getNumber() {
        return this.number;
    }

    public static int testMethod() {
        int a = 10;
        int b = 15;
        int c = b + b + a;
        return c;
    }
}
