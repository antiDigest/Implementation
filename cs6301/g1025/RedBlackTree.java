package cs6301.g1025;

import java.util.Scanner;

public class RedBlackTree<T extends Comparable<? super T>> extends BST<T> {
    static class Entry<T> extends BST.Entry<T> {
        boolean isRed;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            isRed = true;
        }

        Entry() {
            this.element = null;
            this.isRed = false;
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
    }

    RedBlackTree() {
        root = null;
        size = 0;
    }


    Entry<T> right(Entry<T> P, Entry<T> Q) {
        Entry<T> Pright = P.getRight();
        P.right = Q;
        Q.left = Pright;
        return P;
    }

    Entry<T> left(Entry<T> P, Entry<T> Q) {
        Entry<T> Qleft = Q.getLeft();
        Q.left = P;
        P.right = Qleft;
        return Q;
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
            ((Entry<T>) root).setBlack();
            return true;
        }
        Entry<T> t = (Entry<T>) find(x);
        relax(t, x);
//        t = (Entry<T>) find(x); // TODO : check casting here
        repair(t);
        ((Entry<T>) root).setBlack();
        return true;
    }

    void repair(Entry<T> t) {
        Entry<T> parent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
        Entry<T> grandparent = (stack.isEmpty() ? null : (Entry<T>) stack.pop());
        if (grandparent == null) {
            return;
        }
        Entry<T> uncle = grandparent.getLeft() == parent ? grandparent.getRight() : grandparent.getLeft();
        while (t.isRed()) {
            if (parent == null || parent == root || parent.isBlack()) {
                return;
            } else if (uncle.isRed()) {
                parent.setBlack();
                uncle.setBlack();
                grandparent.setRed();
                // t <- g_t ??
                continue;
            } else if (uncle.isBlack()) {
                if (grandparent.getLeft() == parent && parent.getLeft() == t) {
                    // 2a
                    grandparent = right(grandparent, parent);
                    parent.setBlack();
                    grandparent.setRed();
                    return;
                } else if (grandparent.getRight() == parent && parent.getRight() == t) {
                    // 2b
                    grandparent = left(parent, grandparent);
                    parent.setBlack();
                    grandparent.setRed();
                    return;
                }
            } else if (uncle.isBlack()) {
                if (grandparent.getLeft() == parent && parent.getRight() == t) {
                    // 3a
                    grandparent = left(parent, grandparent);
                    parent = right(parent, t);
                    parent.setBlack();
                    grandparent.setRed();
                    return;
                } else if (grandparent.getRight() == parent && parent.getLeft() == t) {
                    // 3b
                    grandparent = right(grandparent, parent);
                    parent = left(t, parent);
                    parent.setBlack();
                    grandparent.setRed();
                    return;
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
        return super.remove(x);
    }

//    void fix(Entry t) {
//        Entry parent = (Entry) (stack.isEmpty() ? null : stack.pop());
//        Entry sibling = parent != null ? (Entry) (parent.left == t ? parent.right : parent.left) : null;
//        // TODO : check casting here
//        while (parent != null) {
//            if (t.isRed) {
//                // Case 1
//                t.isRed = false;
//                return;
//            } else if (!sibling.isRed && !sibling.left.isRed && !sibling.right.isRed) {
//                // Case 2
//                sibling.isRed = true;
//                // t <- p_t ??
//            } else if (!sibling.isRed) {
//                if (parent.right == sibling && sibling.right.isRed) {
//                    // Case 3
//                    right(parent, sibling);
//                    exchangeColor(parent, sibling);
//                    sibling.right.isRed = false;
//                    return;
//                } else if (parent.left == sibling && sibling.left.isRed) {
//                    // Case 3
//                    left(sibling, parent);
//                    exchangeColor(parent, sibling);
//                    sibling.left.isRed = false;
//                    return;
//                } else if (parent.right == sibling && sibling.left.isRed) {
//                    // Case 4
//                    right(sibling, (Entry) sibling.left);
//                    exchangeColor(sibling, (Entry) sibling.left); //TODO apply case 3
//                    left(sibling, parent);
//                    exchangeColor(parent, sibling);
//                    sibling.left.isRed = false;
//                    return;
//                } else if (parent.left == sibling && sibling.right.isRed) {
//                    // Case 4
//                    left((Entry) sibling.left, sibling);
//                    exchangeColor(sibling, (Entry) sibling.left); //TODO apply case 3
//                    right(parent, sibling);
//                    exchangeColor(parent, sibling);
//                    sibling.left.isRed = false;
//                    return;
//                }
//            } else if (sibling.isRed) {
//                // Case 5
//                if (parent.left == t)
//                    left(sibling, parent);
//                else
//                    right(parent, sibling);
//                exchangeColor(parent, sibling);
//            }
//        }
//    }

    void exchangeColor(Entry a, Entry b) {
        boolean temp = a.isRed;
        a.isRed = b.isRed;
        b.isRed = a.isRed;
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

    class Frame {
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
        Frame frame = isValid((Entry<T>) this.root);
        return frame.flag;
    }

    Frame isValid(Entry<T> node) {
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
                        node.getRight() + "::" + node.getLeft());
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
                        node.getRight() + "[" + node.getRight().isRed + "]::" + node.getLeft() + "[" + node.getLeft().isRed + "]");
            return frame;
        } else {
            Frame frame1 = isValid(node.getLeft());
            Frame frame2 = isValid(node.getRight());

            boolean valid = (frame1.flag && frame2.flag) && (frame1.bh == frame2.bh)
                    && (node.element.compareTo(frame1.max) > 0)
                    && (node.element.compareTo(frame2.min) < 0)
                    && node.isBlack();


            if (!valid)
                System.out.println("INVALID RB AT: " + node + " :: " + node.isRed + "->" +
                        node.getRight() + "[" + node.getRight().isRed + "]::" + node.getLeft() + "[" + node.getLeft().isRed + "]");
            return new Frame(valid, node.isRed() ? 0 : frame1.bh + 1, node.element, node.element);

        }
    }

}


