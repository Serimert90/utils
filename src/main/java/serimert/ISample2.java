package serimert;

interface ISample2 {

    String getId();
    void setId(String id);

    int getValue();
    void setValue(int value);

    EnumSample getEnumSample();
    void setEnumSample(EnumSample enumSample);

    default String getIdConcatValue() {
        return getId() + getValue();
    }
}
