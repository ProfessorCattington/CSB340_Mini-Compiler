package Lexer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Simple helper library for lexer class. This cleans up the lexer class just a bit
public class LexerUtilities {

    private static final String errorMsg = "Invalid token: %s, found at line %d, linePos %d\n";

    //helper method for setting up the map with all the token types in it
    public static Map<String, LexerToken.TokenType> buildKeywordsMap() {

        Map<String, LexerToken.TokenType> keywords = new HashMap<String, LexerToken.TokenType>();

        keywords.put("*", LexerToken.TokenType.Op_multiply);
        keywords.put("/", LexerToken.TokenType.Op_divide);
        keywords.put("%", LexerToken.TokenType.Op_mod);
        keywords.put("+", LexerToken.TokenType.Op_add);
        keywords.put("-", LexerToken.TokenType.Op_subtract);
        keywords.put("<", LexerToken.TokenType.Op_less);
        keywords.put("<=", LexerToken.TokenType.Op_lessequal);
        keywords.put(">", LexerToken.TokenType.Op_greater);
        keywords.put(">=", LexerToken.TokenType.Op_greaterequal);
        keywords.put("==", LexerToken.TokenType.Op_equal);
        keywords.put("!=", LexerToken.TokenType.Op_notequal);
        keywords.put("!", LexerToken.TokenType.Op_not);
        keywords.put("=", LexerToken.TokenType.Op_assign);
        keywords.put("&&", LexerToken.TokenType.Op_and);
        keywords.put("||", LexerToken.TokenType.Op_or);

        keywords.put("(", LexerToken.TokenType.LeftParen);
        keywords.put(")", LexerToken.TokenType.RightParen);
        keywords.put("{", LexerToken.TokenType.LeftBrace);
        keywords.put("}", LexerToken.TokenType.RightBrace);
        keywords.put(";", LexerToken.TokenType.Semicolon);
        keywords.put(",", LexerToken.TokenType.Comma);

        keywords.put("if", LexerToken.TokenType.Keyword_if);
        keywords.put("else", LexerToken.TokenType.Keyword_else);
        keywords.put("print", LexerToken.TokenType.Keyword_print);
        keywords.put("putc", LexerToken.TokenType.Keyword_putc);
        keywords.put("while", LexerToken.TokenType.Keyword_while);

        return keywords;
    }

    /*
    another helper method for setting up the categories map.
    I'm using a map because it seemed faster and easier than creating a bunch of if statements
    or switch cases for the different categories.
     */
    //
    public static Map<String, Lexer.tokenCategory> buildCategoryMap() {
        Map<String, Lexer.tokenCategory> categories = new HashMap<String, Lexer.tokenCategory>();

        categories.put("*", Lexer.tokenCategory.operator);
        categories.put("/", Lexer.tokenCategory.operator);
        categories.put("%", Lexer.tokenCategory.operator);
        categories.put("+", Lexer.tokenCategory.operator);
        categories.put("-", Lexer.tokenCategory.operator);
        categories.put("<", Lexer.tokenCategory.operator);
        categories.put("<=", Lexer.tokenCategory.operator);
        categories.put(">", Lexer.tokenCategory.operator);
        categories.put(">=", Lexer.tokenCategory.operator);
        categories.put("==", Lexer.tokenCategory.operator);
        categories.put("!=", Lexer.tokenCategory.operator);
        categories.put("!", Lexer.tokenCategory.operator);
        categories.put("=", Lexer.tokenCategory.operator);
        categories.put("&&", Lexer.tokenCategory.operator);
        categories.put("||", Lexer.tokenCategory.operator);

        categories.put("(", Lexer.tokenCategory.symbol);
        categories.put(")", Lexer.tokenCategory.symbol);
        categories.put("{", Lexer.tokenCategory.symbol);
        categories.put("}", Lexer.tokenCategory.symbol);
        categories.put(";", Lexer.tokenCategory.symbol);
        categories.put(",", Lexer.tokenCategory.symbol);

        categories.put("if", Lexer.tokenCategory.keyword);
        categories.put("else", Lexer.tokenCategory.keyword);
        categories.put("while", Lexer.tokenCategory.keyword);
        categories.put("print", Lexer.tokenCategory.keyword);
        categories.put("putc", Lexer.tokenCategory.keyword);

        categories.put("\"", Lexer.tokenCategory.string);
        categories.put("\'", Lexer.tokenCategory.character);

        categories.put("\n", Lexer.tokenCategory.character);
        categories.put("\u0000", Lexer.tokenCategory.eof);

        return categories;
    }

    /*
        method for generating tokens and

     */
    public static String generateTokens(Lexer lexer) {

        LexerToken token;
        StringBuilder sb = new StringBuilder();

        while ((token = lexer.getToken()).tokenType != LexerToken.TokenType.End_of_input) {
            if (token.tokenType != LexerToken.TokenType.Blank) { //skip the comments section
                sb.append(token);
                sb.append("\n");
            }
        }
        sb.append(token);
        return sb.toString();
    }

    public static void printTokens(String input){

        String[] tokens = input.split("\n");
        for(int i = 0; i < tokens.length; i++){
            System.out.println(tokens[i]);
        }
    }

    public static void outputToFile(String filename, String outputFilePath, String result) {

        //clean off the input file name's path and extension
        String[] pathParts = filename.split("/");
        String newFileName = pathParts[pathParts.length-1];
        newFileName = newFileName.split("\\.")[0];

        newFileName += ".lex";

        try {
            FileWriter myWriter = new FileWriter(outputFilePath + newFileName);
            myWriter.write(result);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf(errorMsg, msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }
}
