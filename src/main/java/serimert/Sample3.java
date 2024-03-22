package serimert;

class Sample3 extends ASample3 {

    private long test3;
    private Sample1 sample1;

    public Sample3() {
        super("a",1);
    }

    public Sample3(String test, int test2) {
        super("test",test2);
    }

    public Sample3(String test, int test2, long test3, Sample1 sample1) {
        super("test",test2);
        this.test3 = test3;
        this.sample1 = sample1;
    }

    public long getTest3() {
        return test3;
    }

    public void setTest3(long test3) {
        this.test3 = test3;
    }

    public Sample1 getSample1() {
        return sample1;
    }

    public void setSample1(Sample1 sample1) {
        this.sample1 = sample1;
    }
}
