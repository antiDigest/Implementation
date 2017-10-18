package cs6301.g1025;

import java.util.Iterator;
import java.util.Stack;

public class BSTIterator implements Iterator<BST.Entry> {

    BST.Entry root;
    Stack<BST.Entry> stack = new Stack<>();
    BST bstClass;

    BSTIterator(BST newClass) {
        bstClass = newClass;
        root = bstClass.getRoot();
        pushAll(root);
    }

    void pushAll(BST.Entry root) {
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
    public BST.Entry next() {
        BST.Entry result = stack.pop();
        if (result.right != null) {
            pushAll(result.right);
        }
        return result;
    }

    @Override
    public void remove() {

        BST.Entry result = stack.isEmpty() ? null : stack.pop();
        BST.Entry parent = stack.isEmpty() ? null : stack.peek();
        BST.Entry child = result.left == null ? result.right : result.left;
        if (parent == null) root = child;
        else if (parent.left == result) parent.left = child;
        else parent.right = child;

    }
}
