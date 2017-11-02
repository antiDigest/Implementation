// Change this to your group number
package cs6301.g1025;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> implements Iterable<T> {

    int MAX = 33;
    int size;
    int maxLevel;
    Entry<T> head, tail;

    public class Entry<T extends Comparable<? super T>> {
        T element;
        Entry<T>[] next;

        Entry(T element) {
            this.element = element;
            this.next = new Entry[maxLevel];
        }

        private Entry(T element, int arraySize) {
            this.element = element;
            this.next = new Entry[arraySize];
        }

        @Override
        public String toString() {
            if(this.element.equals(null)) return "";
            return this.element + "";
        }
    }

    // Constructor
    public SkipList() {
        maxLevel = 0;
        size = 1;
        head = new Entry<>(null, MAX);
        tail = new Entry<>(null, MAX);

        for (int i=0;i<MAX;i++) {
            head.next[i] = tail;
        }
    }

    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<>();
        for (int i = 0; i < 20; i++) {
            sl.add(i + 1);
        }
        sl.add(100);
        sl.add(30);
        sl.add(50);
        sl.add(25);
        sl.add(37);
        sl.add(35);
        System.out.println(sl);
        System.out.println(sl.ceiling(37));
        System.out.println(sl.floor(35));
        System.out.println(sl.first());
        System.out.println(sl.contains(36));
        System.out.println(sl.contains(37));
        sl.remove(35);
        sl.remove(3);
        sl.remove(5);
        sl.remove(13);
        sl.remove(15);
        System.out.println(sl);
    }

    // Add x to list. If x already exists, replace it. Returns true if new node
    // is added to list
    public boolean add(T x) {
        Entry<T>[] prev = find(x);

        if (!prev[0].next[0].equals(tail) && prev[0].next[0].element.compareTo(x) == 0) {
            prev[0].next[0].element = x;
            return false;
        } else {
            int lev = chooseLevel();
            lev = lev==0 ? 1 : lev;
            Entry<T> n = new Entry<>(x);
            for (int i = 0; i < lev; i++) {
                n.next[i] = prev[i].next[i];
                prev[i].next[i] = n;
            }
            size++;
            return true;
        }
    }

    /**
     * Find smallest element that is greater or equal to x
     * @param x
     * @return T type
     */
    public T ceiling(T x) {
        Entry<T>[] prev = find(x);
        return prev[0].next[0].equals(tail) ? null : prev[0].next[0].element;
    }

    /**
     * Does list contain x ?
     * @param x
     * @return boolean
     */
    public boolean contains(T x) {
        Entry<T>[] prev = find(x);
        return prev[0].next[0].element.compareTo(x) == 0;
    }

    // Return first element of list
    public T first() {
        return head.next[0].equals(tail) ? null : head.next[0].element;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        Entry<T>[] prev = find(x);
        return prev[0].equals(head) ? null : (prev[0].next[0].element == x ? prev[0].next[0].element : prev[0].element);
    }

    // Return element at index n of list. First element is at index 0.
    public T get(int n) {
        return null;
    }

    // Is the list empty?
    public boolean isEmpty() {
        if(head.next[0].equals(tail)) return true;
        return false;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return new SLIterator<T>(this);
    }

    private class SLIterator<E extends Comparable<? super E>> implements Iterator<E> {
        SkipList<E> list;
        Entry<E> cursor, prev;
        boolean ready; // is item ready to be removed?

        SLIterator(SkipList<E> list) {
            this.list = list;
            cursor = (Entry<E>) list.head;
            prev = null;
            ready = false;
        }

        public boolean hasNext() {
            return !cursor.next[0].equals(tail);
        }

        public E next() {
            prev = cursor;
            cursor = cursor.next[0];
            ready = true;
            return cursor.element;
        }

        // Removes the current element (retrieved by the most recent next())
        // Remove can be called only if next has been called and the element has
        // not been removed
        public void remove() {
            if (!ready) {
                throw new NoSuchElementException();
            }
            // Handle case when tail of a list is deleted
            if (!cursor.equals(list.tail) && !cursor.equals(list.head)) {
                prev.next[0] = cursor.next[0];
            }
            cursor = prev;
            ready = false; // Calling remove again without calling next will
            // result in exception thrown
            size--;
        }
    }

    // Return last element of list
    public T last() {
        return null;
    }

    // Reorganize the elements of the list into a perfect skip list
    public void rebuild() {

    }

    // Remove x from list. Removed element is returned. Return null if x not in
    // list
    public T remove(T x) {
        Entry<T>[] prev = find(x);
        Entry<T> n = prev[0].next[0];

        if(n.element.compareTo(x) != 0){
            return null;
        } else {
            for (int i=0;i<=maxLevel;i++){
                if(prev[i].next[i].equals(n)){
                    prev[i].next[i] = n.next[i];
                } else break;
            }
        }

        size--;
        return n.element;
    }

    // Return the number of elements in the list
    public int size() {
        return size;
    }

    /**
     * Choose number of leves for a new node randomly
     * Prob(choosing level i) = 1/2 * Prob(choosing level i − 1)
     *
     * @return level (int)
     */
    private int chooseLevel() {
        Random random = new Random();
        int mask = (1 << maxLevel) - 1;
        int lev = Integer.numberOfTrailingZeros(random.nextInt() & mask);
        if (lev > maxLevel) {
            return ++maxLevel;
        }
        return lev;
    }

    /**
     * HELPER FUNCTION
     */

    /**
     * Finds the greatest element less than or equal to x
     * @param x T type
     * @return
     */
    Entry<T>[] find(T x) {
        Entry<T> p = head;
        Entry<T>[] prev = new Entry[size];

        for (int i = maxLevel; i >= 0; i--) {
            while (!p.next[i].equals(tail) && p.next[i].element.compareTo(x) < 0)
                p = p.next[i];
            prev[i] = p;
        }
        return prev;
    }

    /**
     * Print List elements
     */

    public void printList() {
        Entry<T> p = head;

        System.out.print("[");
        int count = 0;
        while (!p.next[0].equals(tail)) {
            if(count>0) System.out.print(p + ", ");
            p = p.next[0];
            count++;
        }
        System.out.print(p + "]");
        System.out.println();
    }

    /**
     * Returns string containing size and level of the SkipList
     * @return
     */
    @Override
    public String toString() {
        printList();
        return "[" + this.maxLevel + ", " + this.size() + "]";
    }
}
