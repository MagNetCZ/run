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
        
        System.println(c);
    }

    public static void testIf() {
        boolean a = true;
        boolean b = false;

        if (a)
            System.println(1);

        if (b)
            System.println(1);
        else
            System.println(2);

        if (false)
            System.println(1);
        else if(true)
            System.println(2);
        else {
            System.println(3);
        }
    }

        public static void testSwitch() {
        boolean b = true;
        switch (b) {
            case true:
                System.println("true");
            case false:
                System.println("false");
                break;
            default:
                System.println("default");
        }
    }

    public static void testWhile() {
        boolean a = true;
        int i = 0;
        while (a) {
            System.println(i);
            i = i + 1;
            a = false;
        }

        do {
            System.println(2);
        } while (false);
    }

    

    public static void testIf() {
        for (int i = 0; true; i = i + 1) {
            System.println(i);
        }
    }

    

    public static void testBoolean() {
        boolean a = false;
        boolean b = true;
        System.println(!a); // True
        System.println(a == a); // True
        System.println(a == b); // False
        System.println(!a == a); // False
    }

    // Curly bracket scope basic
    public static void testVariableScope() {
        if (true) {
            int a = 10;
            System.println(a);
        }

        System.println(a); // Exception, should not be found
    }

    // Curly bracket and for scope
    public static void testVariableScope2() {
        for (int i = 1; i < 10; i++) {
            System.println(i); // 1
            i = 15; // Should be ok
            System.println(i); // 15
        }

        i = 20; // Should not be found
    }

    public static void testFor() {
        for (int i = 1; i < 10; i++) {
            System.println(i);
        }
    }

    public static void testTypeMismatch() {
        Integer a = 5;
        System.println(a);
        a = false;
        System.println(a);
    }

    public static void testArray() {
        boolean[] boolArray = new Boolean[10];
    }

    public static void testCompare() {
        int i = 10;
        System.println(i == 10); // True
        System.println(i != 10); // False
        System.println(i != 5); // True
        System.println(i > 5); // True
        System.println(i > 10); // False
        System.println(i < 5); // False
        System.println(i < 10); // False
        System.println(i <= 5); // False
        System.println(i >= 5); // True
        System.println(i <= 10); // True
        System.println(i >= 10); // True
    }

public static void testNew() {
        Boolean a = new Boolean(false);
        System.println(a);
        }

public static void testArray() {
        Boolean[] a = new Boolean[20];
        System.println(a[0]);
        a[0] = true;
        System.println(a[0]);
        System.println(a);
        }

public static void testIntegerArray() {
        int size = 10;
        Integer[] a = new Integer[size];

        for (int i = 0; i < size; i++) {
        a[i] = size - i;
        }

        for (int i = 0; i < size; i++) {
        System.println(a[i]);
        }
}