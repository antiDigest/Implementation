/**
 * @author Antriksh, Swaroop, Sai Kumar, Gunjan
 * AVL Tree
 */

package cs6301.g1025;


import static java.lang.Math.abs;

import java.util.Scanner;







public class AVLTree<T extends Comparable<? super T>> extends BST<T> {
	static class Entry<T> extends BST.Entry<T> {
		int height;

		Entry(T x, Entry<T> left, Entry<T> right) {
			super(x, left, right);
			height = 0;
		}

		void setHeight(int h) {
			this.height = h;
		}

		int getHeight() {
			return this.height;
		}

		Entry<T> getLeft() {
			return (Entry<T>) this.left;
		}

		Entry<T> getRight() {
			return (Entry<T>) this.right;
		}

		@Override
		public String toString() {
			return this.element + "";
		}

	}

	AVLTree() {
		super();
	}

	/**
	 * Remove x from tree. Return x if found, otherwise return null
	 *
	 * @param x
	 *            value to remove
	 * @return x, if found, otherwise null
	 */
	public T remove(T x) {
		T result = super.remove(x);
		if (result == null)
			return result;
		update();

		return result;
	}

	/**
	 * Add x to tree. If tree contains a node with same key, replace element by
	 * x. Returns true if x is a new element added to tree.
	 * 
	 * @param x
	 *            T type, value to add
	 * @return true if added/replaced, false otherwise
	 */
	public boolean add(T x) {
		Entry<T> obj= new Entry<T>(x, null, null);
		if(root==null){
			return createRoot(obj);
		}
		Entry<T> t = (Entry<T>) find(x);
		boolean res = addHelper(x, t, obj);

		if (res) {
			stack.push(t);
			update();
		}
		return res;

	}

	/**
	 * set the height of the entry
	 * 
	 * @param t
	 *            Entry<T> type
	 * @return void
	 */
	private void setHeight(Entry<T> t) {
		t.height = Math.max(height(t.getLeft()), height(t.getRight())) + 1;
	}

	/**
	 * After rotation, link the stack peek(parent) with the correct child
	 * 
	 * @param head,child
	 *            Entry<T> type
	 * @return void
	 */
	private void linkChild(Entry<T> head, Entry<T> child) {
		Entry<T> parent = (Entry<T>) stack.peek();
		if (parent != null) {
			if (parent.left != null && head.element.compareTo(parent.left.element) == 0) {
				parent.left = child;
			} else if (parent.right != null && head.element.compareTo(parent.right.element) == 0) {
				parent.right = child;
			}
		}
	}

	/**
	 * ROTATIONS
	 */
	private Entry<T> rotateRight(Entry<T> head) {
		Entry<T> headL = head.getLeft();
		Entry<T> headLR = headL.getRight();
		headL.right = head;
		head.left = headLR;
		setHeight(head);
		setHeight(headL);
		linkChild(head, headL);
		return headL;
	}

	private Entry<T> rotateLeft(Entry<T> head) {
		Entry<T> headR = head.getRight();
		Entry<T> headRL = headR.getLeft();
		headR.left = head;
		head.right = headRL;
		setHeight(head);
		setHeight(headR);
		linkChild(head, headR);
		return headR;
	}

	/**
	 * UPDATE CALL FOR ROTATIONS
	 */
	private void update() {

		Entry<T> t = null;
		if (stack.peek() == null) {
			return;
		}
		while (true) {
			t = (Entry<T>) stack.pop();
            setHeight(t);
			int balance = getHeightDiff(t);
			if (balance > 1) {
				int l = getHeightDiff(t.getLeft());
				// LL
				if (l >= 0) {
					t = rotateRight(t);
				}
				// LR
				if (l < 0) {
					t.left = rotateLeft(t.getLeft());
					t = rotateRight(t);
				}
			}
			if (balance < -1) {
				int r = getHeightDiff(t.getRight());
				// RR
				if (r <= 0) {
					t = rotateLeft(t);
				}
				// RL
				if (r > 0) {
					t.right = rotateRight(t.getRight());
					t = rotateLeft(t);
				}
			}

			if (stack.peek() == null) {
				root = t;
				break;
			}

		}

	}

	/**
	 * retuns the height of an entry in the tree
	 * 
	 * @param t
	 *            Entry<T> type
	 * @return int
	 */
	private int height(Entry<T> t) {
		if (t == null)
			return 0;

		return t.getHeight();
	}

	/**
	 * retuns the difference in the height of left child and right child
	 * 
	 * @param t
	 *            Entry<T> type
	 * @return int
	 */

	private int getHeightDiff(Entry<T> t) {
		if (t == null)
			return 0;

		return height(t.getLeft()) - height(t.getRight());
	}

	public static void main(String[] args) {
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
	
	void preorder(Entry<Integer> node) {
		if (node != null) {
			System.out.print(" " + node.element);
			preorder((Entry<Integer>)node.left);
			
			preorder((Entry<Integer>)node.right);
		}
	}
	boolean isValid(Entry<T> node) {
        if (node == null) return true;
        int rh = height((Entry<T>)node.right);
        int lh = height((Entry<T>)node.left);
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
                node.getRight() + "[" + height((Entry<T>)node.right) + "]::" + node.getLeft() + "[" + height((Entry<T>)node.right)+ "]");
        return false;
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
