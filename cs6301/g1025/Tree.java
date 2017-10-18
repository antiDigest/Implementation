/**
 * @author Antriksh, Gunjan, Swaroop, Sai kumar
 * Binary search tree (starter code)
 **/

package cs6301.g1025;

import java.util.Stack;

import static java.lang.Integer.max;

public class Tree<T extends Comparable<? super T>> {

    static class Entry<T> {
        T element;
        Entry<T> left, right;

        Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }

        Entry(T x) {
            this.element = x;
            this.left = null;
            this.right = null;
        }

        @Override
        public String toString() {
            return this.element + "";
        }
    }

    Entry<T> root;
    Stack<Entry<T>> stack;
    int size;

//    BST(){
//        root = null;
//        size = 0;
//    }
//
//    BST(Entry<T> start){
//        root = start;
//    }

    /**
     * ITERATOR
     */

//    @Override
//    public Iterator<Entry> iterator() {
//        return new BSTIterator(this);
//    }

    /**
     * HEIGHT, DEPTH
     */

    int height(Entry<T> u) {
        if (u == null) return -1;
        int lh = height(u.left);
        int rh = height(u.right);
        return 1 + max(lh, rh);
    }

    /**
     * TRAVERSAL
     */

    void traversal() {
        traversal(this.root, 0);
    }

    int traversal(Entry<T> root, int d) {
        if (root != null) {
            int lh = traversal(root.left, d + 1);
            int rh = traversal(root.right, d + 1);
            int h = 1 + max(lh, rh);
            System.out.println(root + " " + d + " " + h);
            return h;
        } else {
            return -1;
        }
    }

    public void printTree() {
        System.out.print("[" + size + "]");
        inOrder();
        System.out.println();
    }

    /**
     * HELPER FUNCTIONS
     */

    Entry<T> getRoot() {
        return root;
    }

    // TODO: Create an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* code to place elements in array here */

        inOrder(root, arr);

        return arr;
    }

    /**
     * TREE TRAVERSALS
     */

    void visit(Entry<T> node) {
        //This function can be made to do anything
        System.out.print(node.element + " ");
    }

    void visit(Entry<T> node, Comparable<T>[] arr) {
        //This function can be made to do anything
        arr[arr.length] = (Comparable<T>) node.element;
    }

    void preOrder() {
        preOrder(this.root);
    }

    void preOrder(Entry<T> root) {
        if (root != null) {
            visit(root); // This function can be made to do anything
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    void inOrder() {
        inOrder(this.root);
    }

    void inOrder(Entry<T> root) {
        if (root != null) {
            inOrder(root.left);
            visit(root); // This function can be made to do anything
            inOrder(root.right);
        }
    }

    void inOrder(Entry<T> root, Comparable<T>[] arr) {
        if (root != null) {
            inOrder(root.left, arr);
            visit(root, arr); // This function can be made to do anything
            inOrder(root.right, arr);
        }
    }

    void postOrder() {
        postOrder(this.root);
    }

    void postOrder(Entry<T> root) {
        if (root != null) {
            preOrder(root.left);
            preOrder(root.right);
            visit(root); // This function can be made to do anything
        }
    }

}
