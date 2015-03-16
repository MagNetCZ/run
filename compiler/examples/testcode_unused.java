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


    public static void testMethod() {
        boolean a = false;
        boolean b = true;
        console(!a);
        console(a == a);
        console(a == b);
        console(!a == a);
        //a = !a;
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

    public static void testWhile() {
        boolean a = true;
        while (a) {
            console(1);
            a = false;
        }

        do {
            console(2);
        } while (false);
    }

    public static void testFor() {
        for (int i = 0; true; i = i + 1) {
            console(i);
        }
    }