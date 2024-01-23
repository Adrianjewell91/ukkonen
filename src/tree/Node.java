package tree;

public class Node {
    public static final int ARRAY_LENGTH = 27;
    public char letter;
    public Node[] children;

    public Node(char c) {
        this.letter = c;
        this.children = new Node[ARRAY_LENGTH];
    }
}
