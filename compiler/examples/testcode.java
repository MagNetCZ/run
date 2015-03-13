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

    public static int testNumbers() {
        int a = 10;
        int b = 15;
        int c = b + b * a;
        
        console(c);
    }

    public static int testIf() {
        int a = 0;
        if (a == 0)
            console(1);
    }

    public static void testMethod() {
        boolean a = false;
        boolean b = true;
        console(!a);
        console(a == a);
        console(a == b);
        console(!a == a);
        //a = !a;
    }
}
