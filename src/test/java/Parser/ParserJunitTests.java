package Parser;

import org.junit.jupiter.api.*;
import java.io.*;

public class ParserJunitTests {
    @Test
    @DisplayName("practice junit test")
    void simpleAssert() {
        Assertions.assertEquals(1, 1);
    }

    @Test
    @DisplayName("Compare Program Output with Given Test Output")
    void compareOutput() throws IOException {
        BufferedReader givenOutput = new BufferedReader(new FileReader("src/main/resources/count.par"));
        BufferedReader parserOutput = new BufferedReader(new FileReader("src/main/resources/loop.par"));
        String givenLine = givenOutput.readLine();
        String parserLine = parserOutput.readLine();
        int line = 1;
        while(givenLine != null || parserLine != null) {
            Assertions.assertFalse(givenLine == null || parserLine == null || !givenLine.equals(parserLine));
            givenLine = givenOutput.readLine();
            parserLine = parserOutput.readLine();
            line++;
        }
        givenOutput.close();
        parserOutput.close();
    }
}
