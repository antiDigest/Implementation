/**
 * @author Saikumar
 * Array Based Queues
 * Ver 1.0: 2017/09/09
 */

package cs6301.g1025;

import cs6301.g1025.utils.SinglyLinkedList;

public class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T> {

    /**
     * Merge procedure for sorting linked list parts
     * Merges "this" list with otherList
     * @param otherList: other list passed as parameter
     */
    void merge(SortableList<T> otherList) {
        Entry<T> tailx = this.head;

        Entry<T> tailCursor = this.head.next;
        Entry<T> otherCursor = otherList.head.next;

        while (tailCursor != null && otherCursor != null) {
            if (tailCursor.element.compareTo(otherCursor.element) <= 0) {
                tailx.next = tailCursor;
                tailx = tailCursor;
                tailCursor = tailCursor.next;

            } else {
                tailx.next = otherCursor;
                tailx = otherCursor;
                otherCursor = otherCursor.next;
            }

        }

        if (tailCursor == null) {
            tailx.next = otherCursor;
        } else {
            tailx.next = tailCursor;
        }
    }

    /**
     * Helper function to find the middle node of the linked list.
     * @return node
     */
    Entry<T> findmid() {

        Entry<T> slowPointer = this.head.next;
        Entry<T> fastPointer = slowPointer.next;
        while (fastPointer != null) {
            fastPointer = fastPointer.next;
            if (fastPointer != null) {
                slowPointer = slowPointer.next;
                fastPointer = fastPointer.next;
            }
        }
        return slowPointer;
    }

    /**
     * Merge Sort procedure to "this" list
     */
    void mergeSort() {
        if (this.head.next == null || this.head.next.next == null) {
            return;
        }

        Entry<T> middleNode = this.findmid();
        Entry<T> nextNode = middleNode.next;
        middleNode.next = null;
        SortableList<T> otherlist = new SortableList<T>();
        otherlist.head.next = nextNode;
        this.mergeSort();
        otherlist.mergeSort();
        this.merge(otherlist);

    }

    /**
     * Calls main mergeSort
     * @param list: list
     */
    public static <T extends Comparable<? super T>> void mergeSort(SortableList<T> list) {
        list.mergeSort();

    }

    public static void main(String[] args) {
        SortableList<Integer> list = new SortableList<Integer>();

        int[] arr = new int[] { 2, 5, 7, 8, -1, 4, 5, -3 };
        for (int i = 0; i < arr.length; i++) {
            list.add(new Integer(arr[i]));
        }
        list.printList();
        mergeSort(list);
        list.printList();

    }
}