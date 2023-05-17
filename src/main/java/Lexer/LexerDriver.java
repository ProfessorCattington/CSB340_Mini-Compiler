/** Lexer Driver
 * @author Michael */

package Lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LexerDriver {

    static String outputFilePath = "src/main/resources/lexer_output/";

    public static void main(String[] args) {

        ArrayList<String> files = new ArrayList<String>();
        files.add("additional_input_files/nfactorial.c");
        files.add("additional_input_files/utopiantree.c");
        files.add("additional_input_files/grades.c");
        files.add("additional_input_files/sum.c");
        files.add("primary_input/99bottles.c");
        files.add("primary_input/fizzbuzz.c");
        files.add("primary_input/prime.c");

        lexicalAnalysis_multi(files);
    }

    //code that was given. putting it here so I can test stuff in isolation
    public static void lexicalAnalysis_single(String fileName) {

        if (1 == 1) {
            try {

                File f = new File("src/main/resources/" + fileName);
                Scanner s = new Scanner(f);
                String source = " ";
                String result = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = LexerUtilities.generateTokens(l);
                LexerUtilities.printTokens(result);

                LexerUtilities.outputToFile(result, outputFilePath, fileName);

            } catch (FileNotFoundException e) {
                LexerUtilities.error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            LexerUtilities.error(-1, -1, "No args");
        }
    }

    //method for generating files from primary and additional input folders
    public static void lexicalAnalysis_multi(ArrayList<String> fileNames) {

        for (int i = 0; i < fileNames.size(); i++) {

            try {
                String inputFilePath = "src/main/resources/" + fileNames.get(i);

                File f = new File(inputFilePath);

                Scanner s = new Scanner(f);
                String source = " ";
                String result = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = LexerUtilities.generateTokens(l);
                LexerUtilities.printTokens(result);
                LexerUtilities.outputToFile(fileNames.get(i), outputFilePath, result);

            } catch (FileNotFoundException e) {
                LexerUtilities.error(-1, -1, "Exception: " + e.getMessage());
            }
        }
    }
}
