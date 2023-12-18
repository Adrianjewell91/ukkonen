package util;

import java.util.List;

public class Test {
    //For three unique characters. (2 regular and the terminating)
    //"Lines" Array must be same length as the string.
    public static void print3(Node tree, List<String> lines, int depth, String path, boolean isNewMiddle) {
        if (depth == lines.size()-1) {
            for (int i = 0; i<depth; i++) {
                lines.set(i, lines.get(i) + path.charAt(i));
            }
            lines.set(depth, lines.get(depth) + (tree == null ? "_" : tree.letter));
            return;
        }

        // Get left middle and right
        Node left = tree == null ? null : tree.children[10];
        Node middle = tree == null ? null : tree.children[11];
        Node right = tree == null ? null : tree.children[26]; //the $ character

        // clean the path here from previous characters.
        String cleanPath =
                isNewMiddle ? path : " ".repeat(path.length());

        // left(path + "")
        print3(left, lines, depth+1, cleanPath + " ", true);
        // middle(path + char)
        print3(middle, lines, depth+1, path + (tree == null ? "_" : tree.letter), false);
        // right (path + "")
        print3(right, lines, depth+1, cleanPath + " ", true);
    }

    // Does the exact some thing as above.
    public static void print3Middle(Node tree, List<String> lines, int depth) {
        if (depth < 0) {
            return;
        }

        String s = (" ".repeat(depth == 0 ? 0 : (int) ((Math.pow(3, depth) - 3) / 2)))
                +
                (" ".repeat(depth == 0 ? 0 : 1))
                +
                (tree == null ? "_" : tree.letter)
                +
                (" ".repeat(depth == 0 ? 0 : 1))
                +
                (" ".repeat(depth == 0 ? 0 : (int) ((Math.pow(3, depth) - 3) / 2)));

        lines.set(depth, lines.get(depth) + s);

        Node left = tree == null ? null : tree.children[10];
        Node middle = tree == null ? null : tree.children[11];
        Node right = tree == null ? null : tree.children[26];

        print3Middle(left, lines, depth - 1);
        print3Middle(middle, lines, depth - 1);
        print3Middle(right, lines, depth - 1);
    }

    // Prints with 2 unique characters (1 regular + terminator)
    //Lines must be same length as the string.
    public static void print(Node tree, List<String> lines, int depth) {
        if (depth == lines.size()) {
            return;
        }

        Node left = tree == null ? null : tree.children[10];
        Node right = tree == null ? null : tree.children[26];

        print(left, lines, depth + 1);

        for(int i = 0 ; i<lines.size(); i++) {
            Character letter = tree == null ? '_' : tree.letter;

            if (i == depth) {
                lines.set(i, lines.get(i) + letter);
            } else {
                lines.set(i, lines.get(i) + " ");
            }
        }

        print(right, lines, depth + 1);
    }

    //prints all paths using dfs.
    public static void printSuffixes(Node[] tree, String path) {
        boolean isTerminus = true;
        for (Node n:tree
        ) {
            if (n == null) { continue; }

            isTerminus = false;
            printSuffixes(n.children, path + n.letter);
        }

        if (isTerminus) {
            System.out.println(path);
        }
    }
}
