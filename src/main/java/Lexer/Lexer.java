package Lexer;

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

    Lexer(String input) {

        this.line = 1;
        this.linePos = 1;
        this.charPosition = 0;
        this.source = input;
        this.nextChar = this.source.charAt(charPosition);
        this.lexeme = "";

        this.keywords = LexerUtilities.buildKeywordsMap();
        this.categories = LexerUtilities.buildCategoryMap();
    }

    public int getLine(){return this.line;}
    public int getLinePos(){return this.linePos;}
    public int getCharPosition(){ return this.charPosition;}
    public String getSource(){ return this.source;}
    public String getLexeme(){return this.lexeme;}

    /*
    Primary method for this class. Restarts the line position counter and clears the lexeme string
    Checks for EOF and whitespace then categorizes the lexeme it finds into symbols, identifiers, etc
     */
    public LexerToken getToken() {

        //reset the lexeme field so we can start over
        this.linePos += lexeme.length();
        this.lexeme = "";

        //get to the next lexeme and increment our positioning value for output
        while (Character.isWhitespace(this.nextChar)) {
            if (this.nextChar == '\n') {
                this.line++;
                this.linePos = 1;
                getNextChar();
                if (atEndOfFile()) {

                    break;
                }
            } else {
                this.linePos++;
                getNextChar();
            }
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
                LexerUtilities.error(this.line, this.linePos, this.lexeme);
            default:
                return identifier_or_integer(tokenCategory);
        }

    }

    //method for looking at the next non whitespace or new line character and finding its category
    tokenCategory categorizeLexeme() {

        //check to see if this is the end of the file so we can end early
        if (atEndOfFile()) {
            this.linePos = 1;
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
            Pattern pattern = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*"); //pattern provided via instruction
            Matcher matcher = pattern.matcher(lexeme);
            if (matcher.matches()) {
                return tokenCategory.identifier; // lexeme is an identifier
            }

            //check again to see if it's an integer
            pattern = Pattern.compile("[0-9]+"); //pattern provided via instruction
            matcher = pattern.matcher(lexeme);
            if (matcher.matches()) {
                return tokenCategory.integer; // lexeme is an integer
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
            //check to see if it's a character literal
            if (ischarLiteral()) {
                return tokenCategory.character;
            }

            //lexeme may still be an operator or symbol
            if (categories.containsKey(this.lexeme)) {
                return categories.get(this.lexeme);
            } else {
                return tokenCategory.error;
            }

        }

        return tokenCategory.error;
    }

    // method for adding characters from string into lexeme
    void buildLexeme() {

        //check to see if we are at the end of the source string. If so set our lexeme to the EOF character
        if (atEndOfFile()) {
            lexeme = "\u0000";
            this.linePos = this.charPosition;
            return;
        }
        // reach ahead into the source string until we hit whitespace or a symbol
        while (!nextIsWhiteSpace() && !nextIsASymbol()) {

            getNextChar();
            addNextChar();

            if (atEndOfFile()) { //check to see if we are about to hit the end of the file.
                break;
            }
        }
    }

    /*
        Helper method for finding the end of a string, character or comment.
        I realized after creating this that it was what the 'follow' method was for in the original code
    */
    void findStringCommentEnd(char endChar, boolean tallyNewLine) {

        while (this.nextChar != endChar) {

            if (tallyNewLine && this.nextChar == '\n') {
                this.line++;
            }

            addNextChar();
            getNextChar();
        }
        addNextChar();
        getNextChar();
    }

    //Method for looking ahead by 1 space to see if it's the end of the file
    boolean nextIsWhiteSpace() {
        return Character.isWhitespace(this.source.charAt(this.charPosition + 1));
    }

    //Method for peeking ahead by 1 character to check for parenthesis and other symbols
    boolean nextIsASymbol() {

        String nextCharacter = "" + this.source.charAt(this.charPosition + 1);
        boolean isSymbol = this.keywords.containsKey(nextCharacter);

        return isSymbol;
    }

    /*
    Method for setting next character from the source string. I tried using the updated method provided
    through canvas but had trouble making it work with the same logic and flow I had already begun using.
    I ended up breaking the method into 3 parts: checking for end of file, getting the next character
    and adding the next character.
     */
    void getNextChar() {
        if (!atEndOfFile()) {
            this.charPosition++;
            this.nextChar = this.source.charAt(this.charPosition);
        }
    }

    /*
    method for adding next character to the lexeme field. Separated this from the original getNextChar
    because I found I was adding characters in places I wasn't always getting the next character.
    */

    void addNextChar() {
        this.lexeme += this.nextChar;
    }

    //method for checking if the next position will be outside the bounds of the source string length
    boolean atEndOfFile() {

        return (this.charPosition + 1) >= this.source.length();
    }

    LexerToken char_lit() { // handle character literals

        //get character from lexeme
        char c = lexeme.toCharArray()[1];
        int n = (int) c;

        return new LexerToken(LexerToken.TokenType.Character, "" + n, line, linePos);
        // return new Lexer.LexerToken(Lexer.LexerToken.TokenType.Character, n + " '" + c + "'", line, linePos);
    }

    LexerToken string_lit() { // handle string literals

        return new LexerToken(LexerToken.TokenType.String, lexeme, line, linePos);
    }

    //method for determining if the next lexeme was a division symbol or a comment
    boolean div_or_comment() { // handle division or comments

        if (this.nextChar == '/') {// need to peak 1 more character ahead...
            char oneCharAhead = source.charAt(charPosition + 1);
            if (oneCharAhead == '*') {
                getNextChar();
                addNextChar();
                findStringCommentEnd('/', true);
                return true;
            }
        }

        return false;
    }

    //Helper method for checking if this lexeme is actually a string literal
    boolean isStringLiteral() {

        if (this.nextChar == '\"') {
            getNextChar();
            findStringCommentEnd('\"', false);
            return true;
        }
        return false;
    }

    //Method for checking the next character for char literal patterns
    boolean ischarLiteral() {

        if (this.nextChar == '\'') {
            getNextChar();
            findStringCommentEnd('\'', false);

            //check to see if it matches the pattern provided via project instructions
            Pattern pattern = Pattern.compile("'([^'\\n]|\\\\n|\\\\\\\\)'");
            Matcher matcher = pattern.matcher(lexeme);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    //method for creating a token for integers and identifiers
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

        //we need to check one character ahead to see if this operator uses two symbols
        char oneCharAhead = source.charAt(charPosition + 1);
        String twoChar = lexeme + oneCharAhead;

        if (keywords.containsKey(twoChar)) {
            getNextChar();
            addNextChar();
        }

        //need to check if it was a unary not operator
        if (lexeme.equals("!") && !Character.isWhitespace(oneCharAhead) && oneCharAhead != '=') {
            getNextChar();
            return new LexerToken(LexerToken.TokenType.Op_not, "", line, linePos);
        }
        //check if it was a unary minus
        else if (lexeme.equals("-") && !Character.isWhitespace(oneCharAhead)) {
            getNextChar();
            return new LexerToken(LexerToken.TokenType.Op_negate, "", line, linePos);
        }

        //buildLexeme();
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
}
