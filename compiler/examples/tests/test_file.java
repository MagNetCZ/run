package cz.fit.cvut.cz.run.examples.tests;

public class Main {
    public static void main(String[] args) {
        String pathToFile = "examples/testcode.java";
        File file = new File(pathToFile);
        System.println("Reading first line from " + pathToFile);
        String line = file.readLine();
        System.println(line);
    }
}
