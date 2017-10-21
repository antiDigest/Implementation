/**
 * @author Antriksh, Swaroop, Sai Kumar, Gunjan
 * Starter code for AVL Tree
 */

package cs6301.g1025;

import java.util.Scanner;

import static java.lang.Math.abs;

public class AVLTree<T extends Comparable<? super T>> extends BST<T> {
    static class Entry<T> extends BST.Entry<T> {
        int height;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }

        Entry() {
            this.element = null;
        }

        Entry<T> getLeft() {
            return (Entry<T>) this.left;
        }

        Entry<T> getRight() {
            return (Entry<T>) this.right;
        }

        int getLeftHeight() {
            if (this.left == null)
                return -1;
            return this.getLeft().height;
        }

        int getRightHeight() {
            if (this.right == null)
                return -1;
            return this.getRight().height;
        }

        void setLeftHeight(int height) {
            if (this.left == null)
                return;
            else if (height > -1)
                this.getLeft().height = height;
        }

        void setRightHeight(int height) {
            if (this.right == null)
                return;
            else if (height > -1)
                this.getRight().height = height;
        }

        void setHeight() {
            this.height = 1 + Integer.max(this.getLeftHeight(), this.getRightHeight());
        }

        @Override
        public String toString() {
            return this.element + "";
        }
    }

    AVLTree() {
        root = null;
        size = 0;
    }

    void relax(Entry<T> t, T x) {
        if (t.element.compareTo(x) == 0) {
            t.element = x; //Replace
        } else if (t.element.compareTo(x) > 0) {
            t.left = new Entry<T>(x, null, null);
            size++;
        } else {
            t.right = new Entry<T>(x, null, null);
            size++;
        }
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
            root = new Entry<T>(x, null, null);
            size = 1;
            return true;
        }
        Entry<T> t = (Entry<T>) find(x);
        relax(t, x);
        return update();
//        return true;
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

    /**
     * UPDATE CALL FOR ROTATIONS
     */

    boolean update() {
        this.root = update((Entry<T>) this.root);
        return isValid();
    }

    Entry<T> update(Entry<T> u) {
        if (u == null) return u;

        u.left = u.left == null ? null : update(u.getLeft());
        u.right = u.right == null ? null : update(u.getRight());

        int lh = u.getLeftHeight();
        int rh = u.getRightHeight();

        if (abs(lh - rh) > 1) {
            if (lh > rh && u.getLeft().getLeftHeight() > u.getLeft().getRightHeight()) {
                u = right(u.getLeft(), u);
            } else if (rh > lh && u.getRight().getRightHeight() > u.getRight().getLeftHeight()) {
                u = left(u, u.getRight());
            } else if (lh > rh && u.getLeft().getRightHeight() > u.getLeft().getLeftHeight()) {
                if(u.getLeft().getRight() != null)
                    u = leftRight(u, u.getLeft(), u.getLeft().getRight());
            } else if (rh > lh && u.getRight().getLeftHeight() > u.getRight().getRightHeight()) {
                if(u.getRight().getLeft() != null)
                    u = rightLeft(u, u.getRight(), u.getRight().getLeft());
            }
        }

        u.setHeight();
        return u;
    }

    public static void main(String[] args) {
        AVLTree<Integer> t = new AVLTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                System.out.println(t.add(x));
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
     * CHECKING VALIDITY
     */

    boolean isValid() {
        return isValid((Entry<T>) this.root);
    }

    boolean isValid(Entry<T> node) {
        if (node == null) return true;
        int rh = node.getRightHeight();
        int lh = node.getLeftHeight();
        if (node.getLeft() == null && node.getRight() == null) return true;
        if (abs(lh - rh) <= 1 && node.height == (1 + Integer.max(lh, rh))) {
            if (node.getLeft() == null && node.getRight().element.compareTo(node.element) > 0)
                return isValid(node.getRight());
            else if (node.getRight() == null && node.getLeft().element.compareTo(node.element) < 0)
                return isValid(node.getLeft());
            else if (node.getLeft().element.compareTo(node.element) < 0
                    && node.getRight().element.compareTo(node.element) > 0)
                return isValid(node.getLeft()) && isValid(node.getRight());
        }
        System.out.println("INVALID AVL AT: " + node + " :: " + node.height + "->" +
                node.getRight() + "[" + node.getRightHeight() + "]::" + node.getLeft() + "[" + node.getRightHeight() + "]");
        return false;
    }

}


/**
 * TESTING INPUT: 10 3 19 23 21 22 5 7 9 -10 -19 -23 0
 */
