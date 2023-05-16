/** Parser Token Type
 * @author Chris */

package Parser;
import java.util.*;

public enum ParserTokenType {
    End_of_input(false, false, false, -1, ParserNodeType.nd_None),
    Op_multiply(false, true, false, 13, ParserNodeType.nd_Mul),
    Op_divide(false, true, false, 13, ParserNodeType.nd_Div),
    Op_mod(false, true, false, 13, ParserNodeType.nd_Mod),
    Op_add(false, true, false, 12, ParserNodeType.nd_Add),
    Op_subtract(false, true, false, 12, ParserNodeType.nd_Sub),
    Op_negate(false, false, true, 14, ParserNodeType.nd_Negate),
    Op_not(false, false, true, 14, ParserNodeType.nd_Not),
    Op_less(false, true, false, 10, ParserNodeType.nd_Lss),
    Op_lessequal(false, true, false, 10, ParserNodeType.nd_Leq),
    Op_greater(false, true, false, 10, ParserNodeType.nd_Gtr),
    Op_greaterequal(false, true, false, 10, ParserNodeType.nd_Geq),
    Op_equal(false, true, true, 9, ParserNodeType.nd_Eql),
    Op_notequal(false, true, false, 9, ParserNodeType.nd_Neq),
    Op_assign(false, false, false, -1, ParserNodeType.nd_Assign),
    Op_and(false, true, false, 5, ParserNodeType.nd_And),
    Op_or(false, true, false, 4, ParserNodeType.nd_Or),
    Keyword_if(false, false, false, -1, ParserNodeType.nd_If),
    Keyword_else(false, false, false, -1, ParserNodeType.nd_None),
    Keyword_while(false, false, false, -1, ParserNodeType.nd_While),
    Keyword_print(false, false, false, -1, ParserNodeType.nd_None),
    Keyword_putc(false, false, false, -1, ParserNodeType.nd_None),
    LeftParen(false, false, false, -1, ParserNodeType.nd_None),
    RightParen(false, false, false, -1, ParserNodeType.nd_None),
    LeftBrace(false, false, false, -1, ParserNodeType.nd_None),
    RightBrace(false, false, false, -1, ParserNodeType.nd_None),
    Semicolon(false, false, false, -1, ParserNodeType.nd_None),
    Comma(false, false, false, -1, ParserNodeType.nd_None),
    Identifier(false, false, false, -1, ParserNodeType.nd_Ident),
    Integer(false, false, false, -1, ParserNodeType.nd_Integer),
    String(false, false, false, -1, ParserNodeType.nd_String);

    private final int precedence;
    private final boolean right_assoc;
    private final boolean is_binary;
    private final boolean is_unary;
    private final ParserNodeType node_type;

    ParserTokenType(boolean right_assoc, boolean is_binary, boolean is_unary, int precedence, ParserNodeType node) {
        this.right_assoc = right_assoc;
        this.is_binary = is_binary;
        this.is_unary = is_unary;
        this.precedence = precedence;
        this.node_type = node;
    }

    public boolean isRightAssoc() {
        return this.right_assoc;
    }

    public boolean isBinary() {
        return this.is_binary;
    }

    public boolean isUnary() {
        return this.is_unary;
    }

    public int getPrecedence() {
        return this.precedence;
    }

    public ParserNodeType getNodeType() {
        return this.node_type;
    }

    public static Map<String, ParserTokenType> createTokenMap() {
        Map<String, ParserTokenType> str_to_tokens = new HashMap<>();
        // Add all tokens to map
        str_to_tokens.put("End_of_input", ParserTokenType.End_of_input);
        str_to_tokens.put("Op_multiply", ParserTokenType.Op_multiply);
        str_to_tokens.put("Op_divide", ParserTokenType.Op_divide);
        str_to_tokens.put("Op_mod", ParserTokenType.Op_mod);
        str_to_tokens.put("Op_add", ParserTokenType.Op_add);
        str_to_tokens.put("Op_subtract", ParserTokenType.Op_subtract);
        str_to_tokens.put("Op_negate", ParserTokenType.Op_negate);
        str_to_tokens.put("Op_not", ParserTokenType.Op_not);
        str_to_tokens.put("Op_less", ParserTokenType.Op_less);
        str_to_tokens.put("Op_lessequal", ParserTokenType.Op_lessequal);
        str_to_tokens.put("Op_greater", ParserTokenType.Op_greater);
        str_to_tokens.put("Op_greaterequal", ParserTokenType.Op_greaterequal);
        str_to_tokens.put("Op_equal", ParserTokenType.Op_equal);
        str_to_tokens.put("Op_notequal", ParserTokenType.Op_notequal);
        str_to_tokens.put("Op_assign", ParserTokenType.Op_assign);
        str_to_tokens.put("Op_and", ParserTokenType.Op_and);
        str_to_tokens.put("Op_or", ParserTokenType.Op_or);
        str_to_tokens.put("Keyword_if", ParserTokenType.Keyword_if);
        str_to_tokens.put("Keyword_else", ParserTokenType.Keyword_else);
        str_to_tokens.put("Keyword_while", ParserTokenType.Keyword_while);
        str_to_tokens.put("Keyword_print", ParserTokenType.Keyword_print);
        str_to_tokens.put("Keyword_putc", ParserTokenType.Keyword_putc);
        str_to_tokens.put("LeftParen", ParserTokenType.LeftParen);
        str_to_tokens.put("RightParen", ParserTokenType.RightParen);
        str_to_tokens.put("LeftBrace", ParserTokenType.LeftBrace);
        str_to_tokens.put("RightBrace", ParserTokenType.RightBrace);
        str_to_tokens.put("Semicolon", ParserTokenType.Semicolon);
        str_to_tokens.put("Comma", ParserTokenType.Comma);
        str_to_tokens.put("Identifier", ParserTokenType.Identifier);
        str_to_tokens.put("Integer", ParserTokenType.Integer);
        str_to_tokens.put("String", ParserTokenType.String);
        return str_to_tokens;
    }
}
