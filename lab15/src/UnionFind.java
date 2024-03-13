public class UnionFind {

    /* TODO: Add instance variables here. */
    private int[] parent;


    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        parent = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return -parent[root(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        if (parent[v] < 0) {
            return -parent[v];
        } else {
            return parent[v];
        }
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return root(v1) == root(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v >= parent.length) {
            throw new IllegalArgumentException("out of bounds");
        }

        return root(v);
    }
    private int root(int v) {
        int root = v;
        while (parent[root] >= 0) {
            root = parent[root];
        }

        while (v != root) {
            int next = parent[v];
            parent[v] = root;
            v = next;
        }
        return root;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (!connected(v1, v2)) {
            int root1 = root(v1);
            int root2 = root(v2);

            if (parent[root1] < parent[root2]) {
                parent[root1] += parent[root2];
                parent[root2] = root1;
            } else {
                parent[root2] += parent[root1];
                parent[root1] = root2;
            }
        }
    }
}
