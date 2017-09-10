/**
 * Set Operations Using Linked Lists implementing Sorted Sets
 *
 * @author Antriksh
 * Ver 1.0: 2017/09/05
 */

package cs6301.g1025;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SetOperations {
    private static int n = 10;

    /**
     * Return elements common to l1 and l2, in sorted order.
     * outList is an empty list created by the calling
     * program and passed as a parameter.
     * Function should be efficient whether the List is
     * implemented using ArrayList or LinkedList.
     *
     * @param l1:      First list/Set
     * @param l2:      Second List/Set
     * @param outList: Output List
     */
    public static <T extends Comparable<? super T>> void intersect(List<T> l1, List<T> l2, List<T> outList) {
        Iterator<T> it1 = l1.iterator();
        Iterator<T> it2 = l2.iterator();

        T item1 = next(it1);
        T item2 = next(it2);
        while (item1 != null && item2 != null) {
            if (item1.compareTo(item2) < 0) {
                item1 = next(it1);
            } else if (item1.compareTo(item2) > 0) {
                item2 = next(it2);
            } else {
                outList.add(item1);
                item1 = next(it1);
                item2 = next(it2);
            }
        }
    }

    static <T extends Comparable<? super T>> T next(Iterator<T> it) {
        return (it.hasNext() ? it.next() : null);
    }

    /**
     * Union Set Operation
     * Return the union of l1 and l2, in sorted order.
     * Output is a set, so it should have no duplicates.
     *
     * @param l1:      First list/Set
     * @param l2:      Second List/Set
     * @param outList: Output List
     */
    public static <T extends Comparable<? super T>> void union(List<T> l1, List<T> l2, List<T> outList) {
        Iterator<T> it1 = l1.iterator();
        Iterator<T> it2 = l2.iterator();

        T item1 = next(it1);
        T item2 = next(it2);
        while (item1 != null && item2 != null) {
            if (item1.compareTo(item2) < 0) {
                outList.add(item1);
                item1 = next(it1);
            } else if (item1.compareTo(item2) > 0) {
                outList.add(item2);
                item2 = next(it2);
            } else {
                outList.add(item1);
                item1 = next(it1);
                item2 = next(it2);
            }
        }
        while (item1 != null) {
            outList.add(item1);
            item1 = next(it1);
        }
        while (item2 != null) {
            outList.add(item2);
            item2 = next(it2);
        }
    }

    /**
     * Return l1 - l2 (i.e, items in l1 that are not in l2), in sorted order.
     * Output is a set, so it should have no duplicates.
     *
     * @param l1:      First list/Set
     * @param l2:      Second List/Set
     * @param outList: Output List
     */
    public static <T extends Comparable<? super T>> void difference(List<T> l1, List<T> l2, List<T> outList) {
        Iterator<T> it1 = l1.iterator();
        Iterator<T> it2 = l2.iterator();

        T item1 = next(it1);
        T item2 = next(it2);
        while (item1 != null && item2 != null) {
            if (item1.compareTo(item2) < 0) {
                outList.add(item1);
                item1 = next(it1);
            } else if (item1.compareTo(item2) > 0) {
                item2 = next(it2);
            } else {
                item1 = next(it1);
                item2 = next(it2);
            }
        }
        while (item1 != null) {
            outList.add(item1);
            item1 = next(it1);
        }
    }

    public static void main(String[] args) {
        List<Integer> l1 = new LinkedList<Integer>();
        List<Integer> l2 = new LinkedList<Integer>();

        Random rand = new Random();
        int k = rand.nextInt(50) % n;
        for (int i = k; i < n * 2; i++) {
            l1.add(i + 1);
        }
        k = rand.nextInt(30) % n;
        for (int i = k; i < n; i++) {
            l2.add(i + 1);
        }

        System.out.println(l1);
        System.out.println(l2);
        List<Integer> outList = new LinkedList<Integer>();
        difference(l1, l2, outList);
        System.out.println("Difference: " + outList);

        outList = new LinkedList<Integer>();
        union(l1, l2, outList);
        System.out.println("Union: " + outList);

        outList = new LinkedList<Integer>();
        intersect(l1, l2, outList);
        System.out.println("Intersection: " + outList);
    }
}
