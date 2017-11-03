// Change this to your group number
package cs6301.g1025;

import java.lang.reflect.Array;
import java.util.*;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> implements Iterable<T> {

    int MAX = 33;
    int size;
    int maxLevel;
    Entry<T> head, tail;

    /**
     * Constructor
     */
    public SkipList() {
        maxLevel = 0;
        size = 0;
        head = new Entry<>(null, MAX);
        tail = new Entry<>(null, MAX);

        for (int i = 0; i < MAX; i++) {
            head.next[i] = tail;
            head.span[i] = 1;
        }
    }

    /**
     * Class storing information for each element
     * @param <T>
     */
    public class Entry<T extends Comparable<? super T>> {
        T element;
        Entry<T>[] next;
        int[] span;

        Entry(T element) {
            this.element = element;
            this.next = new Entry[maxLevel];
            this.span = new int[maxLevel];
        }

        private Entry(T element, int arraySize) {
            this.element = element;
            this.next = new Entry[arraySize];
            this.span = new int[arraySize];
        }

        int level(){
            return this.span.length;
        }

        @Override
        public String toString() {
//            if (this.element.equals(null)) return "";
            return (this.element==null ? "N" : this.element) + "\t" + Arrays.toString(this.span);
        }
    }

    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<>();
        for (int i = 0; i < 7 * 5; i+=5) {
            sl.add(i + 1);
//            System.out.println("Adding: " + (i + 1));
//            System.out.println(sl);
        }
        for (int i = 7 * 5; i >= 0; i-=7) {
            sl.add(i + 1);
//            System.out.println("Adding: " + (i + 1));
//            System.out.println(sl);
        }
        sl.rebuild();
//        sl.add(100);
//        sl.add(30);
//        sl.add(50);
//        sl.add(25);
//        sl.add(37);
//        sl.add(35);
//        System.out.println(sl);
//        System.out.println(sl.ceiling(37));
//        System.out.println(sl.floor(35));
//        System.out.println(sl.first());
//        System.out.println(sl.contains(36));
//        System.out.println(sl.contains(37));
//        sl.remove(35);
//        sl.remove(3);
//        sl.remove(5);
//        sl.remove(13);
//        sl.remove(15);
//        System.out.println(sl);
    }

    /**
     * Add x to list. If x already exists, replace it. Returns true if new node
     * is added to list
     * @param x
     * @return
     */
    public boolean add(T x) {
        Entry<T>[] prev = find(x);

        if (!prev[0].next[0].equals(tail) && prev[0].next[0].element.compareTo(x) == 0) {
            prev[0].next[0].element = x;
            return false;
        } else {
            int lev = chooseLevel();
            Entry<T> n = new Entry<>(x, lev);
            for (int i = 0; i < lev; i++) {
                n.next[i] = prev[i].next[i];
                prev[i].next[i] = n;
//                n.span[i] = 1;
            }

//            Entry<T> move = n;
//            int backStep = 0;
//            int nextLev = move.level() - 1;
//            if(nextLev > lev){
//                lev = nextLev;
//                backStep = 1;
//            }
//            for (int i = 0; !move.equals(head); i++) {
//                if(!move.equals(prev[i])) {
//                    backStep++;
//                    for (int j = prev[i].level() - 1; j > nextLev; j--) {
//                        if(j >= lev) prev[i].span[j]++;
//                        else {
//                            prev[i].span[j] = backStep;
//                        }
//                    }
//                    move = prev[i];
//                    nextLev = move.level() - 1;
//                    if(nextLev >= lev){
//                        backStep = 0;
//                        lev = nextLev;
////                        n = move;
//                    }
//                }
//            }

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

    /**
     * Return first element of list
     * @return
     */
    public T first() {
        return head.next[0].equals(tail) ? null : head.next[0].element;
    }

    /**
     * Find largest element that is less than or equal to x
     */
    public T floor(T x) {
        Entry<T>[] prev = find(x);
        return prev[0].equals(head) ? null : (prev[0].next[0].element == x ? prev[0].next[0].element : prev[0].element);
    }

    /**
     * Return element at index n of list. First element is at index 0.
     */
    public T get(int n) {
        return null;
    }

    /**
     * Is the list empty?
     */
    public boolean isEmpty() {
        return (head.next[0].equals(tail));
    }

    /**
     * Iterate through the elements of list in sorted order
     */
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

        /**
         * Removes the current element (retrieved by the most recent next())
         * Remove can be called only if next has been called and the element has
         * not been removed
         */
        public void remove() {
            if (!ready) {
                throw new NoSuchElementException();
            }
            /**
             * Handle case when tail of a list is deleted
             */
            if (!cursor.equals(list.tail) && !cursor.equals(list.head)) {
                prev.next[0] = cursor.next[0];
//                for (int i = 0; i < prev.span.length; i++)
//                    prev.span[i]--;
            }
            cursor = prev;
            ready = false; // Calling remove again without calling next will
            // result in exception thrown
            size--;
        }
    }


    /**
     * Return last element of list
     */
    public T last() {
        return null;
    }

    /**
     * Reorganize the elements of the list into a perfect skip list
     */
    public void rebuild() {
        T[] list = this.toArray();
        System.out.println(Arrays.toString(list));
        SkipList<T> skiplist = new SkipList<>();
        int level = (int)Math.ceil(Math.log((double)size() + 1));
        rebuild(list, 0, size(), level, skiplist);
    }

    Entry<T> rebuild(T[] array, int start, int end, int level, SkipList<T> skipList){
        if(start < end){
            int mid = (end - start + 1) / 2;
            Entry<T> midValue = new Entry<>(array[mid], level);
            System.out.println(midValue);
            Entry<T> prev = rebuild(array, start, mid - 1, level - 1, skipList);
            prev.next[0] = midValue;
            midValue.next[0] = rebuild(array, mid + 1, end, level - 1, skipList);
            return midValue;
        } else {
            return new Entry<>(array[start], level);
        }

    }

    T[] toArray(){
        Entry<T> p = head;
        T[] list = (T[]) Array.newInstance(Comparable.class, size());;

        int count = 0;
        while (!p.equals(tail)) {
            if (count > 0) list[count-1] = p.element;
            p = p.next[0];
            count++;
        }

        return list;
    }

    /**
     * Remove x from list. Removed element is returned. Return null if x not in
     * list
     * @param x
     * @return
     */
    public T remove(T x) {
        Entry<T>[] prev = find(x);
        Entry<T> n = prev[0].next[0];

        if (n.element.compareTo(x) != 0) {
            return null;
        } else {
            for (int i = 0; i <= maxLevel; i++) {
                if (prev[i].next[i].equals(n)) {
                    prev[i].next[i] = n.next[i];
//                    prev[i].span[i]--;
                } else break;
            }
        }

        size--;
        return n.element;
    }

    /**
     * Choose number of levels for a new node randomly
     * Prob(choosing level i) = 1/2 * Prob(choosing level i − 1)
     *
     * @return level (int)
     */
    private int chooseLevel() {
        Random random = new Random();
        int mask = (1 << maxLevel) - 1;
        int lev = Integer.numberOfTrailingZeros(random.nextInt() & mask) + 1;
        if (lev > maxLevel) {
            return ++maxLevel;
        }
        return lev;
    }

    /**
     * Print List elements
     */

    public void printList() {
        Entry<T> p = head;

        System.out.print("[");
        int count = 0;
        while (!p.next[0].equals(tail)) {
            if (count > 0) System.out.print(p + ", \n");
            p = p.next[0];
            count++;
        }
        System.out.print(p + "]");
        System.out.println();
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
        Entry<T>[] prev = new Entry[size + 1];

        for (int i = maxLevel; i >= 0; i--) {
            while (!p.next[i].equals(tail) && p.next[i].element.compareTo(x) < 0)
                p = p.next[i];
            prev[i] = p;
        }
        return prev;
    }

    /**
     * Return the number of elements in the list
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Returns string containing size and level of the SkipList
     *
     * @return
     */
    @Override
    public String toString() {
        printList();
        return "[" + this.maxLevel + ", " + this.size() + "]";
    }
}
