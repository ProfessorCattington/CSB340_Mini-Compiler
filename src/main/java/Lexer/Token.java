/** Token Base Class
 * @author Michael */

package Lexer;

public class Token {

    public String value;
    public int line;
    public int pos;

    Token(String value, int line, int pos) {
        this.value = value;
        this.line = line;
        this.pos = pos;
    }

    public String toString() {
        return "";
    }
}
