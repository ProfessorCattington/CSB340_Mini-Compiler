package Parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParserJunitTests {
    @Test
    @DisplayName("practice junit test")
    void simpleAssert() {
        Assertions.assertEquals(1, 1);
    }

}
