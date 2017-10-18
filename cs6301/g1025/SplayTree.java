package cs6301.g1025;

public class SplayTree<T extends Comparable<? super T>> extends BST<T> {
    SplayTree() {
        super();
    }

    void splay(BST.Entry t) {
        while (t != root) { //TODO check for root ??

            if (root.left == t) {
                right(root, t);
            } else if (root.right == t) {
                left(t, root);
            } else {

            }
        }
    }

    /**
     * ROTATIONS
     */

    void right(Entry P, Entry Q) {
        Entry Pright = (Entry) P.right;
        P.right = Q;
        Q.left = Pright;
    }

    void left(Entry P, Entry Q) {
        Entry Qleft = (Entry) Q.left;
        Q.left = P;
        P.right = Qleft;
    }

}