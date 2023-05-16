package Parser;
import java.io.*;
import java.util.*;

public class ParserDriver {
    public static void main(String[] args) {
        if (1 == 1) {
            try {

                String input = "src/main/resources/loop.lex";
                String output = "src/main/resources/loop_test2.lex";

                List<ParserToken> list = ParserUtilities.inputFromFile(input);
                ParserClass p = new ParserClass(list);
                String result = "";
                StringBuilder sb = new StringBuilder();
                result = p.buildAST(p.parse(), sb);
                ParserUtilities.printAST(result);
                ParserUtilities.outputToFile(result, output);


            } catch (FileNotFoundException e) {
                ParserClass.error(-1, -1, "Exception: " + e.getMessage());
            } catch (Exception e) {
                ParserClass.error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            ParserClass.error(-1, -1, "No args");
        }
    }
}

