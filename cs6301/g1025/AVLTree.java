/**
 * @author Antriksh, Swaroop, Sai Kumar, Gunjan
 * Starter code for AVL Tree
 */

package cs6301.g1025;

import java.util.Scanner;
import java.util.Stack;

import static java.lang.Integer.max;
import static java.lang.Math.abs;

public class AVLTree<T extends Comparable<? super T>> extends BST<T> {
    static class Entry<T> extends BST.Entry<T> {
        int height;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }
    }

    AVLTree() {
//        super();
        root = new Entry<T>(null, null, null);
        size = 0;
    }

    /**
     * find x (or smallest element greater than x) in the tree
     *
     * @param x x to find
     * @return x, or smallest element greater than x
     */
    Entry<T> find(T x) {
        stack = new Stack<>();
        return super.find(this.root, x);
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
        if (root == null) {
            root = new Entry<>(x, null, null);
            size = 1;
            return true;
        }
        Entry<T> t = find(x);
        if (t.element.compareTo(x) == 0) {
            t.element = x; //Replace
        } else if (t.element.compareTo(x) > 0) {
            t.left = new Entry<T>(x, null, null);
        } else {
            t.right = new Entry<T>(x, null, null);
        }
        size++;
//        return true;
        update(); // TODO : check casting here
        return true;
    }

    /**
     * Remove x from tree.
     * Return x if found, otherwise return null
     *
     * @param x value to remove
     * @return x, if found, otherwise null
     */
    public T remove(T x) {
        T result = super.remove(x);
        update(); // TODO : check casting here
        return result;
    }

    /**
     * HEIGHT, DEPTH
     */

    int height(Entry<T> u) {
        // TODO : check casting here
        if (u == null) return -1;
        int lh = u.left.height;
        int rh = u.right.height;
        return 1 + max(lh, rh);
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

    void rightRight(Entry P, Entry Q, Entry R) {
        Entry Pright = (Entry) P.right;
        Entry Qright = (Entry) Q.right;
        Q.right = R;
        R.left = Qright;
        P.right = Q;
        Q.left = Pright;
    }

    void leftLeft(Entry P, Entry Q, Entry R) {
        Entry Qleft = (Entry) Q.left;
        Entry Rleft = (Entry) R.left;
        Q.left = P;
        P.right = Qleft;
        R.left = Q;
        Q.right = Rleft;
    }

    void leftRight(Entry P, Entry Q, Entry R) {
        Entry Qright = (Entry) Q.right;
        Entry Qleft = (Entry) Q.left;
        Q.left = R;
        Q.right = P;
        P.left = Qleft;
        R.right = Qright;
    }

    void rightLeft(Entry P, Entry Q, Entry R) {
        Entry Qright = (Entry) Q.right;
        Entry Qleft = (Entry) Q.left;
        Q.right = R;
        Q.left = P;
        P.right = Qleft;
        R.left = Qright;
    }

    /**
     * UPDATE CALL FOR ROTATIONS
     */

    void update() {
        update(this.root);
    }

    int update(Entry u) {
        // TODO : check casting here
        if (u == null) return -1;
        int lh = u.left.height;
        int rh = u.right.height;

        if (abs(lh - rh) > 1) {
            if (lh > rh && u.left.left.height > u.left.right.height) {
                right(u, (Entry) u.left);
            } else if (rh > lh && u.right.right.height > u.right.left.height) {
                left(u, (Entry) u.right);
            } else if (lh > rh && u.left.right.height > u.left.left.height) {
                rightLeft(u, (Entry) u.left, (Entry) u.left.right);
            } else if (rh > lh && u.right.left.height > u.right.left.height) {
                leftRight(u, (Entry) u.left, (Entry) u.right.left);
            }
        }

        lh = u.left.height = height((Entry) u.left);
        rh = u.right.height = height((Entry) u.right);

        return 1 + max(lh, rh);
    }

    public static void main(String[] args) {
        BST<Integer> t = new BST<>();
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

    void printTree() {
        System.out.print("[" + size + "] ");
        inOrder();
        System.out.print("{" + root.height + "}"); // TODO : check casting here
        System.out.println();
    }

}

