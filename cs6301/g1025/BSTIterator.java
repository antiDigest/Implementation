package cs6301.g1025;

import java.util.Iterator;
import java.util.Stack;

public class BSTIterator<T> implements Iterator<BST.Entry<T>> {

    BST.Entry<T> root;
    Stack<BST.Entry<T>> stack = new Stack<>();
    BST bstClass;

    BSTIterator(BST newClass) {
        bstClass = newClass;
        root = (BST.Entry<T>)bstClass.root;
        pushAll(root);
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
    public BST.Entry<T> next() {
        BST.Entry<T> result = stack.pop();
        if (result.right != null) {
            pushAll(result.right);
        }
        return result;
    }

    @Override
    public void remove() {

        BST.Entry t = stack.isEmpty() ? null : stack.pop();

        if (t.left == null || t.right == null) {
            bypass(t); // If only one child is left
        } else {
            stack.push(t);
            BST.Entry<T> minRight = next();
            t.element = minRight.element;
            bypass(minRight);
        }
        bstClass.size--;

    }

    /**
     * Helper function for remove()
     * runs when t has only one child
     * replaces t with one of it's child
     *
     * @param t type Entry<T>
     */
    void bypass(BST.Entry<T> t) {
        BST.Entry<T> parent = stack.isEmpty() ? null : stack.peek();
        BST.Entry<T> child = t.getLeft() == null ? t.getRight() : t.getLeft();
        if (parent == null) root = child;
        else if (parent.getLeft() == t) parent.left = child;
        else parent.right = child;
    }

}
