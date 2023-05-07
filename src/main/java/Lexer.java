import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private int line;
    private int linePos;
    private int charPosition;
    private char nextChar;
    private String source;
    private String lexeme;

    Map<String, LexerToken.TokenType> keywords;
    Map<String, tokenCategory> categories;

    enum tokenCategory {
        operator,
        negativeOp,
        symbol,
        keyword,
        identifier,
        integer,
        string,
        character,
        comment,
        error,
        eof,
    }

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("Invalid token: %s, found at line %d, linePos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    Lexer(String input) {

        this.line = 1;
        this.linePos = 1;
        this.charPosition = 0;
        this.source = input;
        this.nextChar = this.source.charAt(charPosition);
        this.lexeme = "";

        buildKeywordsMap();
        buildCategoryMap();
    }

    LexerToken getToken() {

        //reset the lexeme field so we can start over
        this.linePos += lexeme.length();
        this.lexeme = "";

        //handle new line character. has to be done separately because it also counts as whitespace apparently
        if (this.nextChar == '\n') {
            line++;
            this.linePos = 1;
            getNextChar();
        }

        //get to the next lexeme and increment our positioning value for output
        while (Character.isWhitespace(this.nextChar) && this.nextChar != '\n') {
            this.linePos++;
            getNextChar();
        }

        //analyze the character to see what category it could b and build the lexeme
        tokenCategory tokenCategory = categorizeLexeme();

        //create a token from the category
        switch (tokenCategory) {

            case eof:
                return new LexerToken(LexerToken.TokenType.End_of_input, "", this.line, this.linePos);

            case character:
                return char_lit();

            case string:
                return string_lit();

            case operator:
                return ops_keywords();

            case symbol:
                return symbols();

            case keyword:
                return ops_keywords();

            case comment:
                return new LexerToken(LexerToken.TokenType.Blank, "", 0, 0);
            case error:
                error(this.line, this.linePos, this.lexeme);
            default:
                return identifier_or_integer(tokenCategory);
        }
    }

    //method for looking at the next non whitespace or new line character and finding its category
    tokenCategory categorizeLexeme() {

        //check to see if this is the end of the file so we can end early
        if (atEndOfFile()) {
            this.linePos = this.charPosition;
            return categories.get("\u0000");
        }

        //put the next character into the lexeme
        addNextChar();

        //see if the character has a matching category in the map. if not then it might be an identifier or literal
        if (!categories.containsKey(lexeme)) { //possibly an identifier or string literal

            buildLexeme();

            //check once more to see if it's actually keyword/reserved word
            if (categories.containsKey(lexeme)) {
                return categories.get(lexeme);
            }

            //check the lexeme against regex to see if it's string
            Pattern pattern = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
            Matcher matcher = pattern.matcher(lexeme);
            if (this.lexeme.length() > 1 && matcher.matches()) {
                return tokenCategory.identifier; // lexeme is an identifier
            }

            //check again to see if it's an integer
            pattern = Pattern.compile("[0-9]+");
            matcher = pattern.matcher(lexeme);
            if (matcher.matches()) {
                return tokenCategory.integer; // lexeme is an integer
            }

            //finally check to see if it's a single character
            pattern = Pattern.compile("'([^'\\n]|\\\\n|\\\\\\\\)'");
            matcher = pattern.matcher(lexeme);
            if (matcher.matches()) {
                return tokenCategory.character; // lexeme is a character
            }

        } else { // the lexeme was not probably not an identifier

            //check to make sure it wasn't the comment block
            if (div_or_comment()) {
                return tokenCategory.comment;
            }
            //check to see if it's a string literal
            if (isStringLiteral()) {
                return tokenCategory.string;
            }

            return categories.get(this.lexeme); // lexeme was whatever we found in the map
        }

        return tokenCategory.error;
    }

    //method for adding characters from string into lexeme
    void buildLexeme() {

        if (atEndOfFile()) {
            lexeme = "\u0000";
            this.linePos = this.charPosition;
            return;
        }
        while (!nextIsWhiteSpace() && !nextIsASymbol()) {

            getNextChar();
            addNextChar();

            if (atEndOfFile()) { //check to see if we are about to hit the end of the file.
                break;
            }
        }
    }

    //method for finding the end of a comment or string
    void findStringCommentEnd(char endChar, boolean tallyNewLine) {

        while (this.nextChar != endChar) {

            if (tallyNewLine && this.nextChar == '\n') {
                this.line++;
            }

            addNextChar();
            getNextChar();
        }

        getNextChar();
    }

    //look ahead 1 character and see if it's whitespace
    boolean nextIsWhiteSpace() {
        return Character.isWhitespace(this.source.charAt(this.charPosition + 1));
    }

    //look 1 character ahead for parenthesis and stuff
    boolean nextIsASymbol() {

        String nextCharacter = "" + this.source.charAt(this.charPosition + 1);
        boolean isSymbol = this.keywords.containsKey(nextCharacter);

        return isSymbol;
    }

    //method for setting next character from the source string
    void getNextChar() {
        if (!atEndOfFile()) {
            this.charPosition++;
            this.nextChar = this.source.charAt(this.charPosition);
        }
    }

    //method for adding next character to the lexeme field
    void addNextChar() {
        this.lexeme += this.nextChar;
    }

    //method for checking if the next position will be outside the bounds of the source string length
    boolean atEndOfFile() {

        return (this.charPosition + 1) >= this.source.length();
    }

    LexerToken char_lit() { // handle character literals

        char c = nextChar;
        int n = (int) c;

        return new LexerToken(LexerToken.TokenType.Integer, "" + n, line, linePos);
    }

    LexerToken string_lit() { // handle string literals

        return new LexerToken(LexerToken.TokenType.String, lexeme, line, linePos);
    }

    //method for determining if the next lexeme was a division symbol or a comment
    boolean div_or_comment() { // handle division or comments

        if (this.nextChar == '/') {// need to peak 1 more character ahead...
            getNextChar();
            if (this.nextChar == '*') {
                addNextChar();
                findStringCommentEnd('/', true);
                return true;
            }
        }

        return false;
    }

    boolean isStringLiteral() {

        if (this.nextChar == '\"') {
            getNextChar();
            findStringCommentEnd('\"', false);
            return true;
        }
        return false;
    }

    LexerToken identifier_or_integer(tokenCategory tokenCategory) { // handle identifiers and integers
        getNextChar();
        if (tokenCategory == Lexer.tokenCategory.identifier) {
            return new LexerToken(LexerToken.TokenType.Identifier, lexeme, line, linePos);
        } else {
            return new LexerToken(LexerToken.TokenType.Integer, lexeme, line, linePos);
        }
    }

    //method for handling operator and keyword lexeme
    LexerToken ops_keywords() {

        buildLexeme();
        LexerToken.TokenType type = keywords.get(lexeme);
        getNextChar();

        return new LexerToken(type, "", line, linePos);
    }

    //method for getting a token out of a symbol. different than ops_keywords because it's only 1 character long
    LexerToken symbols() {
        LexerToken.TokenType type = keywords.get(lexeme);
        getNextChar();

        return new LexerToken(type, "", line, linePos);
    }

    String printTokens() {

        LexerToken token;
        StringBuilder sb = new StringBuilder();

        while ((token = getToken()).tokenType != LexerToken.TokenType.End_of_input) {
            sb.append(token);
            sb.append("\n");
            System.out.println(token);
        }
        sb.append(token);
        System.out.println(token);
        return sb.toString();
    }

    static void outputToFile(String result) {
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/hello.lex");
            myWriter.write(result);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void buildKeywordsMap() {

        keywords = new HashMap<String, LexerToken.TokenType>();

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
    }

    void buildCategoryMap() {
        categories = new HashMap<String, tokenCategory>();

        categories.put("*", tokenCategory.operator);
        categories.put("/", tokenCategory.operator);
        categories.put("%", tokenCategory.operator);
        categories.put("+", tokenCategory.operator);
        categories.put("-", tokenCategory.operator);
        categories.put("<", tokenCategory.operator);
        categories.put("<=", tokenCategory.operator);
        categories.put(">", tokenCategory.operator);
        categories.put(">=", tokenCategory.operator);
        categories.put("==", tokenCategory.operator);
        categories.put("!=", tokenCategory.operator);
        categories.put("!", tokenCategory.operator);
        categories.put("=", tokenCategory.operator);
        categories.put("&&", tokenCategory.operator);
        categories.put("||", tokenCategory.operator);

        categories.put("(", tokenCategory.symbol);
        categories.put(")", tokenCategory.symbol);
        categories.put("{", tokenCategory.symbol);
        categories.put("}", tokenCategory.symbol);
        categories.put(";", tokenCategory.symbol);
        categories.put(",", tokenCategory.symbol);

        categories.put("if", tokenCategory.keyword);
        categories.put("else", tokenCategory.keyword);
        categories.put("while", tokenCategory.keyword);
        categories.put("print", tokenCategory.keyword);
        categories.put("putc", tokenCategory.keyword);

        categories.put("\"", tokenCategory.string);

        categories.put("\u0000", tokenCategory.eof);
    }
}
