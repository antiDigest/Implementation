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
	 * @param x
	 *            value to find
	 * @return true if found, else fasle
	 */
	public boolean contains(T x) {
		Entry<T> t = find(x);
		boolean result = (t == null) && (t.element.compareTo(x) == 0);
		if (result)
			splay(t);
		return result;
	}

	/**
	 * Add x to tree,and splay at x. If tree contains a node with same key,
	 * replace element by x,and will do splay at x. Returns true if x is a new
	 * element added to tree.
	 *
	 * @param x
	 *            T type, value to add
	 * @return true if added/replaced, false otherwise
	 */
	public boolean add(T x) {
		Entry<T> obj = new Entry<T>(x, null, null);
		if (root == null) {
			return createRoot(obj);
		}
		Entry<T> t = find(x);
		boolean res = addHelper(x, t, obj);
		//if successfull
        if (res) {
			stack.push(t);
            splay(obj);
		} 
        else {
			splay(t);
		}
		return res;

	}

	/**
	 * Is there an element that is equal to x in the tree? Element in tree that
	 * is equal to x is returned, null otherwise.
	 *
	 * @param x
	 *            T type, value to find
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
	 * ROTATIONS
	 */
	private Entry<T> rightRotate(Entry<T> x) {

		Entry<T> y = x.getLeft();
		x.left = y.getRight();
		y.right = x;
		linkChild(x, y);
		return y;

	}

	private Entry<T> leftRotate(Entry<T> x) {
		Entry<T> y = x.getRight();
		x.right = y.getLeft();
		y.left = x;
		linkChild(x, y);
		return y;
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
		if(min!=null){
		splay(stack.pop());}
		return min;
	}

	/**
	 * Returns maximum from the BST
	 * 
	 * @return maximum value (T)
	 */
	public T max() {
		T max = super.min();
		if(max!=null){
		splay(stack.pop());}
		return max;
	}

	/**
	 * Remove x from tree. Return x if found, otherwise return null
	 *
	 * @param x
	 *            value to remove
	 * @return x, if found, otherwise null
	 */
	public T remove(T x) {
		if (root == null)
			return null;
		Entry<T> t = find(x);
		T result=super.removehelp(t , x);
		if(result==null){
		  splay(t);
		}
		else{
			if(!stack.isEmpty()&&stack.peek()!=null)
			   splay(stack.pop());
		}
		return result;
	}

	/**
	 * After rotation, link the stack peek(parent) with the correct child
	 * 
	 * @param head,child
	 *            Entry<T> type
	 * @return void
	 */
	private void linkChild(Entry<T> head, Entry<T> top) {
		if (!stack.isEmpty()) {
			Entry<T> parent = (Entry<T>) stack.peek();
			if (parent != null) {
				if (parent.left != null && head.element.compareTo(parent.left.element) == 0) {
					parent.left = top;
				} else if (parent.right != null && head.element.compareTo(parent.right.element) == 0) {
					parent.right = top;

				}
			}
		}
	}

	/**
	 * compares the two entries and returns boolean
	 * 
	 * @param t1,t2
	 *            Entry<T> type
	 * @return boolean
	 */
	private boolean compare(Entry<T> t1, Entry<T> t2) {

		if (t2 == null || t1 == null) {
			return false;
		} else
			return t1.element.compareTo(t2.element)==0;
	}

	/**
	 * Brings the node t to the root !
	 * Bottom up splaying
	 * @param t
	 *            BST.Entry type, node to be brought to root
	 */
	private void splay(Entry<T> node) {
		if(stack.isEmpty()||stack.peek()==null){
			return;
		}
         
		Entry<T> top = null;
		Entry<T> parent = (stack.isEmpty() ? null : stack.pop());
		Entry<T> grandparent = (stack.isEmpty() ? null : stack.pop());
		
		boolean pcl = compare(node, parent.getLeft());
		boolean pcr = compare(node, parent.getRight());
	    if (grandparent == null) {
			if (pcl) {
				top = rightRotate(parent);
			}
			if (pcr) {
				top = leftRotate(parent);
			}
		}
		else{
			boolean gpr = compare(parent, grandparent.getRight());
			boolean  gpl = compare(parent, grandparent.getLeft());
		    if (pcl) {
				if (gpl) {
					Entry<T> res = rightRotate(grandparent);
                    top = rightRotate(res);
				}
				if (gpr) {
					grandparent.right = rightRotate(parent);
					top = leftRotate(grandparent);
				}
			} else if (pcr) {
				if (gpr) {
					Entry<T> res = leftRotate(grandparent);
                    top = leftRotate(res);
				}
				if (gpl) {
					grandparent.left = leftRotate(parent);
					top = rightRotate(grandparent);
				}
			}

		}
		if (stack.isEmpty() || stack.peek() == null) {
			root = top;
			return;

		} else {
           splay(top);
		}

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

	private boolean isValid(T x) {
		if (super.isValid(this.root))
			return this.isValid(this.root, x);
		return false;
	}

	private boolean isValid(Entry<T> node, T x) {
		return node.element == x;
	}

}

/**
 * 10 20 30 40 50 60 70 1 2 15 16 35 45
 */
