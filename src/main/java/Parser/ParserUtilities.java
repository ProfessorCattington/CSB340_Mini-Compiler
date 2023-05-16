package Parser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ParserUtilities {

    public static void printAST(String AST) {
        System.out.println(AST);
    }

    public static void outputToFile(String result, String file) {
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(result);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ParserToken> inputFromFile(String file) throws Exception {
        String value, token;
        int line, pos;
        ParserToken t;
        boolean found;
        List<ParserToken> list = new ArrayList<>();
        Map<String, ParserTokenType> str_to_tokens = ParserTokenType.createTokenMap();
        Scanner s = new Scanner(new File(file));
        String source = "";
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
