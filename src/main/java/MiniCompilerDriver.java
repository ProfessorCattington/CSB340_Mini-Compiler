/** Mini compiler Driver that inputs c files into the lexer, then inputs the created lex files
 * into the parser and creates the par files */

import java.util.*;
import static Lexer.LexerDriver.lexicalAnalysis_multi;
import static Parser.ParserClass.runParserDefault;

public class MiniCompilerDriver {
    public static void main(String[] args) {
        // c input files
        ArrayList<String> lexer = new ArrayList<>();
        lexer.add("additional_input_files/nfactorial.c");
        lexer.add("additional_input_files/utopiantree.c");
        lexer.add("additional_input_files/grades.c");
        lexer.add("additional_input_files/sum.c");
        lexer.add("primary_input/99bottles.c");
        lexer.add("primary_input/fizzbuzz.c");
        lexer.add("primary_input/prime.c");
        ArrayList<String> parser = createParserList(lexer);

        // Run files through Lexer  -->  lex files created in lexer_output folder
        lexicalAnalysis_multi(lexer);
        // Run files through Parser  -->  par files created in parser_output folder
        runParserDefault(parser);
    }

    public static ArrayList<String> createParserList(ArrayList<String> lexerList) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : lexerList) {
            String[] s1 = s.split("/");
            String[] s2 = s1[1].split("\\.");
            list.add(s2[0]);
        }
        return list;
    }
}
