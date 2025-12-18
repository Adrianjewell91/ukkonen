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
        System.out.println("Logs for string: " + s5);
        List<String>  logs = new ArrayList<>();
        build(s5, true, logs);

        System.out.println(String.join("\n", logs));
    }

    /*
     * An implementation of Ukkonen's Algorithm.
     * 
     * Overview: Build a suffix tree in O(N) time by optimizing a naive O(N^^3)
     * algorithm.
     *
     * The naive O(N^^3) algorithm:
     
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
        int localCounter = 0;
        int gblCounter = 0;
        Node currentNode = root;
        Edge currentEdge = null;
        Node lastCreatedInternalNode = null;
        
        for (int i = 0; i < s.length(); i++) {
            
            char c = s.charAt(i);
            // System.out.println("LastcreatedinternalNode: " + String.valueOf(lastCreatedInternalNode == null));

            if (isDebug == true)
            {
                logs.add("Inserting char " + c + ".");
            }

            /*
            Option 1: 
            
            Reach a branch point while traversing the string (due to repeats).
            */
            if (currentEdge != null
                    &&
                    c != s.charAt(localCounter + currentEdge.start)) {

                int local = localCounter;

                while (gblCounter > 0) {
                    // System.out.println(gblCounter);
                    /*
                    Skip-jump down the string and nodes, if necessary. 
                    It happens when the gblCounter indicates that to continue the branching, one much skip down the string to a lower node.
                    */
                    if (currentNode.isRoot) {
                        local = gblCounter;
                        currentEdge = currentNode.edges[s.charAt(i - local) - 'a'];

                        while (local > (currentEdge.end.end - currentEdge.start)) {
                            local -= currentEdge.end.end - currentEdge.start;
                            currentNode = currentEdge.child;
                            currentEdge = currentNode.edges[s.charAt(i - local) - 'a'];
                        }
                    } else {
                        currentEdge = currentNode.edges[s.charAt(i - local) - 'a'];
                    }

                    /*
                    Create the new branch point, which == a new internal node, and copying over children from the existing edge.
                    */
                    Node internalNode = new Node();
                    Edge split = new Edge(currentEdge.start + local, currentEdge.end);
                    Edge newEdge      = new Edge(i, globalEnd);
                    
                    internalNode.edges[s.charAt(
                            currentEdge.start
                                    +
                                    local)
                            -
                            'a'] = split;
                    internalNode.edges[c - 'a'] = newEdge;
                    internalNode.suffixLink = root;

                    split.child = currentEdge.child;
                    
                    currentEdge.child = internalNode;
                    currentEdge.end = new End(currentEdge.start + local);

                    /*
                    Create the suffix link.
                    */
                    if (lastCreatedInternalNode == null) {
                        lastCreatedInternalNode = internalNode;
                    } else {
                        lastCreatedInternalNode.suffixLink = internalNode;
                        lastCreatedInternalNode = internalNode;
                    }

                    /*
                    nb. 
                    Reset the last created internal node if the traversal reaches the root, because each link of suffixes must terminate at the root, which means traversing down from the root starts a "new" set of links.
                    */
                    if (currentNode.suffixLink.isRoot
                            &&
                            !currentNode.isRoot) {
                        lastCreatedInternalNode = null;
                    }

                    /*
                    Traverse the suffix link.
                    */
                    currentNode = currentNode.suffixLink;

                    if (isDebug == true)
                    {
                        logs.add("Traversed to a Suffix Link. Is it root: " + String.valueOf(currentNode.isRoot));
                    }
                    
                    gblCounter--;
                }

                /*
                Create the new node at the root for the new character.
                */
                // technically duplicate of line ~224
                Edge e = new Edge(i, globalEnd);
                currentNode.edges[c - 'a'] = e;

                /*
                Reset everything. 

                This is essentially an implementation complexity. 
                */
                lastCreatedInternalNode = null;
                currentEdge = null;
                localCounter = 0;
            } 
            /*
            Option 2: 
            
            Find the next character already exists while traversing the string (due to repeats).
            */
            else if (currentEdge != null
                    &&
                    c == s.charAt(localCounter + currentEdge.start)) {
                if (localCounter + currentEdge.start == currentEdge.end.end) {
                    currentNode = currentEdge.child;
                    currentEdge = currentNode.edges[c - 'a'];
                    localCounter = 1;
                } else {
                    localCounter++;
                }

                gblCounter++;
            } 
            /*
            Option 3: 
            
            There is no edge for the character.
            */
            else if (currentNode.edges[c - 'a'] == null) {
                Edge e = new Edge(i, globalEnd);
                currentNode.edges[c - 'a'] = e;
            } 
            /*
            Option 4: 
            
            Start traversing the edge for that character since it is there already.
            */
            else {
                currentEdge = currentNode.edges[c - 'a'];
                localCounter = 1;

                gblCounter++;
            }

            globalEnd.end = i + 1;
        }

        return root;
    }

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
