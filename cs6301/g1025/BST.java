/**
 * @author Antriksh, Gunjan, Swaroop, Sai kumar
 * Binary search tree
 **/

package cs6301.g1025;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class BST<T extends Comparable<? super T>> implements Iterable<BST.Entry<T>> {

    static class Entry<T> {
        T element;
        Entry<T> left, right;

        Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }

        Entry() {
            this.element = null;
        }

        Entry<T> getLeft() {
            return this.left;
        }

        Entry<T> getRight() {
            return this.right;
        }

        boolean isLeaf() {
            return this.left == null && this.right == null;
        }

        @Override
        public String toString() {
            return this.element + "";
        }
    }

    Entry<T> root;
    Stack<Entry<T>> stack;
    int size;

    public BST() {
        root = null;
        size = 0;
    }

    /**
     * ITERATOR
     */

    @Override
    public Iterator<Entry<T>> iterator() {
        return new BSTIterator(this);
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
        return (t == null) && (t.element.compareTo(x) == 0);
    }

    /**
     * find x (or smallest element greater than x) in the tree
     *
     * @param x x to find
     * @return x, or smallest element greater than x
     */
    Entry<T> find(T x) {
        stack = new Stack<>();
        return find(this.root, x);
    }

    /**
     * find x (or smallest element greater than x) in the tree
     *
     * @param root root of tree
     * @param x    x to find
     * @return x, or smallest element greater than x
     */
    Entry<T> find(Entry<T> root, T x) {
        if (root == null || root.element.compareTo(x) == 0) {
            return root;
        }
        while (true) {
            if (root.element.compareTo(x) < 0) {
                if (root.getRight() == null) break;
                stack.push(root);
                root = root.getRight();
            } else if (root.element.compareTo(x) > 0) {
                if (root.getLeft() == null) break;
                stack.push(root);
                root = root.getLeft();
            } else {
                break;
            }
        }
        return root;
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
        return isValid();
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
        return ((t == null) && (t.element.compareTo(x) == 0)) ? (T) t.element : null;
    }

    /**
     * Remove x from tree.
     * Return x if found, otherwise return null
     *
     * @param x value to remove
     * @return x, if found, otherwise null
     */
    public T remove(T x) {
        if (root == null) return null;
        Entry<T> t = find(x);
        if (t.element.compareTo(x) != 0) return null;

        T result = t.element;

        if (t.left == null || t.right == null) {
            bypass(t); // If only one child is left
        } else {
            stack.push(t);
            Entry<T> minRight = find(t.right, t.element);
            t.element = minRight.element;
            bypass(minRight);
        }
        size--;
        return result;
    }

    /**
     * Helper function for remove()
     * runs when t has only one child
     * replaces t with one of it's child
     *
     * @param t type Entry<T>
     */
    void bypass(Entry<T> t) {
        Entry<T> parent = stack.isEmpty() ? null : stack.peek();
        Entry<T> child = t.getLeft() == null ? t.getRight() : t.getLeft();
        if (parent == null) root = child;
        else if (parent.getLeft() == t) parent.left = child;
        else parent.right = child;
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
        stack = new Stack<>();
        Entry<T> t = root;
        while (t.getLeft() != null) {
            stack.push(t);
            t = t.getLeft();
        }
        return t.element;
    }

    /**
     * Returns maximum from the BST
     *
     * @return maximum value (T)
     */
    public T max() {
        stack = new Stack<>();
        Entry<T> t = root;
        while (t.getRight() != null) {
            stack.push(t);
            t = t.getRight();
        }
        return t.element;
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

    /**
     * HELPER FUNCTIONS
     */

    void printTree() {
        System.out.print("[" + size + "] ");
        inOrder();
        System.out.println();
    }

    Entry<T> getRoot() {
        return root;
    }

    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];

	    /* code to place elements in array here */
        int index = 0;
        inOrder(root, arr, index);
        return arr;
    }

    /**
     * TREE TRAVERSALS
     */

//    void visit(Entry<T> node) {
//        //This function can be made to do anything
//        if(node == null)
//            return;
//        System.out.print(node.element + " ");
//    }

    void inOrder() {
        inOrder(this.root);
    }

    void inOrder(Entry<T> root) {
        if (root != null) {
            inOrder(root.getLeft());
            System.out.print(root.element + " "); // This function can be made to do anything
            inOrder(root.getRight());
        }
    }

    /**
     * Storing elements of a tree in an array
     *
     * @param root  root of tree
     * @param arr   array, type = Comparable<T>
     * @param index index value
     * @return int, index
     */
    int inOrder(Entry<T> root, Comparable<T>[] arr, int index) {
        if (root != null) {
            index = inOrder(root.getLeft(), arr, index);
            arr[index++] = (Comparable) root.element;
            index = inOrder(root.getRight(), arr, index);
        }
        return index;
    }

    /**
     * CHECK VALIDITY
     */

    boolean isValid() {
        return this.isValid(this.root);
    }

    boolean isValid(Entry<T> node) {
        if (node == null) return true;
        if (node.isLeaf()) return true;
        if (node.getLeft() == null && node.getRight().element.compareTo(node.element) > 0) return true;
        if (node.getRight() == null && node.getLeft().element.compareTo(node.element) < 0) return true;
        if (node.getLeft().element.compareTo(node.element) < 0
                && node.getRight().element.compareTo(node.element) > 0)
            return isValid(node.getLeft()) & isValid(node.getRight());

        System.out.println("INVALID BST AT: " + node + " :: " + node.getRight() + "::" + node.getLeft());

        return false;
    }

}
