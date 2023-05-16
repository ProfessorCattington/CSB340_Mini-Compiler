package Parser;

import static Parser.ParserClass.runParser;

public class ParserDriver {
    public static void main(String[] args) {
        runParser("src/main/resources/hello.lex", "src/main/resources/hello_test2.par");
    }


}

