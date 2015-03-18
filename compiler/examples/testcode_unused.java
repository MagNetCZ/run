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

    public static void testIf() {
        boolean a = true;
        boolean b = false;

        if (a)
            console(1);

        if (b)
            console(1);
        else
            console(2);

        if (false)
            console(1);
        else if(true)
            console(2);
        else {
            console(3);
        }
    }

        public static void testSwitch() {
        boolean b = true;
        switch (b) {
            case true:
                console("true");
            case false:
                console("false");
                break;
            default:
                console("default");
        }
    }

    public static void testWhile() {
        boolean a = true;
        int i = 0;
        while (a) {
            console(i);
            i = i + 1;
            a = false;
        }

        do {
            console(2);
        } while (false);
    }

    

    public static void testIf() {
        for (int i = 0; true; i = i + 1) {
            console(i);
        }
    }

    

    public static void testBoolean() {
        boolean a = false;
        boolean b = true;
        console(!a); // True
        console(a == a); // True
        console(a == b); // False
        console(!a == a); // False
    }

    // Curly bracket scope basic
    public static void testVariableScope() {
        if (true) {
            int a = 10;
            console(a);
        }

        console(a); // Exception, should not be found
    }

    // Curly bracket and for scope
    public static void testVariableScope2() {
        for (int i = 1; i < 10; i++) {
            console(i); // 1
            i = 15; // Should be ok
            console(i); // 15
        }

        i = 20; // Should not be found
    }

    // Scope redeclaration
    public static void testVariableScope3() {
        // TODO
    }

    public static void testFor() {
        for (int i = 1; i < 10; i++) {
            console(i);
        }
    }

    