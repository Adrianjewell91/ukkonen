package util;

import tree.Node;

import java.util.ArrayList;
import java.util.List;

public class Tester {
    public static void test(Node[] tree, int quantity) throws Exception {
        List<String> suffixes = Tester.getSuffixes(tree, "", new ArrayList<>());
        System.out.println(suffixes.size() == quantity);
    }

    public static List<String> getSuffixes(Node[] tree, String path, List<String> suffixes) {
        boolean isTerminus = true;
        for (Node n:tree
        ) {
            if (n == null) { continue; }

            isTerminus = false;
            getSuffixes(n.children, path + n.letter, suffixes);
        }

        if (isTerminus) {
            suffixes.add(path);
        }

        return suffixes;
    }
}
