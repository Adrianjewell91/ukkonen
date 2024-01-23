import tree.Builder;
import tree.Node;
import util.Printer;
import util.Strings;
import util.Tester;

//https://www.cs.cmu.edu/afs/cs/project/pscico-guyb/realworld/www/slidesF06/cmuonly/gusfield.pdf
//https://www.google.com/search?q=suffix+tree&tbm=isch&hl=en&chips=q:suffix+tree,g_1:mississippi:3G0nRoyESx0%3D&sa=X&ved=2ahUKEwi2-fuAlNiCAxWZIlkFHeHmD74Q4lYoAHoECAEQNA&biw=1648&bih=897#imgrc=VO5eubBqkXoqRM
public class App {
    public static void main(String[] args) throws Exception {
        Node[] tree;
        /*
            Naive
        */

        // String: abba
        tree = Builder.naive(Strings.test1, root());
        Tester.test(tree, 4);
        Printer.suffixes(tree, "");
        Printer.visualizeABBA(tree);

        // String: xabcxabc
        tree = Builder.naive(Strings.test2, root());
        Tester.test(tree, 8);
        Printer.suffixes(tree, "");


        /*
            Ukkonen's Algorithm
        */

        // String: abba
        tree = Builder.ukkonen(util.Strings.test1, root());
        Printer.suffixes(tree, "");

        // String: xabcxabc
        tree = Builder.ukkonen(util.Strings.test2, root());
        Printer.suffixes(tree, "");

        // String: the abcs
        tree = Builder.ukkonen(util.Strings.test3, root());
        Printer.suffixes(tree, "");
    }

    public static Node[] root() {
        return new Node[Node.ARRAY_LENGTH];
    }
}