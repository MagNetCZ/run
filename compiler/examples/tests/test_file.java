package cz.fit.cvut.cz.run.examples.tests;

public class Main {
    public static void main(String[] args) {
        File file = new File("examples/testcode.java");
        String line = file.readLine();
        System.println(line);
    }
}
