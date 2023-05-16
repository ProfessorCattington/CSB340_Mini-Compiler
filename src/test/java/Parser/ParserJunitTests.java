/** Parser Tests
 * @author Chris */

package Parser;
import org.junit.jupiter.api.*;
import java.io.*;
import static Parser.ParserUtilities.inputFromFile;
import static Parser.ParserUtilities.outputToFile;

public class ParserJunitTests {
    @Test
    @DisplayName("practice junit test")
    void simpleAssert() {
        Assertions.assertEquals(1, 1);
    }

    @Test
    @DisplayName("Compare parser output - 99bottles")
    void compareOutput1() throws IOException {
        BufferedReader givenOutput = new BufferedReader(new FileReader(
                "src/main/resources/primary_verification_files/99bottles.par"));
        BufferedReader parserOutput = new BufferedReader(new FileReader(
                "src/main/resources/parser_output/99bottles.par"));
        String givenLine = givenOutput.readLine();
        String parserLine = parserOutput.readLine();
        while(givenLine != null || parserLine != null) {
            Assertions.assertEquals(givenLine, parserLine);
            givenLine = givenOutput.readLine();
            parserLine = parserOutput.readLine();
        }
        givenOutput.close();
        parserOutput.close();
    }

    @Test
    @DisplayName("Compare parser output - fizzbuzz")
    void compareOutput2() throws IOException {
        BufferedReader givenOutput = new BufferedReader(new FileReader(
                "src/main/resources/primary_verification_files/fizzbuzz.par"));
        BufferedReader parserOutput = new BufferedReader(new FileReader(
                "src/main/resources/parser_output/fizzbuzz.par"));
        String givenLine = givenOutput.readLine();
        String parserLine = parserOutput.readLine();
        while(givenLine != null || parserLine != null) {
            Assertions.assertEquals(givenLine, parserLine);
            givenLine = givenOutput.readLine();
            parserLine = parserOutput.readLine();
        }
        givenOutput.close();
        parserOutput.close();
    }

    @Test
    @DisplayName("Compare parser output - prime")
    void compareOutput3() throws IOException {
        BufferedReader givenOutput = new BufferedReader(new FileReader(
                "src/main/resources/primary_verification_files/prime.par"));
        BufferedReader parserOutput = new BufferedReader(new FileReader(
                "src/main/resources/parser_output/prime.par"));
        String givenLine = givenOutput.readLine();
        String parserLine = parserOutput.readLine();
        while(givenLine != null || parserLine != null) {
            Assertions.assertEquals(givenLine, parserLine);
            givenLine = givenOutput.readLine();
            parserLine = parserOutput.readLine();
        }
        givenOutput.close();
        parserOutput.close();
    }

    @Test
    @DisplayName("Test Input Method Throws FileNotFoundException - Bad Input")
    void badInputFileThrowsException() {
        Assertions.assertThrows(FileNotFoundException.class, ()-> inputFromFile("NotAFile.lex"));
    }

    @Test
    @DisplayName("Test Output method Throws RuntimeException - Bad Filename")
    void badOutputFileThrowsException() {
        Assertions.assertThrows(RuntimeException.class, ()-> outputToFile("",""));
    }



}
