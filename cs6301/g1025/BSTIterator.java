package cs6301.g1025;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BSTIterator<T extends Comparable<? super T>> implements Iterator<T> {

	private Stack<BST.Entry<T>> stack = new Stack<>();

	BSTIterator(BST newClass) {
		pushAll((BST.Entry<T>) newClass.root);
	}

	private void pushAll(BST.Entry<T> root) {
		stack.push(root);
		while (root.left != null) {
			root = root.left;
			stack.push(root);
		}
	}

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		BST.Entry<T> result = stack.pop();
		if (result.right != null) {
			pushAll(result.right);
		}
		return result.element;
	}

}
