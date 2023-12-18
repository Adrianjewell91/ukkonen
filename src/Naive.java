import util.Node;

public class Naive {
    //Naive O n^2 algorithm.
    public static Node[] naive(String str, Node[] tree) {
        for(int i = 0; i<str.length(); i++) {
            int nodeIdx = Character.getNumericValue(str.charAt(i)) % Node.ARRAY_LENGTH;

            if(tree[nodeIdx] == null) {
                tree[nodeIdx] = new Node(str.charAt(i));
            }

            dfs(str, i+1, tree[nodeIdx].children);
        }

        return tree;
    }

    private static void dfs(String str, int i, Node[] node) {
        if (i == str.length()) {
            //Remove this for implicit suffix tree.
            node[Node.ARRAY_LENGTH-1] = new Node('$');
            return;
        }

        int nodeIdx = Character.getNumericValue(str.charAt(i)) % Node.ARRAY_LENGTH;

        if(node[nodeIdx] == null) {
            node[nodeIdx] = new Node(str.charAt(i));
        }

        dfs(str, i+1, node[nodeIdx].children);
    }
}
