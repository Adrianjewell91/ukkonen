import util.Node;
import util.Strings;
import util.Test;

import java.util.*;

//https://www.google.com/search?q=suffix+tree&tbm=isch&hl=en&chips=q:suffix+tree,g_1:mississippi:3G0nRoyESx0%3D&sa=X&ved=2ahUKEwi2-fuAlNiCAxWZIlkFHeHmD74Q4lYoAHoECAEQNA&biw=1648&bih=897#imgrc=VO5eubBqkXoqRM
public class App {
    public static void main(String[] args) throws Exception {
        Node[] tree = Naive.naive(Strings.t, new Node[Node.ARRAY_LENGTH]);
//        util.Node[] tree = Ukkonen.ukkonen(util.Strings.t,new util.Node[util.Node.ARRAY_LENGTH]);
        print(tree);
    }

    private static void print(Node[] tree) {
        printTree(tree);
        Test.printSuffixes(tree, "");
    }

    private static void printTree(Node[] tree) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < Strings.t.length()+1; i++) {
            lines.add("");
        }

        Test.print3(tree[10], lines, 0, "", true);
        Test.print3(tree[11], lines, 0, "", true);
        System.out.println(Strings.t);
        lines.forEach(line -> System.out.println(line));
    }
}