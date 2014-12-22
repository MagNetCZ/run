package cz.fit.cvut.cz.run.examples;

/**
 * Created by MagNet on 22. 12. 2014.
 */
public class Test {
    int number = 5;

    public Test(int number) {
        this.number = number;
    }

    public void increment() {
        number = number + getNumber();
    }

    public int getNumber() {
        return number;
    }
}
