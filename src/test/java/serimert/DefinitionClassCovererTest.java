package serimert;

import org.junit.jupiter.api.Test;

import static serimert.DefinitionClassCoverer.coverClasses;

public class DefinitionClassCovererTest {

    @Test
    public void test() {
        coverClasses("serimert.Sample1");
        coverClasses("serimert.Sample2");
        coverClasses("serimert.Sample3");
        coverClasses("serimert.EnumSample");
    }
}
