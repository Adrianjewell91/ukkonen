import java.util.*;

public class Main {
    /*
    TESTS

    A sequence of strings comprise the tests, and each string derives from the former according to this way:

    abc|abc|dea|abcabf|abcabcdg.

    String 1 tests basic construction.
    String 2 tests basic construction with repeats.
    String 3 tests simple branching. 
    String 4 tests branching using the suffix link technique.
    String 5 tests additionally the skip-jump technique that must occur when a suffix traversal reaches the root node, but must traverse down several nodes before continuing the branching.
    */

    // String 1: abc
    // /abc
    // /bc
    // /c
    static String s1Test = "/abc\n/bc\n/c\n";
    static String s1 = "abc"; // Simple

    // String 2: abcabc
    // /abcabc
    // /bcabc
    // /cabc
    static String s2Test = "/abcabc\n/bcabc\n/cabc\n";
    static String s2 = "abcabc"; // Check counter goes up

    // String 3: abcabcdea
    // /abc/abcdea
    // /abc/dea
    // /bc/abcdea
    // /bc/dea
    // /c/abcdea
    // /c/dea
    // /dea
    // /ea
    static String s3Test = "/abc/abcdea\n/abc/dea\n/bc/abcdea\n/bc/dea\n/c/abcdea\n/c/dea\n/dea\n/ea\n";
    static String s3 = "abcabcdea"; // Check formation of suffix links

    // String 4: abcabcdeabcabf
    // /ab/c/ab/cdeabcabf
    // /ab/c/ab/f
    // /ab/c/deabcabf
    // /ab/f
    // /b/c/ab/cdeabcabf
    // /b/c/ab/f
    // /b/c/deabcabf
    // /b/f
    // /c/ab/cdeabcabf
    // /c/ab/f
    // /c/deabcabf
    // /deabcabf
    // /eabcabf
    // /f
    static String s4Test = "/ab/c/ab/cdeabcabf\n/ab/c/ab/f\n/ab/c/deabcabf\n/ab/f\n/b/c/ab/cdeabcabf\n/b/c/ab/f\n/b/c/deabcabf\n/b/f\n/c/ab/cdeabcabf\n/c/ab/f\n/c/deabcabf\n/deabcabf\n/eabcabf\n/f\n";
    static String s4 = "abcabcdeabcabf"; // Check traversal of suffix links to do second branching.

    // String 5: abcabcdeabcabfabcabcdg
    // /ab/c/ab/cd/eabcabfabcabcdg
    // /ab/c/ab/cd/g
    // /ab/c/ab/fabcabcdg
    // /ab/c/d/eabcabfabcabcdg
    // /ab/c/d/g
    // /ab/fabcabcdg
    // /b/c/ab/cd/eabcabfabcabcdg
    // /b/c/ab/cd/g
    // /b/c/ab/fabcabcdg
    // /b/c/d/eabcabfabcabcdg
    // /b/c/d/g
    // /b/fabcabcdg
    // /c/ab/cd/eabcabfabcabcdg
    // /c/ab/cd/g
    // /c/ab/fabcabcdg
    // /c/d/eabcabfabcabcdg
    // /c/d/g
    // /d/eabcabfabcabcdg
    // /d/g
    // /eabcabfabcabcdg
    // /fabcabcdg
    // /g
    static String s5Test = "/ab/c/ab/cd/eabcabfabcabcdg\n/ab/c/ab/cd/g\n/ab/c/ab/fabcabcdg\n/ab/c/d/eabcabfabcabcdg\n/ab/c/d/g\n/ab/fabcabcdg\n/b/c/ab/cd/eabcabfabcabcdg\n/b/c/ab/cd/g\n/b/c/ab/fabcabcdg\n/b/c/d/eabcabfabcabcdg\n/b/c/d/g\n/b/fabcabcdg\n/c/ab/cd/eabcabfabcabcdg\n/c/ab/cd/g\n/c/ab/fabcabcdg\n/c/d/eabcabfabcabcdg\n/c/d/g\n/d/eabcabfabcabcdg\n/d/g\n/eabcabfabcabcdg\n/fabcabcdg\n/g\n";
    static String s5 = "abcabcdeabcabfabcabcdg"; // Check only node "traversal" after reaching root, proves correct sf
                                          // extensions 2x.

    static String[] strings = new String[] { s1, s2, s3, s4, s5 };
    static String[] tests   = new String[] { s1Test, s2Test, s3Test, s4Test, s5Test };

    public static void main(String[] args) {
        /*
            Confirms structure.
        */
        for (int i = 0; i < strings.length; i++) {
            // System
            //     .out
            //     .println("String: " + strings[i]);

            SuffixTreeBuilder.Node          root = SuffixTreeBuilder.build(strings[i], false, null);
            StringBuilder b    = new StringBuilder();

            suffixes(root, "", strings[i], b);

            System
                .out
                .println(b.toString().equals(tests[i]));

            // System.out.println(b.toString());
            // System.out.println(tests[i]);
        }

        /*
            Confirms suffix link traversal during extensions of branch points.

            Example: String s5: abcabcdeabcabfabcabcdg.

            In the first traversal we insert the 'd' at index 6: three 'traversals' to the root each time.

            In the second traversal we insert the 'f' at index 13: 5 traversals. 
            The first three use the internal nodes created previously. Then 2 more which go back to the root again.

            In the third traversal, we insert the 'g' at the final index: 7 traversals.
            The first three go through the suffix links created previously.
            The second three go through the suffix links created previously previously.
            The last one creates an internal node at root -> de... from which goes back to the root. 

            A special property of the last traversal is it reaches the root halfway through, 
            but the next branching must happen some nodes down, so it is required to skip down a few nodes. The last node that was passed through becomes the suffix link entry point for the next round back to root (see the diagram).
        */
        System.out.println("Logs for string, did it traverse the suffix links correctly: " + s5);
        List<String>  logs = new ArrayList<>();
        SuffixTreeBuilder.build(s5, true, logs);
        String[] traversalsToRoot = {
            "true","true","true",
            "false","false","true","true","true",
            "false","false","true","false","false","true","true"
        };

        int j = 0;
        // System.out.println(String.join("\n", logs));
        for (String log : logs)
        {
          if (log.equals("true") || log.equals("false"))
          {
              System.out.println(traversalsToRoot[j].equals(log));
              j++;
          }
        }
    }

    public static void suffixes(SuffixTreeBuilder.Node tree, String path, String s, StringBuilder builder) {
        if (tree == null) {
            // System.out.println(path);
            builder.append(path + "\n");
            return;
        }

        for (SuffixTreeBuilder.Edge e : tree.edges) {
            if (e == null) {
                continue;
            }

            suffixes(e.child, path + "/" + s.substring(e.start, e.end.end), s, builder);
        }
    }

}
