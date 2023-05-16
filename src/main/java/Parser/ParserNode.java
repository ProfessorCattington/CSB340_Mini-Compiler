package Parser;

public class ParserNode {
        public ParserNodeType nt;
        public ParserNode left, right;
        public String value;

        public ParserNode() {
                this.nt = null;
                this.left = null;
                this.right = null;
                this.value = null;
        }

        public ParserNode(ParserNodeType node_type, ParserNode left, ParserNode right, String value) {
                this.nt = node_type;
                this.left = left;
                this.right = right;
                this.value = value;
        }

        public static ParserNode make_node(ParserNodeType nodetype, ParserNode left, ParserNode right) {
                return new ParserNode(nodetype, left, right, "");
        }

        public static ParserNode make_node(ParserNodeType nodetype, ParserNode left) {
                return new ParserNode(nodetype, left, null, "");
        }

        public static ParserNode make_leaf(ParserNodeType nodetype, String value) {
                return new ParserNode(nodetype, null, null, value);
        }
}