package serimert;

class Sample2 implements ISample2 {

    private String id;
    private int value;
    private EnumSample enumSample;

    public Sample2(String id, int value, EnumSample enumSample) {
        this.id = id;
        this.value = value;
        this.enumSample = enumSample;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public EnumSample getEnumSample() {
        return enumSample;
    }

    @Override
    public void setEnumSample(EnumSample enumSample) {
        this.enumSample = enumSample;
    }
}
