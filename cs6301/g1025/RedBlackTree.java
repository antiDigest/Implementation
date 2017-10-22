package cs6301.g1025;

import java.util.Scanner;

public class RedBlackTree<T extends Comparable<? super T>> extends BST<T> {
    private static class Entry<T> extends BST.Entry<T> {
        boolean isRed;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            isRed = true;
        }

        void set(T x) {
            this.element = x;
            this.setRed();
        }

        Entry<T> getLeft() {
            return (Entry<T>) this.left;
        }

        Entry<T> getRight() {
            return (Entry<T>) this.right;
        }

        void setBlack() {
            this.isRed = false;
        }

        void setRed() {
            this.isRed = true;
        }

        boolean isRed() {
            return this.isRed;
        }

        boolean isBlack() {
            return (!this.isRed);
        }

        Entry<T> getSibling(Entry<T> t){
            return (this.getLeft() == t ? this.getRight() : this.getLeft());
        }

        boolean childrenBlack(){
            return (this.getLeft()==null || this.getLeft().isBlack())
                    && (this.getRight()==null || this.getRight().isBlack());
        }

        boolean redChild(){
            return (this.getLeft()!=null || this.getLeft().isRed())
                    || (this.getRight()!=null || this.getRight().isRed());
        }
    }

    private RedBlackTree() {
        root = null;
        size = 0;
    }

    /**
     * ROTATIONS
     */

    private Entry<T> right(Entry<T> P, Entry<T> Q) {
        Entry<T> Pright = P.getRight();
        P.right = Q;
        Q.left = Pright;
        linkChild(Q, P);
        return P;
    }

    private Entry<T> left(Entry<T> P, Entry<T> Q) {
        Entry<T> Qleft = Q.getLeft();
        Q.left = P;
        P.right = Qleft;
        linkChild(P, Q);
        return Q;
    }

    /**
     * After rotation, link the stack peek(parent) with the correct child
     *
     * @param head Entry<T> type
     * @param child Entry<T> type
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
        } else {
            root = child;
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
            ((Entry<T>) root).setBlack();
            return true;
        }
        Entry<T> t = (Entry<T>) find(x);
        boolean res = addHelper(x, t, new Entry<T>(x, null, null));

        if(res){
            stack.push(t);
            if(x.compareTo(t.element) > 0)
                t = t.getRight();
            else
                t = t.getLeft();

            repair(t);
            ((Entry<T>) root).setBlack();
        }
        return res;
    }

    private void repair(Entry<T> t) {
        Entry<T> parent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
        Entry<T> grandparent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
        while (t.isRed()) {
            if (grandparent == null) {
                return;
            }
            Entry<T> uncle = grandparent.getSibling(parent);
            if (parent == null || parent == root || parent.isBlack()) {
                return;
            } else if (uncle != null && uncle.isRed()) {
                parent.setBlack();
                uncle.setBlack();
                grandparent.setRed();
                // t <- g_t
                t = grandparent;
                parent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
                grandparent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
            } else if (uncle == null || uncle.isBlack()) {
                if (grandparent.getLeft() == parent && parent.getLeft() == t) {
                    // 2a
                    parent = right(parent, grandparent);
                    parent.setBlack();
                    grandparent.setRed();
                    grandparent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
//                    if(grandparent!=null)
//                        grandparent.left = parent;
//                    else root = parent;
                } else if (grandparent.getRight() == parent && parent.getRight() == t) {
                    // 2b
                    parent = left(grandparent, parent);
                    parent.setBlack();
                    grandparent.setRed();
                    grandparent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
//                    if(grandparent!=null)
//                        grandparent.right = parent;
//                    else root = parent;
                } else if (grandparent.getLeft() == parent && parent.getRight() == t) {
                    // 3a
                    Entry<T> temp = parent;
                    parent = left(parent, t);
                    t = temp;
                    grandparent.left = parent;
                } else if (grandparent.getRight() == parent && parent.getLeft() == t) {
                    // 3b
                    Entry<T> temp = parent;
                    parent = right(t, parent);
                    t = temp;
                    grandparent.right = parent;
                }
            }
        }

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
        Entry<T> t = (Entry<T>) find(x);
        if (t.element.compareTo(x) != 0) {
            return null;
        }

        T result = t.element;

        if (t.left == null || t.right == null) {
            bypass(t); // If only one child is left
        } else {
            stack.push(t);
            Entry<T> minRight = (Entry<T>) find(t.right, t.element);
            t.element = minRight.element;
            bypass(minRight);
        }
        size--;

        System.out.println(isValid());
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
        Entry<T> pt = (Entry<T>) stack.peek();
        Entry<T> c = t.left == null ? t.getRight() : t.getLeft();
        if (pt == null)
            root = c;

        else if (pt.left == t)
            pt.left = c;
        else
            pt.right = c;

        if(t.isBlack())
            fix(c);
    }

    private void fix(Entry<T> t) {
        Entry<T> parent = (stack.isEmpty() ? null : (Entry<T>)stack.pop());
        Entry<T> sibling = parent!=null ? parent.getSibling(t) : null;
        while (t != root && parent != null) {
            if (t != null && t.isRed()) {
                // Case 1
                t.setBlack();
                return;
            } else if (sibling != null && sibling.isBlack() && sibling.childrenBlack()) {
                // Case 2
                sibling.setRed();
                // t <- p_t ??
                t = parent;
                parent = (stack.isEmpty() ? null : (Entry<T>)stack.pop());
                sibling = parent!=null ? parent.getSibling(t) : null;
            } else if (sibling != null && sibling.isBlack() && sibling.redChild()) {
                if (sibling.getRight() != null && parent.getRight() == sibling && sibling.getRight().isRed()) {
                    // Case 3
                    parent = right(parent, sibling);
                    exchangeColor(parent, sibling);
                    sibling.getRight().setBlack();
//                    sibling = parent!=null ? parent.getSibling(t) : null;
                    return;
                } else if (sibling.getLeft() != null && parent.getLeft() == sibling && sibling.getLeft().isRed()) {
                    // Case 3
                    parent = left(sibling, parent);
                    exchangeColor(parent, sibling);
                    sibling.getLeft().setBlack();
//                    sibling = parent!=null ? parent.getSibling(t) : null;
                    return;
                } else if (sibling.getLeft() != null && parent.getRight() == sibling && sibling.getLeft().isRed()) {
                    // Case 4
                    sibling = right(sibling, sibling.getLeft());
                    exchangeColor(sibling, sibling.getLeft()); //TODO apply case 3
                } else if (sibling.getRight() != null && parent.getLeft() == sibling && sibling.getRight().isRed()) {
                    // Case 4
                    sibling = left(sibling.getLeft(), sibling);
                    exchangeColor(sibling, sibling.getLeft()); //TODO apply case 3
                }
            } else if (t == null && sibling != null && sibling.isRed()) {
                // Case 5
                if (parent.getLeft() == t)
                    sibling = left(parent, sibling);
                else
                    sibling = right(sibling, parent);
                exchangeColor(parent, sibling);
                parent = sibling;
            }
        }
    }

    private void exchangeColor(Entry a, Entry b) {
        boolean temp = a.isRed;
        a.isRed = b.isRed;
        b.isRed = temp;
    }

    public static void main(String[] args) {
        RedBlackTree<Integer> t = new RedBlackTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                System.out.println(t.isValid());
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
     * CHECKING VALIDITY
     */

    private class Frame {
        boolean flag;
        int bh;
        T min;
        T max;

        Frame(boolean flag, int bh, T min, T max) {
            this.flag = flag;
            this.bh = bh;
            this.min = min;
            this.max = max;
        }

    }

    boolean isValid() {
        if(this.root==null) return true;
        Frame frame = isValid((Entry<T>) this.root);
        return frame.flag;
    }

    private Frame isValid(Entry<T> node) {
        if (node.getRight() == null && node.getLeft() == null) {
            if (node.isRed())
                return new Frame(true, 0, node.element, node.element);
            else
                return new Frame(true, 1, node.element, node.element);
        } else if (node.getLeft() == null) {
            Frame frame = isValid(node.getRight());
            frame.flag = frame.flag && frame.bh == 0
                    && (node.element.compareTo(frame.min) < 0)
                    && node.isBlack();
            frame.bh++;
            frame.min = node.element;
            if (!frame.flag)
                System.out.println("INVALID RB AT: " + node + " :: " + node.isRed + "->" +
                        node.getRight() + "[" + node.getRight().isRed() + "]::" + node.getLeft());
            return frame;
        } else if (node.getRight() == null) {
            Frame frame = isValid(node.getLeft());
            frame.flag = frame.flag && frame.bh == 0
                    && (node.element.compareTo(frame.max) > 0)
                    && node.isBlack();
            frame.bh++;
            frame.max = node.element;
            if (!frame.flag)
                System.out.println("INVALID RB AT: " + node + " :: " + node.isRed + "->" +
                        node.getRight() + "::" + node.getLeft() + "[" + node.getLeft().isRed() + "]");
            return frame;
        } else {
            Frame frame1 = isValid(node.getLeft());
            Frame frame2 = isValid(node.getRight());

            boolean valid = (frame1.flag && frame2.flag)
                    && (frame1.bh == frame2.bh)
                    && (node.element.compareTo(frame1.max) > 0)
                    && (node.element.compareTo(frame2.min) < 0)
                    && (node.getLeft().isRed() ? node.isBlack() : (node.getRight().isRed() ? node.isBlack(): true) );

            if (!valid)
                System.out.println("INVALID RB AT: " + node + " :: " + node.isRed + "->" +
                        node.getRight() + "[" + node.getRight().isRed + "]::" + node.getLeft() + "[" + node.getLeft().isRed + "]");
            return new Frame(valid, frame1.bh + (node.isRed() ? 0 : 1), node.element, node.element);

        }
    }

}

/**
 * TEST CASE: 10 20 30 40 50 1 5 3 2 45 48 44 46 32 35 37 27 25 28
 * 10 20 30 40 50 1 5 3 7 9 11 13 17 19 29
 */
