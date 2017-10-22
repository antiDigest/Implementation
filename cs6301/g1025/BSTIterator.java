package cs6301.g1025;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BSTIterator<T> implements Iterator<T> {

	Stack<BST.Entry<T>> stack = new Stack<BST.Entry<T>>();
	BST bstClass;

	BSTIterator(BST newClass) {
		bstClass = newClass;
		pushAll(bstClass.root);

	}

	void pushAll(BST.Entry<T> root) {
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
