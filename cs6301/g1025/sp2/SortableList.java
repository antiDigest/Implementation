package cs6301.g1025.sp2;

import cs6301.g00.Shuffle;
import cs6301.g00.SinglyLinkedList;

//import java.util.Iterator;
//import java.util.Scanner;

public class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T> {
	

	void merge(SortableList<T> other) {
		/**
		 *Merge this list with other list
		 *Loop invariant: tc-thisList cursor, oc-otherList cursor
		 *this.head......tc.prev - processed 
		 *other.head.next.....oc.prev - processed
		 *tc, oc - ptrs to next element to be processed
		 */
		
		Entry<T> tailx  = this.head;
		Entry<T> tc = this.head.next;
		Entry<T> oc = other.head.next;

		
		while(tc != null && oc != null){
			if(tc.element.compareTo(oc.element) <= 0){
				tailx.next = tc;
				tc = tc.next;
			} else {
				tailx.next = oc;
				oc = oc.next;
			}
			tailx = tailx.next;
		}
		
		if(tc == null) {
			tailx.next = oc;
			this.tail = other.tail;
		}
		else if(oc == null) tailx.next = tc;
		
	}

	void mergeSort() { // Sort this list
		if(this.head == this.tail || this.size == 1) return;
		mergeSort(this.head.next);
	}
	
	void mergeSort(Entry<T> start){
		if(start == null || start.next == null) return;
		
		Entry<T> walker = start;
		Entry<T> runner = start;
		Entry<T> prev = null;//to preserve the prev to walker node
		/**
		 *To find the middle node of the node 
		 */
		while(runner != null && runner.next != null) {
			prev = walker;
			walker = walker.next;
			runner = runner.next.next;
		}
		prev.next = null;//splitting the list to half
		mergeSort(start);
		mergeSort(walker);
		SortableList<T> l2 = getSortableList(walker);//forming a list of second half
		this.merge(l2);//merging the second half with first half
		return;
		
	}
	
	SortableList<T> getSortableList(Entry<T> e){
		SortableList<T> res = new SortableList<T>();
		while(e != null){
			res.add(e.element);
			e = e.next;
		}
		return res;
	}

	public static <T extends Comparable<? super T>> void mergeSort(SortableList<T> list) {
		list.mergeSort();
	}
	
	public static Integer[] getRandomArray(int size){
		Integer[] arr = new Integer[size];
		for(int i=0; i<size; i++){
			arr[i] = i;
		}
		
		Shuffle.shuffle(arr);
		return arr;
		
	}
	 
	public static void main(String[] args){
		int n = 10;
		if (args.length > 0) {
			n = Integer.parseInt(args[0]);
		}

		SortableList<Integer> lst = new SortableList<>();
		// Integer [] rand = {3,1,2,4,1,3,4,3,2,2,1,4,2,6,5,3,7,5,3,2,1,5,3,2,1,5,3,1,5};
		Integer [] rand = getRandomArray(n);
		for (Integer i : rand) {
			lst.add(i);
		}
		lst.printList();
		lst.mergeSort();
		lst.printList();

	}
}