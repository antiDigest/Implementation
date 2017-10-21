package cs6301.g1025;

import java.util.Scanner;

public class SplayTree<T extends Comparable<? super T>> extends BST<T> {
    SplayTree() {
        super();
    }


    /**
     * UTILITY FUNCTIONS
     */

    /**
     * Is x contained in tree?
     *
     * @param x value to find
     * @return true if found, else fasle
     */
    public boolean contains(T x) {
        Entry<T> t = find(x);
        boolean result = (t == null) && (t.element.compareTo(x) == 0);
        if (result) splay(t);
        return result;
    }

    /**
     * Add x to tree.
     * If tree contains a node with same key, replace element by x.
     * Returns true if x is a new element added to tree.
     *
     * @param x T type, value to add
     * @return true if added/replaced, false otherwise
     */
    public boolean add(T x) {
        boolean add = super.add(x);
        if (add) {
            Entry<T> t = find(x);
            splay(t);
        }
        return add;
    }

    /**
     * Is there an element that is equal to x in the tree?
     * Element in tree that is equal to x is returned, null otherwise.
     *
     * @param x T type, value to find
     * @return x, if present, else null
     */
    public T get(T x) {
        Entry<T> t = find(x);
        T result = ((t == null) && (t.element.compareTo(x) == 0)) ? (T) t.element : null;
        if (result != null)
            splay(t);
        return result;
    }

    /**
     * EXTRA UTILITY
     */

    /**
     * Returns minimum from the BST
     *
     * @return min value (T)
     */
    public T min() {
        T min = super.min();
        Entry<T> t = find(min);
        splay(t);
        return min;
    }

    /**
     * Returns maximum from the BST
     *
     * @return maximum value (T)
     */
    public T max() {
        T max = super.min();
        Entry<T> t = find(max);
        splay(t);
        return max;
    }

    /**
     * Brings the node t to the root !
     *
     * @param t BST.Entry type, node to be brought to root
     */
    void splay(BST.Entry<T> t) {
        //TODO : Corrections to code, losing pointers

        t = find(t.element);

        Entry<T> parent = (stack.isEmpty() ? null : stack.pop());
        Entry<T> grandparent = (stack.isEmpty() ? null : stack.pop());

        while (parent != null) {
            if (parent.getLeft() == t) {
                root = right(t, parent);
            } else if (parent.getRight() == t) {
                root = left(parent, t);
            } else if(grandparent != null) {
                if (grandparent.getRight() == parent && parent.getRight() == t) {
                    System.out.println("LEFT LEFT");
                    t = leftLeft(grandparent, parent, t);
                } else if (grandparent.getLeft() == parent && parent.getLeft() == t) {
                    System.out.println("RIGHT RIGHT");
                    t = rightRight(t, parent, grandparent);
                } else if (grandparent.getLeft() == parent && parent.getRight() == t) {
                    System.out.println("LEFT RIGHT");
                    t = leftRight(grandparent, parent, t);
                } else if (grandparent.getRight() == parent && parent.getLeft() == t) {
                    System.out.println("RIGHT LEFT");
                    t = rightLeft(grandparent, parent, t);
                }
            }

            parent = (stack.isEmpty() ? null : stack.pop());
            grandparent = (stack.isEmpty() ? null : stack.pop());

        }
    }

    /**
     * ROTATIONS
     */

    Entry<T> right(Entry<T> P, Entry<T> Q) {
        Entry<T> Pright = P.getRight();
        P.right = Q;
        Q.left = Pright;
        return P;
    }

    Entry<T> left(Entry<T> P, Entry<T> Q) {
        Entry<T> Qleft = Q.getLeft();
        Q.left = P;
        P.right = Qleft;
        return Q;
    }

    Entry<T> rightRight(Entry<T> P, Entry<T> Q, Entry<T> R) {
        Entry<T> Pright = P.right;
        Entry<T> Qright = Q.right;
        Q.right = R;
        R.left = Qright;
        P.right = Q;
        Q.left = Pright;
        return P;
    }

    Entry<T> leftLeft(Entry<T> P, Entry<T> Q, Entry<T> R) {
        Entry<T> Qleft = Q.left;
        Entry<T> Rleft = R.left;
        Q.left = P;
        P.right = Qleft;
        R.left = Q;
        Q.right = Rleft;
        return R;
    }

    Entry<T> leftRight(Entry<T> P, Entry<T> R, Entry<T> Q) {
        Entry<T> Qright = Q.getRight();
        Entry<T> Qleft = Q.getLeft();
        Q.left = R;
        Q.right = P;
        P.left = Qright;
        R.right = Qleft;
        return Q;
    }

    Entry<T> rightLeft(Entry<T> P, Entry<T> R, Entry<T> Q) {
        Entry<T> Qright = Q.getRight();
        Entry<T> Qleft = Q.getLeft();
        Q.right = R;
        Q.left = P;
        P.right = Qleft;
        R.left = Qright;
        return Q;
    }

    public static void main(String[] args) {
        SplayTree<Integer> t = new SplayTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if (x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for (int i = 0; i < t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }

    /**
     * CHECK VALIDITY
     */

    boolean isValid(T x) {
        if(super.isValid(this.root))
            return this.isValid(this.root, x);
        return false;
    }

    boolean isValid(Entry<T> node, T x) {
        return node.element==x;
    }


}