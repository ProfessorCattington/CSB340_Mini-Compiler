package Lexer;

public class LexerToken extends Token {

    public TokenType tokenType;

    public static enum TokenType {
        End_of_input, Op_multiply, Op_divide, Op_mod, Op_add, Op_subtract,
        Op_negate, Op_not, Op_less, Op_lessequal, Op_greater, Op_greaterequal,
        Op_equal, Op_notequal, Op_assign, Op_and, Op_or, Keyword_if,
        Keyword_else, Keyword_while, Keyword_print, Keyword_putc, LeftParen, RightParen,
        LeftBrace, RightBrace, Semicolon, Comma, Identifier, Integer, String, Character, Blank
    }

    LexerToken(TokenType tokenType, String value, int line, int pos) {
        super(value, line, pos);
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {

        String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokenType);
        switch (this.tokenType) {

            case Integer:
                result += String.format("  %4s", value);
                break;
            case Identifier:
                result += String.format(" %s", value);
                break;
            case Character:
                result += String.format(" %s", value);
                break;
            case String:
                result += String.format(" %s", value);
                break;
            case Blank:
                result = "";
                break;
        }
        return result;
    }
}
