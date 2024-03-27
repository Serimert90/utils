package serimert;

public class CoverOptions {

    private boolean coverAll = true;
    private boolean coverGetterAndSetters;
    private boolean coverSuperClasses;
    private boolean coverInterfaceDefaultMethods;
    private boolean coverToStringEqualsHashcode;

    private CoverOptions() { /* Empty constructor */ }

    public boolean isCoverAll() {
        return coverAll;
    }

    public boolean isCoverGetterAndSetters() {
        return coverAll || coverGetterAndSetters;
    }

    public boolean isCoverSuperClasses() {
        return coverAll || coverSuperClasses;
    }

    public boolean isCoverToStringEqualsHashcode() {
        return coverAll || coverToStringEqualsHashcode;
    }

    public boolean isCoverInterfaceDefaultMethods() {
        return coverAll || coverInterfaceDefaultMethods;
    }

    public static class Builder {

        private final CoverOptions coverOptions = new CoverOptions();

        public Builder coverAll(boolean coverAll) {
            coverOptions.coverAll = coverAll;
            return this;
        }

        public Builder coverGetterAndSetters(boolean coverGetterAndSetters) {
            coverOptions.coverGetterAndSetters = coverGetterAndSetters;
            return this;
        }

        public Builder coverSuperClasses(boolean coverSuperClasses) {
            coverOptions.coverSuperClasses = coverSuperClasses;
            return this;
        }

        public Builder coverInterfaceDefaultMethods(boolean coverInterfaceDefaultMethods) {
            coverOptions.coverInterfaceDefaultMethods = coverInterfaceDefaultMethods;
            return this;
        }

        public Builder coverToStringEqualsHashcode(boolean coverToStringEqualsHashcode) {
            coverOptions.coverToStringEqualsHashcode = coverToStringEqualsHashcode;
            return this;
        }

        public CoverOptions all() {
            coverOptions.coverAll = true;
            return coverOptions;
        }

        public CoverOptions build() {
            return coverOptions;
        }
    }
}
