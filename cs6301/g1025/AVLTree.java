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

        Entry(){
            this.element = null;
        }

        Entry<T> getLeft(){
            return (Entry<T>) this.left;
        }

        Entry<T> getRight(){
            return (Entry<T>) this.right;
        }

        int getLeftHeight(){
            if(this.left == null)
                return -1;
            return this.getLeft().height;
        }

        int getRightHeight(){
            if(this.right == null)
                return -1;
            return this.getRight().height;
        }

        void setLeftHeight(int height){
            if(this.left == null)
                return;
            else if(height > -1)
                this.getLeft().height = height;
        }

        void setRightHeight(int height){
            if(this.right == null)
                return;
            else if(height > -1)
                this.getRight().height = height;
        }

//        int updateHeight(){
//            int lh = this.left == null ? -1 : this.getLeft().updateHeight();
//            int rh = this.right == null ? -1 : this.getRight().updateHeight();
//
//            if (abs(lh - rh) > 1) {
//                if (lh > rh && this.getLeft().getLeftHeight() > this.getLeft().getRightHeight()) {
//                    this = right(this.getLeft(), this);
//                } else if (rh > lh && this.getRight().getRightHeight() > this.getRight().getLeftHeight()) {
//                    AVLTree.left(this, this.getRight());
//                } else if (lh > rh && this.getLeft().getRightHeight() > this.getLeft().getLeftHeight()) {
//                    AVLTree.rightLeft(this, this.getLeft(), this.getLeft().getRight());
//                } else if (rh > lh && this.getRight().getRightHeight() > this.getRight().getLeftHeight()) {
//                    AVLTree.leftRight(this, this.getLeft(), this.getRight().getLeft());
//                }
//            }
//
//            System.out.println(rh + ":" + lh);
//
//            this.setLeftHeight(lh);
//            this.setRightHeight(rh);
//            return 1 + max(lh, rh);
//        }

        @Override
        public String toString() {
            return this.element + "";
        }
    }

    AVLTree() {
//        super();
        root = new Entry<>();
        size = 0;
    }


    /**
     * Add x to tree.
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     * @param x T type, value to add
     * @return true if added/replaced, false otherwise
     */
    public boolean add(T x) {
//        super.add(x);
        if (root.element == null) {
            root = new Entry<>(x, null, null);
            size = 1;
            return true;
        }
        Entry<T> t = (Entry<T>) find(x);
        if (t.element.compareTo(x) == 0) {
            t.element = x; //Replace
        } else if (t.element.compareTo(x) > 0) {
            t.left = new Entry<T>(x, null, null);
            size++;
        } else {
            t.right = new Entry<T>(x, null, null);
            size++;
        }
//        update(); // TODO : check casting here
        System.out.println(update((Entry<T>) this.root));
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
//        update(); // TODO : check casting here
        return result;
    }

    /**
     * ROTATIONS
     */

    Entry<T> right(Entry<T> P, Entry<T> Q) {
        Entry<T> Pright = P.getRight();
        P.right = Q;
        Q.left = Pright;

        System.out.println(P + ":Swapping:with:" + Q);
        return P;
    }

    Entry<T> left(Entry P, Entry Q) {
        Entry Qleft = (Entry) Q.left;
        Q.left = P;
        P.right = Qleft;

        System.out.println(P + ":Swapping:with:" + Q);

        return Q;
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

    static void leftRight(Entry P, Entry Q, Entry R) {
        Entry Qright = (Entry) Q.right;
        Entry Qleft = (Entry) Q.left;
        Q.left = R;
        Q.right = P;
        P.left = Qleft;
        R.right = Qright;
    }

    static void rightLeft(Entry P, Entry Q, Entry R) {
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
        update((Entry<T>) this.root);
    }

    int update(Entry<T> u) {
        if (u == null) return -1;
        int lh = u.left == null ? -1 : update(u.getLeft());
        int rh = u.right == null ? -1 : update(u.getRight());

        if (abs(lh - rh) > 1) {
            if (lh > rh && u.getLeft().getLeftHeight() > u.getLeft().getRightHeight()) {
                u = right(u.getLeft(), u);
                System.out.println("New u: "+u);
            } else if (rh > lh && u.getRight().getRightHeight() > u.getRight().getLeftHeight()) {
                u = left(u, u.getRight());
                System.out.println("New u: "+u);
            } else if (lh > rh && u.getLeft().getRightHeight() > u.getLeft().getLeftHeight()) {
                AVLTree.rightLeft(u, u.getLeft(), u.getLeft().getRight());
            } else if (rh > lh && u.getRight().getRightHeight() > u.getRight().getLeftHeight()) {
                AVLTree.leftRight(u, u.getLeft(), u.getRight().getLeft());
            }
        }

        System.out.println(rh + ":" + lh);

        u.setLeftHeight(lh);
        u.setRightHeight(rh);
        return 1 + max(lh, rh);
    }

    public static void main(String[] args){
        AVLTree<Integer> t = new AVLTree<>();
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

}

