/** Parser Utilities
 * @author Chris */

package Parser;
import java.io.*;
import java.util.*;

public class ParserUtilities {
    public static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    public static String buildAST(ParserNode t, StringBuilder sb) {
        int i = 0;
        if (t == null) {
            sb.append(";");
            sb.append("\n");
        } else {
            sb.append(t.nt);
            if (t.nt == ParserNodeType.nd_Ident || t.nt == ParserNodeType.nd_Integer || t.nt == ParserNodeType.nd_String) {
                sb.append(" " + t.value);
                sb.append("\n");
            } else {
                sb.append("\n");
                buildAST(t.left, sb);
                buildAST(t.right, sb);
            }
        }
        return sb.toString();
    }

    public static void printAST(String AST) {
        System.out.println(AST);
    }

    public static void outputToFile(String result, String filePath) {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(result);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void outputToFileDefault(String result, String fileName) {
        String addExtension = fileName + ".par";
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/parser_output/" + addExtension);
            myWriter.write(result);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ParserToken> inputFromFile(String filePath) throws Exception {
        Scanner s = new Scanner(new File(filePath));
        return buildList(s);
    }

    public static List<ParserToken> inputFromFileDefault(String fileName) throws Exception {
        String addExtension = fileName + ".lex";
        Scanner s = new Scanner(new File("src/main/resources/lexer_output/" + addExtension));
        return buildList(s);
    }

    private static List<ParserToken> buildList(Scanner s) throws Exception {
        String value, token;
        int line, pos;
        boolean found;
        List<ParserToken> list = new ArrayList<>();
        Map<String, ParserTokenType> str_to_tokens = ParserTokenType.createTokenMap();
        while (s.hasNext()) {
            String str = s.nextLine();
            StringTokenizer st = new StringTokenizer(str);
            line = Integer.parseInt(st.nextToken());
            pos = Integer.parseInt(st.nextToken());
            token = st.nextToken();
            value = "";
            while (st.hasMoreTokens()) {
                value += st.nextToken() + " ";
            }
            found = false;
            if (str_to_tokens.containsKey(token)) {
                found = true;
                list.add(new ParserToken(str_to_tokens.get(token), value, line, pos));
            }
            if (found == false) {
                throw new Exception("Lexer.Token not found: '" + token + "'");
            }
        }
        return list;
    }
}
