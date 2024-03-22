package serimert;

abstract class ASample3 {

    private final String test;
    private final int test2;
    ASample3(String test, int test2) {
        this.test = test;
        this.test2 = test2;
    }

    public String getTest() {
        return test;
    }

    public int getTest2() {
        return test2;
    }
}
