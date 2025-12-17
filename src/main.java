import java.util.*;

public class Main {

    /*
        Represents a substring using a start and end index.
    */
    public static class Edge {
        public int start;
        public End end;
        public Node child;

        public Edge(
                int start,
                End end) {
            this.start = start;
            this.end = end;
        }
    }

    /*
        Represents either the root of the tree or an internal branch point.
    */
    public static class Node {
        public boolean isRoot;
        public Node suffixLink;
        public Edge[] edges = new Edge[27];

        public Node() {
        }
    }

    /*
        Represents a global pointer to the terminal index. 
        All terminal edges to a single instance of this class.
    */
    public static class End {
        public int end;

        public End(
                int end) {
            this.end = end;
        }
    }

    /*
    TESTS
    */

    // String: abc
    // /abc
    // /bc
    // /c
    static String s1Test = "/abc\n/bc\n/c\n";
    static String s1 = "abc"; // Simple

    // String: abcabc
    // /abcabc
    // /bcabc
    // /cabc
    static String s2Test = "/abcabc\n/bcabc\n/cabc\n";
    static String s2 = "abcabc"; // Check counter goes up

    // String: abcabcdea
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

    // String: abcabcdeabcabf
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

    // String: abcabcdeabcabfabcabcdg
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
            Confirm structure.
        */
        for (int i = 0; i < strings.length; i++) {
            // System
            //     .out
            //     .println("String: " + strings[i]);
            
            Node          root = build(strings[i], false, null);
            StringBuilder b    = new StringBuilder();
            
            suffixes(root, "", strings[i], b);
            
            System
                .out
                .println(b.toString().equals(tests[i]));
            
            // System.out.println(b.toString());
            // System.out.println(tests[i]);
        }

        /*
            Confirm suffix link traversal during extensions of branch points.

            Example: String s5: abcabcdeabcabfabcabcdg.

            In the first traversal we insert the 'd' at index 6: three 'traversals' to the root each time.

            In the second traversal we insert the 'f' at index 13: 5 traversals. 
            The first there use the internal nodes created previously. Then 2 more which go back to the root again.

            In the third traversal, we insert the 'g' at the final index: 7 traversals.
            The first 3 go through the suffix links created previously.
            The second 3 go through the suffix links created previously previously.
            The last 1 creates an internal node on Root->de... from, which goes back to the root. 

            A special property of the last traversal, is that halfway through the traversal reaches the Root, 
            but the next branch must happen some node down, so it is required to skip down a few nodes. The last node that was passed through becomes the suffix link entry point for the next round back to root (see the diagram).
        */
        List<String>  logs = new ArrayList<>();
        build(s5, true, logs);

        System.out.println(String.join("\n", logs));
    }

    /*
     * Ukkonen's Algorithm.
     * 
     * Overview: Build a suffix tree in O(N) time by optimizing a naive O(N^^3)
     * algorithm.
     *
     * Naive O(N^^3) algorithm:
     
        for each substring (0..i) in string:
            build suffix tree:
                for each char in substring:
                    insert into suffix tree;
            
     * 
     * Optimizations:
     * 
     * 1. Extending each suffix: a) Maintain a global pointer to the last index and
     * update the pointer instead.
     * 2. Paths that branch : a) Maintain a pointer of how far down the suffix path
     * already traversed.
     * b) Point internal branch points to one another, then:
     * c) Traverse the branch points to avoid dfs path traversals during branch
     * creation.
     */
    public static Node build(String s, boolean isDebug, List<String> logs) {
        Node root = new Node();
        root.suffixLink = root;
        root.isRoot = true;

        End globalEnd = new End(0);
        int counter = 0;
        int gbl = 0;
        Node currentNode = root;
        Edge currentEdge = null;

        for (int i = 0; i < s.length(); i++) {
            
            char c = s.charAt(i);

            if (isDebug == true)
            {
                logs.add("Inserting char " + c + ".");
            }
            
            Node lastCreatedInternalNode = null;

            if (currentEdge != null
                    &&
                    c != s.charAt(counter + currentEdge.start)) {
                // todo: reset counter here instead?
                int local = counter;

                while (gbl > 0) {
                    Node internalNode = new Node();

                    Edge splitOffEdge = new Edge(currentEdge.start + local, currentEdge.end);
                    splitOffEdge.child = currentEdge.child;

                    internalNode.edges[s.charAt(
                            currentEdge.start
                                    +
                                    local)
                            -
                            'a'] = splitOffEdge;
                    internalNode.edges[c - 'a'] = new Edge(i, globalEnd);
                    internalNode.suffixLink = root;

                    currentEdge.child = internalNode;
                    currentEdge.end = new End(currentEdge.start + local);

                    if (lastCreatedInternalNode == null) {
                        lastCreatedInternalNode = internalNode;
                    } else {
                        lastCreatedInternalNode.suffixLink = internalNode;
                        lastCreatedInternalNode = internalNode;
                    }

                    if (currentNode.suffixLink.isRoot
                            &&
                            !currentNode.isRoot) {
                        lastCreatedInternalNode = null;
                    }

                    currentNode = currentNode.suffixLink;

                    if (isDebug == true)
                    {
                        logs.add("Traversed to a Suffix Link. Is it root: " + String.valueOf(currentNode.isRoot));
                    }
                    // this line may not be required
                    currentEdge = null;

                    gbl--;
                    if (gbl <= 0)
                        break;

                    if (currentNode.isRoot) {
                        local = gbl;
                        currentEdge = currentNode.edges[s.charAt(i - local) - 'a'];

                        while (local > (currentEdge.end.end - currentEdge.start)) {
                            local -= currentEdge.end.end - currentEdge.start;
                            currentNode = currentEdge.child;
                            currentEdge = currentNode.edges[s.charAt(i - local) - 'a'];
                        }
                    } else {
                        currentEdge = currentNode.edges[s.charAt(i - local) - 'a'];
                    }
                }

                if (lastCreatedInternalNode != null) {
                    lastCreatedInternalNode.suffixLink = root;
                    lastCreatedInternalNode = null;
                }

                // technically duplicate to line ~224
                Edge e = new Edge(i, globalEnd);
                currentNode.edges[c - 'a'] = e;
                currentEdge = null;
                counter = 0;
            } else if (currentEdge != null
                    &&
                    c == s.charAt(counter + currentEdge.start)) {
                if (counter + currentEdge.start == currentEdge.end.end) {
                    currentNode = currentEdge.child;
                    currentEdge = currentNode.edges[c - 'a'];
                    counter = 1;
                } else {
                    counter++;
                }

                gbl++;
            } else if (currentNode.edges[c - 'a'] == null) {
                Edge e = new Edge(i, globalEnd);
                currentNode.edges[c - 'a'] = e;
            } else {
                currentEdge = currentNode.edges[c - 'a'];
                counter = 1;

                gbl++;
            }

            globalEnd.end = i + 1;
        }

        return root;
    }

    // test the correct suffix link creation. // maybe do it manually.
    public static void suffixes(Node tree, String path, String s, StringBuilder builder) {
        if (tree == null) {
            // System.out.println(path);
            builder.append(path + "\n");
            return;
        }

        for (Edge e : tree.edges) {
            if (e == null) {
                continue;
            }

            suffixes(e.child, path + "/" + s.substring(e.start, e.end.end), s, builder);
        }
    }
}
