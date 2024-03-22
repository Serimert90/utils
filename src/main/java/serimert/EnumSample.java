package serimert;

enum EnumSample {

    TEST("TEST1", 3),
    TEST2("TEST2", 4);

    private final String test1;
    private final int test2;

    EnumSample() {
        this("default", -1);
    }

    EnumSample(String test1, int test2) {
        this.test1 = test1;
        this.test2 = test2;
    }

    public String getTest1() {
        return test1;
    }

    public int getTest2() {
        return test2;
    }
}
