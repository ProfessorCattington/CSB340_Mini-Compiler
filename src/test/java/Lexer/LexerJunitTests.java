package Lexer;

import org.junit.jupiter.api.*;
import java.io.*;

import java.util.Scanner;

import static Lexer.LexerUtilities.outputToFile;

public class LexerJunitTests {

    static String verificationFilePath = "../main/resources/primary_verification_files/";
    static String inputFilePath = "../main/resources/primary_input/";
    static String outputFilePath = "../main/resources/lexer_output/";

    @Test
    @DisplayName("Verify Lexer class is constructed properly")
    void testLexerConstruction(){

        int expectedLine = 1;
        int expectedLinePos = 1;
        int expectedCharPos = 0;
        String expectedSource = "count = 1;";
        String expectedLexeme = "";

        Lexer lexer = new Lexer(expectedSource);

        Assertions.assertEquals(expectedLine, lexer.getLine());
        Assertions.assertEquals(expectedLinePos, lexer.getLinePos());
        Assertions.assertEquals(expectedCharPos, lexer.getCharPosition());
        Assertions.assertEquals(expectedSource, lexer.getSource());
        Assertions.assertEquals(expectedLexeme, lexer.getLexeme());
    }

    @Test
    @DisplayName("Verify the lexer produces an output file")
    void testLexerMakesExpectedOutput() throws FileNotFoundException {

        String outputFileName = "prime.lex";
        String inputFileName = "prime.c";

        File inputFile = new File(inputFilePath + inputFileName);
        Scanner scanner = new Scanner(inputFile);
        String source = " ";
        String result = " ";
        while (scanner.hasNext()) {
            source += scanner.nextLine() + "\n";
        }
        Lexer l = new Lexer(source);
        result = LexerUtilities.generateTokens(l);
        outputToFile(inputFileName, outputFilePath, result);

        File outputFile = new File(outputFilePath + outputFileName);
        Assertions.assertTrue(outputFile.exists());
    }

    @Test
    @DisplayName("Test Lexer's file output matches the verification file'")
    void testLexerOutputAgainstVerificationFile() throws FileNotFoundException {

        String verificationFileName = "prime.lex";
        String outputFileName = "prime.lex";
        String inputFileName = "prime.c";

        //use lexer to generate a .lex file
        File file = new File(inputFilePath + inputFileName);
        Scanner scanner = new Scanner(file);
        String source = " ";
        String result = " ";
        while (scanner.hasNext()) {
            source += scanner.nextLine() + "\n";
        }
        Lexer l = new Lexer(source);
        result = LexerUtilities.generateTokens(l);
        outputToFile(inputFileName, outputFilePath, result);

        //get the generated .lex file
        file = new File(outputFilePath + outputFileName);
        scanner = new Scanner(file);
        String generatedOutput = " ";
        while (scanner.hasNext()) {
            generatedOutput += scanner.nextLine() + "\n";
        }

        //get the .lex file provided by Eric on canvas
        file = new File(verificationFilePath + verificationFileName);
        scanner = new Scanner(file);
        String verificationOutput = " ";
        while (scanner.hasNext()) {
            verificationOutput += scanner.nextLine() + "\n";
        }

        //verify the contents are the same
        Assertions.assertEquals(generatedOutput.length(), verificationOutput.length());

        for (int i = 0; i < generatedOutput.length(); i++) {
            Assertions.assertEquals(generatedOutput.charAt(i), verificationOutput.charAt(i));
        }
    }

    @Test
    @DisplayName("Test bad file path on output throws exception")
    void testBadFilePath(){

        String badFilePath = "/bad_folder/";
        String inputFileName = "prime.c";

        String result = "bad";
        String finalResult = result;

        Assertions.assertThrows(RuntimeException.class, () -> outputToFile(inputFileName, badFilePath, finalResult));
    }
}
