/**
 * @author Antriksh, Gunjan, Swaroop, Sai kumar
 * Binary search tree
 **/

package cs6301.g1025;


import java.util.Iterator;

import java.util.Scanner;
import java.util.Stack;

public class BST<T extends Comparable<? super T>> implements Iterable<T> {
	static class Entry<T> {
		T element;
		Entry<T> left, right;

		Entry(T x, Entry<T> left, Entry<T> right) {
			this.element = x;
			this.left = left;
			this.right = right;
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
	int size;
	Stack<Entry<T>> stack;

	public BST() {
		root = null;
		size = 0;
	}

    /**
     * ITERATOR
     */

    @Override
	public Iterator<T> iterator() {
		return new BSTIterator<>(this);

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
		return (t.element != null) && (t.element.compareTo(x) == 0);

	}

	/**
	 * find x in tree. Returns node where search ends.
	 * 
	 * @param x x to find
	 * @return x.
	 */
	Entry<T> find(T x) {
		stack = new Stack<>();
		stack.push(null);
		return find(root, x);
	}

	/**
	 * Helper function for find function find x in the tree
	 * 
	 * @param t root of tree
	 * @param x x to find
	 * @return x, or smallest element greater than x
	 */
	Entry<T> find(Entry<T> t, T x) {
		if (t.element == null || t.element.compareTo(x) == 0) {
			return t;
		}
		while (true) {
			if (t.element.compareTo(x) > 0) {
				if (t.left == null)
					break;
				stack.push(t);
				t = t.left;
			} else if (t.element.compareTo(x) < 0) {
				if (t.right == null)
					break;
				stack.push(t);
				t = t.right;
			} else {
				break;
			}
		}
		return t;
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
		return ((t.element != null) && (t.element.compareTo(x) == 0)) ? (T) t.element : null;

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
		return addHelper(x, t, new Entry<T>(x, null, null));

	}

	protected boolean addHelper(T x, Entry<T> t, Entry<T> obj) {
		if (t.element.compareTo(x) == 0) {
			t.element = x; // Replace
			return false;
		} else if (t.element.compareTo(x) > 0) {
			t.left = obj;
		} else {
			t.right = obj;
		}
		size++;
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
		if (root == null)
			return null;
		Entry<T> t = find(x);
		if (t.element.compareTo(x) != 0)
			return null;

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
		Entry<T> pt = stack.peek();
		Entry<T> c = t.left == null ? t.right : t.left;
		if (pt == null)
			root = c;

		else if (pt.left == t)
			pt.left = c;
		else
			pt.right = c;
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

	public Comparable[] toArray() {
		Comparable[] arr = new Comparable[size];
		int index = 0;
		inOrder(root, arr, index);
		return arr;

	}

	/**
	 * Storing elements of a tree in an array
	 * 
	 * @param root root of tree
	 * @param arr array, type = Comparable<T>
	 * @param index index value
	 * @return int, index
	 */
	int inOrder(Entry<T> root, Comparable<T>[] arr, int index) {
		if (root != null) {
			index = inOrder(root.left, arr, index);
			arr[index++] = (Comparable<T>) root.element;
			index = inOrder(root.right, arr, index);
		}
		return index;
	}

	public void printTree() {
		System.out.print("[" + size + "]");
		printTree(root);
		System.out.println();
	}

	void printTree(Entry<T> node) {
		if (node != null) {
			printTree(node.left);
			System.out.print(" " + node.element);
			printTree(node.right);
		}
	}

	/**
	 * For testing the code CHECK VALIDITY
	 */

	boolean isValid() {
		return this.isValid(this.root);
	}

	boolean isValid(Entry<T> node) {
		if (node == null)
			return true;
		if (node.isLeaf())
			return true;
		if (node.getLeft() == null && node.getRight().element.compareTo(node.element) > 0)
			return true;
		if (node.getRight() == null && node.getLeft().element.compareTo(node.element) < 0)
			return true;
		if (node.getLeft().element.compareTo(node.element) < 0 && node.getRight().element.compareTo(node.element) > 0)
			return isValid(node.getLeft()) & isValid(node.getRight());

		System.out.println("INVALID BST AT: " + node + " :: " + node.getRight() + "::" + node.getLeft());

		return false;
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
		while (t.left != null) {
			stack.push(t);
			t = t.left;
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
		while (t.right != null) {
			stack.push(t);
			t = t.right;
		}
		return t.element;
	}

}
/*
 * Sample input: 1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0
 * 
 * Output: Add 1 : [1] 1 Add 3 : [2] 1 3 Add 5 : [3] 1 3 5 Add 7 : [4] 1 3 5 7
 * Add 9 : [5] 1 3 5 7 9 Add 2 : [6] 1 2 3 5 7 9 Add 4 : [7] 1 2 3 4 5 7 9 Add 6
 * : [8] 1 2 3 4 5 6 7 9 Add 8 : [9] 1 2 3 4 5 6 7 8 9 Add 10 : [10] 1 2 3 4 5 6
 * 7 8 9 10 Remove -3 : [9] 1 2 4 5 6 7 8 9 10 Remove -6 : [8] 1 2 4 5 7 8 9 10
 * Remove -3 : [8] 1 2 4 5 7 8 9 10 Final: 1 2 4 5 7 8 9 10
 * 
 */
