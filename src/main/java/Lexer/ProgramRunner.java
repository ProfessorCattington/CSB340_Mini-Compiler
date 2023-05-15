package Lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProgramRunner {

    public static void main(String[] args) {

        String errorString = "a = 100;\nprint(num);\n ╔";
        String integerOperator = "1 - 1;";
        String stringLiteral = "\"Hello, World\"";
        String charLiteral = "a = 'a';";
        Lexer l = new Lexer(errorString);
        String result = l.printTokens();
        //stubCode();

    }

    //code that was given. putting it here so I can test stuff in isolation
    public static void stubCode() {

        if (1 == 1) {
            try {

                File f = new File("main\\resources\\99bottles.c");
                Scanner s = new Scanner(f);
                String source = " ";
                String result = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                Lexer.outputToFile(result);

            } catch (FileNotFoundException e) {
                Lexer.error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            Lexer.error(-1, -1, "No args");
        }
    }

}
