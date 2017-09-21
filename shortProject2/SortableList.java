package shortProject2;
/* Write the Merge sort algorithm that works on linked lists.  This will
be a member function of a linked list class, so that it can work with
the internal details of the class.  The function should use only
O(log n) extra space (mainly for recursion), and not make copies of
elements unnecessarily.  You can start from the SinglyLinkedList class
provided or create your own.

static<T extends Comparable<? super T>> void mergeSort(SortableList<T> list) { ... }

Here is a skeleton of SortableList.java:

public class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T> {
 void merge(SortableList<T> otherList) {  // Merge this list with other list
 }
 void mergeSort() { Sort this list
 }
 public static<T extends Comparable<? super T>> void mergeSort(SortableList<T> list) {
	list.mergeSort();
 }
}
*/

public class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T> {

	void merge(SortableList<T> otherList) { // Merge this list with other list
		// saves the head
		Entry<T> tailx = this.head;

		// for processed
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

	// find the middle of the given list
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
