import org.apache.commons.lang3.time.FastDateFormat;

public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given 2-3 TREE. */
    public RedBlackTree(TwoThreeTree<T> tree) {
        Node<T> ttTreeRoot = tree.root;
        root = buildRedBlackTree(ttTreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        }
        if (r.getItemCount() == 1) {
            RBTreeNode<T> node = new RBTreeNode(true, r.getItemAt(0));
            node.left = buildRedBlackTree(r.getChildAt(0));
            node.right = buildRedBlackTree(r.getChildAt(1));
            return node;
        } else {
            RBTreeNode<T> node = new RBTreeNode<>(true, r.getItemAt(1));
            RBTreeNode<T> left = new RBTreeNode<>(false, r.getItemAt(0));
            left.left = buildRedBlackTree(r.getChildAt(0));
            left.right = buildRedBlackTree(r.getChildAt(1));
            node.left = left;
            node.right = buildRedBlackTree(r.getChildAt(2));
            return node;
        }
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        if (node.left == null) {
            return node;
        }
        RBTreeNode<T> n = node.left;
        node.left = n.right;
        n.right = node;
        n.isBlack = n.right.isBlack;
        n.right.isBlack = false;
        return n;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        if (node.right == null) {
            return node;
        }
        RBTreeNode<T> n = node.right;
        node.right = n.left;
        n.left = node;
        n.isBlack = n.left.isBlack;
        n.left.isBlack = false;
        return n;
    }

    public void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    /* Inserts the given node into this Red Black Tree*/
    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        // Insert (return) new red leaf node.
        if (node == null) {
            return new RBTreeNode<>(false, item);
        }

        // Handle normal binary search tree insertion.
        int comp = item.compareTo(node.item);
        if (comp == 0) {
            return node; // do nothing.
        } else if (comp < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }

        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left.left) && isRed(node.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.right) && isRed(node.left)) {
            flipColors(node);
        }

        // handle "middle of three" and "right-leaning red" structures

        // handle "smallest of three" structure

        // handle "largest of three" structure

        // TODO: YOUR CODE HERE
        return node; //fix this return statement
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

}
