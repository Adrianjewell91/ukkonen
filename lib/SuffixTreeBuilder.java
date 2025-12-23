import java.util.*;

/*
Builds a suffix tree using ukkonen's algorithm.
*/
public class SuffixTreeBuilder {
    /*
     * Represents a substring using a start and end index.
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
     * Represents either the root of the tree or an internal branch point.
     */
    public static class Node {
        public boolean isRoot;
        public Node suffixLink;
        public Edge[] edges = new Edge[27];

        public Node() {
        }
    }

    /*
     * Represents a global pointer to the terminal index.
     * All terminal edges to a single instance of this class.
     */
    public static class End {
        public int end;

        public End(
                int end) {
            this.end = end;
        }
    }

    /*
     * An implementation of Ukkonen's Algorithm.
     * 
     * Overview: Build a suffix tree in O(N) time by optimizing a naive O(N^^3)
     * algorithm.
     *
     * The naive O(N^^3) algorithm:
     * 
     * for each substring (0..i) in string:
     * build suffix tree:
     * for each char in substring:
     * insert into suffix tree;
     * 
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
        int peg = 0;
        Node currentNode = root;
        Edge currentEdge = null;

        /*
         * A requirement to understanding this algorithm is that at each step, the
         * implicit suffix tree exists for 0..i.
         */
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            if (isDebug == true) {
                logs.add("Inserting char " + c + ".");
            }

            /*
             * Two cases:
             * 1. Not traversing an edge (will be guaranteed to be at the root node).
             * 2. Already traversing an edge (would be doing this when repeats start
             * happening).
             */

            /*
             * Case 1.
             */
            if (currentEdge == null) {
                /*
                 * Option 1:
                 * 
                 * There is no edge for the character. aka this character hasn't been seen
                 * before.
                 */
                if (currentNode.edges[c - 'a'] == null) {
                    Edge e = new Edge(i, globalEnd);
                    currentNode.edges[c - 'a'] = e;

                    peg++;
                }
                /*
                 * Option 2:
                 * 
                 * Start traversing the edge for that character since it is there already.
                 */
                else {
                    currentEdge = currentNode.edges[c - 'a'];
                }
            }
            /*
             * Case 2. There is an active edge...
             */
            else {
                /*
                 * Option 1:
                 * 
                 * The next character already exists while traversing the string (due to
                 * repeats).
                 */
                if (c == s.charAt(i - peg)) {
                    if (i - peg == currentEdge.end.end) {
                        currentNode = currentEdge.child;
                        currentEdge = currentNode.edges[c - 'a'];
                    }
                }
                /*
                 * Option 2:
                 * 
                 * It's not the next character therefore reached a branch point while traversing
                 * the string (due to repeats).
                 */
                else {
                    Node lastCreatedInternalNode = null;
                    /*
                    LocalCounter is the distance between the current node and the branch point to be created.

                    It works because the start-end indexes of the the edges being traversed are guaranteed to be contiguous.
                    That is because when a set of edges are traversed, it continues from idx 0 until it reaches a non-matching character.
                    */
                    int localCounter = (i - peg) - currentEdge.start;
                    
                    while (peg < i) {
                        /*
                         * Skip-jump down the string and nodes, if necessary.
                         * It happens when the gblCounter indicates that to continue the branching, one
                         * much skip down the string to a lower node.
                         */
                        if (currentNode.isRoot) {
                            localCounter = i - peg;
                            currentEdge = currentNode.edges[s.charAt(i - localCounter) - 'a'];

                            while (localCounter > (currentEdge.end.end - currentEdge.start)) {
                                localCounter -= currentEdge.end.end - currentEdge.start;
                                currentNode = currentEdge.child;
                                currentEdge = currentNode.edges[s.charAt(i - localCounter) - 'a'];
                            }
                        } else {
                            // current edge.start would here too,
                            /*
                                but i-localCounter also works because: localCounter is the distance from the currentNode,
                                and since i is the current character, it is guaranteed that i-localCounter will be the first 
                                character in the desired edge, and therefore in all the edges to be manipulated during the suffix link traversal.
                            */
                            currentEdge = currentNode.edges[s.charAt(i - localCounter) - 'a'];
                        }

                        /*
                         * Create the new branch point, which == a new internal node, and copying over
                         * children from the existing edge.
                         */
                        Node internalNode = new Node();
                        Edge split = new Edge(currentEdge.start + localCounter, currentEdge.end);
                        Edge newEdge = new Edge(i, globalEnd);

                        internalNode.edges[s.charAt(
                                currentEdge.start
                                        +
                                        localCounter)
                                -
                                'a'] = split;
                        internalNode.edges[c - 'a'] = newEdge;
                        internalNode.suffixLink = root;

                        split.child = currentEdge.child;

                        currentEdge.child = internalNode;
                        currentEdge.end = new End(currentEdge.start + localCounter);

                        /*
                         * Create the suffix link.
                         */
                        if (lastCreatedInternalNode == null) {
                            lastCreatedInternalNode = internalNode;
                        } else {
                            lastCreatedInternalNode.suffixLink = internalNode;
                            lastCreatedInternalNode = internalNode;
                        }

                        /*
                         * nb.
                         * Reset the last created internal node if the traversal reaches the root,
                         * because each link of suffixes must terminate at the root, which means
                         * traversing down from the root starts a "new" set of links.
                         */
                        if (currentNode.suffixLink.isRoot
                                &&
                                !currentNode.isRoot) {
                            lastCreatedInternalNode = null;
                        }

                        /*
                         * Traverse the suffix link.
                         */
                        currentNode = currentNode.suffixLink;

                        if (isDebug == true) {
                            logs.add("Traversed to a Suffix Link. Is it to the root?");
                            logs.add(String.valueOf(currentNode.isRoot));
                        }

                        peg++;
                    }

                    /*
                     * Create the new node at the root for the new character.
                     */
                    // technically duplicate of line ~224
                    Edge e = new Edge(i, globalEnd);
                    currentNode.edges[c - 'a'] = e;

                    /*
                     * Essentially: the next possible peg will be i+1 because at i+1 there might be
                     * a repeat and so the peg indeed gets held at i+1.
                     */
                    peg++;

                    /*
                     * Reset the edge.
                     * 
                     * This is an implementation complexity.
                     */
                    currentEdge = null;
                }
            }

            /*
             * Update the global end, which adds the character to all existing terminal
             * edges.
             */
            globalEnd.end++;
        }

        return root;
    }

    public static void toString(SuffixTreeBuilder.Node tree, String path, String s, StringBuilder builder) {
        if (tree == null) {
            // System.out.println(path);
            builder.append(path + "\n");
            return;
        }

        for (SuffixTreeBuilder.Edge e : tree.edges) {
            if (e == null) {
                continue;
            }

            toString(e.child, path + "/" + s.substring(e.start, e.end.end), s, builder);
        }
    }
}
