package tree;

public class Tree {
    //tree.Naive O n^2 algorithm.
    public static Node[] naive(String str, Node[] tree) {
        for(int i = 0; i<str.length(); i++) {
            int nodeIdx = getNodeIdx(str, i);
            tree[nodeIdx] = getNode(tree, nodeIdx, str, i);

            dfs(str, i+1, tree[nodeIdx].children);
        }

        return tree;
    }

    public static Node[] ukkonen(String str, Node[] tree) {
        for(int j = 0; j < str.length(); j++) {

            String subStr = str.substring(0, j+1);

            for (int i = 0; i < subStr.length(); i++) {

                int nodeIdx = getNodeIdx(str, i);
                tree[nodeIdx] = getNode(tree, nodeIdx, str, i);
                dfs_ukkonen(subStr, i + 1, tree[nodeIdx].children);

            }

        }

        return tree;
    }

    private static Node getNode(Node[] tree, int nodeIdx, String str, int i) {
        return tree[nodeIdx] == null ? new Node(str.charAt(i)) : tree[nodeIdx];
    }

    private static int getNodeIdx(String str, int i) {
        return Character.getNumericValue(str.charAt(i)) % Node.ARRAY_LENGTH;
    }

    private static void dfs(String str, int i, Node[] tree) {
        if (i == str.length()) {
            //Remove this for implicit suffix tree.
            tree[Node.ARRAY_LENGTH-1] = new Node('$');
            return;
        }

        int nodeIdx = getNodeIdx(str, i);
        tree[nodeIdx] = getNode(tree, nodeIdx, str, i);

        dfs(str, i+1, tree[nodeIdx].children);
    }

    public static void dfs_ukkonen(String str, int i, Node[] node) {
        if (i == str.length()) {
            // No terminal character for the implicit suffix tree.
            return;
        }

        int nodeIdx = getNodeIdx(str, i);
        node[nodeIdx] = getNode(node, nodeIdx, str, i);

        dfs_ukkonen(str, i+1, node[nodeIdx].children);
    }
}
