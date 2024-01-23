package tree;

public class Builder {
    //Naive O n^2 algorithm.
    public static Node[] naive(String str, Node[] tree) {

        for(int i = 0; i < str.length(); i++) {
            dfs(str, i, tree);
        }

        return tree;
    }

    private static void dfs(String str, int i, Node[] tree) {
        if (i == str.length()) {
            // Set the terminating char:
            tree[Node.ARRAY_LENGTH-1] = new Node('$');
            return;
        }

        int nodeIdx = getNodeIdx(str, i);
        tree[nodeIdx] = getNode(tree, nodeIdx, str, i);

        dfs(str, i+1, tree[nodeIdx].children);
    }

    //starts as O(n^3) but
    // TODO: Optimize it.
    public static Node[] ukkonen(String str, Node[] tree) {

        for(int j = 0; j < str.length(); j++) {
            String subStr = str.substring(0, j+1);

            for (int i = 0; i < subStr.length(); i++) {
                dfs_ukkonen(subStr, i, tree);
            }

        }

        return tree;
    }

    public static void dfs_ukkonen(String str, int i, Node[] node) {
        if (i == str.length()) {
            // No terminating char because it is an implicit suffix tree.
            return;
        }

        int nodeIdx = getNodeIdx(str, i);
        node[nodeIdx] = getNode(node, nodeIdx, str, i);

        dfs_ukkonen(str, i+1, node[nodeIdx].children);
    }

    private static Node getNode(Node[] tree, int nodeIdx, String str, int i) {
        // str.charAt(i) assumes that later on it will match to the index, or something like that.
        // instead should map the index to the character desired charMap[nodeIdx % something] or something like that
        // this is some principle of coding design etc.
        return tree[nodeIdx] == null ? new Node( str.charAt(i) ) : tree[nodeIdx];
    }

    private static int getNodeIdx(String str, int i) {
        // this is correct I think, but not entirely either.
        return Character.getNumericValue( str.charAt(i) ) % Node.ARRAY_LENGTH;
    }

}
