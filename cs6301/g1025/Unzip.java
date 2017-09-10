/**
 * @author Saikumar
 * Multi Unzip function
 * Ver 1.0: 2017/09/10
 */

package cs6301.g1025;

import cs6301.g1025.utils.SinglyLinkedList;

public class Unzip<T extends Comparable<? super T>> extends SinglyLinkedList<T> {

    /**
     * Multi Unzip function
     * @param k: number of branches to unzip to
     */
    public void multiUnzip(int k) {

        if (size <= k) {
            return;
        }

        Entry<T> main = this.head;
        Entry<T>[] arr = (Entry<T>[]) new Entry[k];

        Entry<T> c = main.next;
        int l = 0;

        //create  states
        while (c != null && l < k) {
            arr[l] = c;
            c = c.next;
            if (l + 1 == k)
                arr[l].next = null;
            else {
                arr[l].next = c;
            }
            l++;

        }

        Entry<T>[] arr1 = (Entry<T>[]) new Entry[k];
        for (int i = 0; i < arr.length; i++) {
            arr1[i] = arr[i];
        }

        int state = 0;

        while (c != null) {

            arr[state].next = c;
            arr[state] = c;
            c = c.next;
            if (state + 1 != k)
                arr[state].next = arr1[state + 1];

            if (state + 1 == k) {
                arr[state].next = null;
                state = 0;
            } else {
                state = state + 1;
            }
        }
        this.head.next = arr1[0];
    }

    public static void main(String[] args) {
        Unzip<Integer> list = new Unzip<>();

        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < arr.length; i++) {
            list.add(new Integer(arr[i]));
        }
        list.printList();
        list.multiUnzip(4);

        list.printList();

    }

}