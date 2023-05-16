/** Parser Driver
 * @author Chris */

package Parser;
import java.util.*;
import static Parser.ParserClass.*;

public class ParserDriver {
    public static void main(String[] args) {
        // running single instances
        runParser("src/main/resources/primary_verification_files/99bottles.lex",
                "src/main/resources/parser_output/99bottles.par");
        runParser("src/main/resources/primary_verification_files/fizzbuzz.lex",
                "src/main/resources/parser_output/fizzbuzz.par");
        runParser("src/main/resources/primary_verification_files/prime.lex",
                "src/main/resources/parser_output/prime.par");


        // running multiple instances
        ArrayList<String> inputFilePaths = new ArrayList<>(Arrays.asList(
                "src/main/resources/example_input_files/count.lex",
                "src/main/resources/example_input_files/hello.lex",
                "src/main/resources/example_input_files/loop.lex"));
        ArrayList<String> outputFilePaths = new ArrayList<>(Arrays.asList(
                "src/main/resources/parser_output/count.par",
                "src/main/resources/parser_output/hello.par",
                "src/main/resources/parser_output/loop.par"));
        runParser(inputFilePaths, outputFilePaths);
    }
}

