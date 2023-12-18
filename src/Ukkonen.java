import util.Node;

public class Ukkonen {
    // TODO: Optimize This.
    public static Node[] ukkonen(String str, Node[] tree) {
        for(int j = 0; j < str.length(); j++) {

            String subStr = str.substring(0, j+1);

            for (int i = 0; i < subStr.length(); i++) {

                int nodeIdx = Character.getNumericValue(subStr.charAt(i)) % Node.ARRAY_LENGTH;
                if (tree[nodeIdx] == null) {
                    tree[nodeIdx] = new Node(subStr.charAt(i));
                }
                dfs_ukkonen(subStr, i + 1, tree[nodeIdx].children);

            }

        }

        return tree;
    }

    public static void dfs_ukkonen(String str, int i, Node[] node) {
        if (i == str.length()) {
            // No terminal character for the implicit suffix tree.
            return;
        }

        int nodeIdx = Character.getNumericValue(str.charAt(i)) % Node.ARRAY_LENGTH;

        if(node[nodeIdx] == null) {
            node[nodeIdx] = new Node(str.charAt(i));
        }

        dfs_ukkonen(str, i+1, node[nodeIdx].children);
    }
}
