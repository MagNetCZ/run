package cz.fit.cvut.cz.run.examples.tests;

public class CustomException extends Exception {
}

public class OtherException extends Exception {
}

public class Main {
    public static void main(String[] args) {
        try {
            Main.nonExceptionMethod();
            System.println("Method ran without any exceptions");
        } catch (Exception ex) {
            System.println("Method finished with the following exception");
            System.println(ex);
        }

        try {
            Main.exceptionMethod();
            System.println("Method ran without any exceptions");
        } catch (Exception ex) {
            System.println("Method finished with the following exception");
            System.println(ex);
        }
    }

    public static void nonExceptionMethod() {
        System.println("I am a cool method that doesn't throw anything");
    }

    public static void exceptionMethod() {
        try {
            try {
                System.println("trying");
                throw new OtherException();
            } catch (CustomException ex) {
                System.println("not gonna catch");
            } finally {
                System.println("finally");
            }

            System.println("Should not be evaluated");
        } catch (Exception ex) {
            System.println("should catch");
            System.println(ex);
            throw ex;
        }
    }
}
