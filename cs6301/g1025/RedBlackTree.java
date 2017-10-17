package cs6301.g1025;

public class RedBlackTree<T extends Comparable<? super T>> extends BST<T> {
    static class Entry<T> extends BST.Entry<T> {
        boolean isRed;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            isRed = true;
        }

        Entry(T x) {
            super(x);
            isRed = true;
        }
    }

    RedBlackTree() {
        super();
    }

    void right(Entry P, Entry Q){
        Entry Pright = (Entry) P.right;
        P.right = Q;
        Q.left = Pright;
    }

    void left(Entry P, Entry Q){
        Entry Qleft = (Entry) Q.left;
        Q.left = P;
        P.right = Qleft;
    }

    /**
     * Add x to tree.
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     * @param x T type, value to add
     * @return true if added/replaced, false otherwise
     */
    public boolean add (T x){
        super.add(x);
        Entry t = new Entry(find(x)); // TODO : check casting here
        repair(t);
        return true;
    }

    void repair(Entry t){
        // TODO : check casting here
        Entry parent = (Entry) (stack.isEmpty() ? null : stack.pop());
        Entry grandparent = (Entry) (stack.isEmpty() ? null : stack.pop());
        Entry uncle = null;
        if(grandparent != null)
            uncle = (Entry) ( grandparent.left == parent ? grandparent.right : grandparent.left );
        if(grandparent==null){
            return;
        }
        while(t.isRed){
            if(parent==null || grandparent==null || !parent.isRed){
                return;
            } else if (uncle.isRed){
                parent.isRed = false;
                uncle.isRed = false;
                grandparent.isRed=true;
                // t <- g_t ??
            } else if (!uncle.isRed){
                if (grandparent.left == parent && parent.left == t){
                    // 2a
                    right(grandparent, parent);
                    parent.isRed = false;
                    grandparent.isRed = true;
                    return;
                } else if (grandparent.right == parent && parent.right == t){
                    // 2b
                    left(parent, grandparent);
                    parent.isRed = false;
                    grandparent.isRed = true;
                    return;
                }
            } else if (!uncle.isRed){
                if(grandparent.left == parent && parent.right == t){
                    // 3a
                    left(parent, grandparent);
                    right(grandparent, parent);
                    parent.isRed = false;
                    grandparent.isRed = true;
                    return;
                }
                else if (grandparent.right == parent && parent.left == t){
                    // 3b
                    right(grandparent, parent);
                    left(parent, grandparent);
                    parent.isRed = false;
                    grandparent.isRed = true;
                    return;
                }
            }
        }
    }

    /**
     * Remove x from tree.
     *  Return x if found, otherwise return null
     * @param x value to remove
     * @return x, if found, otherwise null
     */
    public T remove(T x){
        return super.remove(x);
    }

    void fix(Entry t){
        Entry parent = (Entry) (stack.isEmpty() ? null : stack.pop());
        Entry sibling = parent != null ? (Entry)(parent.left == t ? parent.right : parent.left) : null;
        // TODO : check casting here
        while(parent!=null){
            if(t.isRed) {
                // Case 1
                t.isRed = false;
                return;
            } else if (!sibling.isRed && !sibling.left.isRed && !sibling.right.isRed){
                // Case 2
                sibling.isRed = true;
                // t <- p_t ??
            } else if (!sibling.isRed){
                if(parent.right == sibling && sibling.right.isRed){
                    // Case 3
                    right(parent, sibling);
                    exchangeColor(parent, sibling);
                    sibling.right.isRed = false;
                    return;
                } else if (parent.left == sibling && sibling.left.isRed){
                    // Case 3
                    left(sibling, parent);
                    exchangeColor(parent, sibling);
                    sibling.left.isRed = false;
                    return;
                } else if (parent.right == sibling && sibling.left.isRed){
                    // Case 4
                    right(sibling, (Entry)sibling.left);
                    exchangeColor(sibling, (Entry)sibling.left); //TODO apply case 3
                    left(sibling, parent);
                    exchangeColor(parent, sibling);
                    sibling.left.isRed = false;
                    return;
                } else if (parent.left == sibling && sibling.right.isRed){
                    // Case 4
                    left((Entry)sibling.left, sibling);
                    exchangeColor(sibling, (Entry)sibling.left); //TODO apply case 3
                    right(parent, sibling);
                    exchangeColor(parent, sibling);
                    sibling.left.isRed = false;
                    return;
                }
            } else if (sibling.isRed){
                // Case 5
                if (parent.left == t)
                    left(sibling, parent);
                else
                    right(parent, sibling);
                exchangeColor(parent, sibling);
            }
        }
    }

    void exchangeColor(Entry a, Entry b){
        boolean temp = a.isRed;
        a.isRed = b.isRed;
        b.isRed = a.isRed;
    }
}


