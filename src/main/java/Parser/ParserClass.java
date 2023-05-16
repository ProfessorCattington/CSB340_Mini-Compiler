package Parser;
import java.io.FileNotFoundException;
import java.util.*;

public class ParserClass {
    private List<ParserToken> source;
    private ParserToken token;
    private int position;

    public ParserClass(List<ParserToken> source) {
        this.source = source;
        this.token = null;
        this.position = 0;
    }

    public ParserToken getNextToken() {
        this.token = this.source.get(this.position++);
        return this.token;
    }

    public ParserTokenType getType() {
        return this.token.tokentype;
    }

    public Boolean isType(ParserTokenType type) {
        return getType() == type;
    }

    public String getVal() {
        return this.token.value;
    }

    public ParserNode expr(int p) {
        // create nodes for token types such as LeftParen, Op_add, Op_subtract, etc.
        // be very careful here and be aware of the precedence rules for the AST tree
        ParserNode result = null, node;
        ParserTokenType temp;
        if (isType(ParserTokenType.LeftParen)) {
            result = paren_expr();
        } else if (isType(ParserTokenType.Op_subtract) || isType(ParserTokenType.Op_add)) {
            if (isType(ParserTokenType.Op_subtract)) {
                temp = ParserTokenType.Op_negate;
            } else {
                temp = ParserTokenType.Op_add;
            }
            getNextToken();
            node = expr(ParserTokenType.Op_negate.getPrecedence());
            if (isType(ParserTokenType.Op_negate)) {
                result = ParserNode.make_node(ParserNodeType.nd_Negate, node);
            } else {
                result = node;
            }
        } else if (isType(ParserTokenType.Op_not)) {
            getNextToken();
            result = ParserNode.make_node(ParserNodeType.nd_Not, expr(ParserTokenType.Op_not.getPrecedence()));
        } else if (isType(ParserTokenType.Identifier)) {
            result = ParserNode.make_leaf(ParserNodeType.nd_Ident, getVal());
            getNextToken();
        } else if (isType(ParserTokenType.Integer)) {
            result = ParserNode.make_leaf(ParserNodeType.nd_Integer, getVal());
            getNextToken();
        } else {
            ParserUtilities.error(token.line, token.pos, "ERROR: Unexpected Input");
        }
        int precedence = 0;
        while (getType().isBinary() && getType().getPrecedence() >= p) {
            temp = getType();
            getNextToken();
            precedence = temp.getPrecedence();
            if (!temp.isRightAssoc()) {
                precedence++;
            }
            node = expr(precedence);
            result = ParserNode.make_node(temp.getNodeType(), result, node);
        }
        return result;
    }

    public ParserNode paren_expr() {
        expect("paren_expr", ParserTokenType.LeftParen);
        ParserNode node = expr(0);
        expect("paren_expr", ParserTokenType.RightParen);
        return node;
    }

    public void expect(String msg, ParserTokenType s) {
        if (getType() == s) {
            getNextToken();
            return;
        }
        ParserUtilities.error(this.token.line, this.token.pos, msg + ": Expecting '" + s + "', found: '"
                + getType() + "'");
    }

    public ParserNode stmt() {
        // this one handles TokenTypes such as Keyword_if, Keyword_else, nd_If, Keyword_print, etc.
        // also handles while, end of file, braces
        ParserNode s, s2, t = null, e, v;
        if (isType(ParserTokenType.Keyword_if)) {
            getNextToken();
            e = paren_expr();
            s = stmt();
            s2 = null;
            if (isType(ParserTokenType.Keyword_else)) {
                getNextToken();
                s2 = stmt();
            }
            t = ParserNode.make_node(ParserNodeType.nd_If, e, ParserNode.make_node(ParserNodeType.nd_If, s, s2));
        } else if (isType(ParserTokenType.Keyword_putc)) {
            getNextToken();
            e = paren_expr();
            t = ParserNode.make_node(ParserNodeType.nd_Prtc, e);
            expect("Putc", ParserTokenType.Semicolon);
        } else if (isType(ParserTokenType.Keyword_print)) {
            getNextToken();
            expect("Print", ParserTokenType.LeftParen);
            while (1 == 1) {
                if (isType(ParserTokenType.String)) {
                    // print string
                    e = ParserNode.make_node(ParserNodeType.nd_Prts, ParserNode.make_leaf(ParserNodeType.nd_String, getVal()));
                    getNextToken();
                } else {
                    // print integer
                    e = ParserNode.make_node(ParserNodeType.nd_Prti, expr(0), null);
                }
                t = ParserNode.make_node(ParserNodeType.nd_Sequence, t, e);

                if (isType(ParserTokenType.Comma)) {
                    getNextToken();
                } else {
                    break;
                }
            }
            expect("Print", ParserTokenType.RightParen);
            expect("Print", ParserTokenType.Semicolon);
        } else if (isType(ParserTokenType.Semicolon)) {
            getNextToken();
        } else if (isType(ParserTokenType.Identifier)) {
            v = ParserNode.make_leaf(ParserNodeType.nd_Ident, token.value);
            getNextToken();
            expect("assign", ParserTokenType.Op_assign);
            e = expr(0);
            t = ParserNode.make_node(ParserNodeType.nd_Assign, v, e);
            expect("assign", ParserTokenType.Semicolon);
        } else if (isType(ParserTokenType.Keyword_while)) {
            getNextToken();
            e = paren_expr();
            s = stmt();
            t = ParserNode.make_node(ParserNodeType.nd_While, e, s);
        } else if (isType(ParserTokenType.LeftBrace)) {
            getNextToken();
            while (getType() != ParserTokenType.End_of_input &&
                    getType() != ParserTokenType.RightBrace) {
                t = ParserNode.make_node(ParserNodeType.nd_Sequence, t, stmt());
            }
            expect("LeftBrace", ParserTokenType.RightBrace);
        } else if (isType(ParserTokenType.End_of_input)) {
            // do nothing
        } else {
            ParserUtilities.error(token.line, token.pos, "ERROR: Unexpected Input");
        }
        return t;
    }

    public ParserNode parse() {
        ParserNode t = null;
        getNextToken();
        while (getType() != ParserTokenType.End_of_input) {
            t = ParserNode.make_node(ParserNodeType.nd_Sequence, t, stmt());
        }
        return t;
    }

    public static void runParser(String inputFilePath, String outputFilePath) {
        if (1 == 1) {
            try {
                List<ParserToken> list = ParserUtilities.inputFromFile(inputFilePath);
                ParserClass p = new ParserClass(list);
                String result = "";
                StringBuilder sb = new StringBuilder();
                result = ParserUtilities.buildAST(p.parse(), sb);
                ParserUtilities.printAST(result);
                ParserUtilities.outputToFile(result, outputFilePath);
            } catch (FileNotFoundException e) {
                ParserUtilities.error(-1, -1, "Exception: " + e.getMessage());
            } catch (Exception e) {
                ParserUtilities.error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            ParserUtilities.error(-1, -1, "No args");
        }
    }

    public static void runParser(ArrayList<String> inputFilePaths, ArrayList<String> outputFilePaths) {
        if(inputFilePaths.size() == outputFilePaths.size()) {
            for(int i = 0; i < inputFilePaths.size(); i++) {
                runParser(inputFilePaths.get(i), outputFilePaths.get(i));
            }
        }
    }
}







