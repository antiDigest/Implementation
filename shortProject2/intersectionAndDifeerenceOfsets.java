package shortProject2;

import java.util.LinkedList;
import java.util.List;

//// Given two linked lists implementing sorted sets, write
//functions for union, intersection, and set difference of the sets.
public class intersectionAndDifeerenceOfsets {
	public static <T extends Comparable<? super T>> void intersect(List<T> l1, List<T> l2, List<T> outList) {
		// Return elements common to l1 and l2, in sorted order.
		// outList is an empty list created by the calling
		// program and passed as a parameter.
		// Function should be efficient whether the List is
		// implemented using ArrayList or LinkedList.
		// Do not use HashSet/Map or TreeSet/Map or other complex
		// data structures.
		int i = 0;
		int j = 0;
		while (i < l1.size() && j < l2.size()) {
			if (l1.get(i).compareTo(l2.get(j)) == 0) {
				outList.add(l1.get(i));
				i++;
				j++;

			} else if (l1.get(i).compareTo(l2.get(j)) < 0) {
				i++;
			} else {
				j++;
			}
		}

	}

	public static <T extends Comparable<? super T>> void union(List<T> l1, List<T> l2, List<T> outList) {
		// Return the union of l1 and l2, in sorted order.
		// Output is a set, so it should have no duplicates.
		int i = 0;
		int j = 0;
		while (i < l1.size() && j < l2.size()) {
			if (l1.get(i).compareTo(l2.get(j)) == 0) {
				outList.add(l1.get(i));
				i++;
				j++;

			} else if (l1.get(i).compareTo(l2.get(j)) < 0) {
				outList.add(l1.get(i));
				i++;
			} else {
				outList.add(l2.get(j));
				j++;
			}
		}
		if (i < l1.size()) {
			for (int k = i; k < l1.size(); k++) {
				outList.add(l1.get(k));
			}
		}
		if (j < l2.size()) {
			for (int k = j; k < l2.size(); k++) {
				outList.add(l2.get(k));
			}
		}
	}

	public static <T extends Comparable<? super T>> void difference(List<T> l1, List<T> l2, List<T> outList) {
		// Return l1 - l2 (i.e, items in l1 that are not in l2), in sorted
		// order.
		// Output is a set, so it should have no duplicates.
		int i = 0;
		int j = 0;
		while (i < l1.size() && j < l2.size()) {
			if (l1.get(i).compareTo(l2.get(j)) == 0) {
				i++;
				j++;

			} else if (l1.get(i).compareTo(l2.get(j)) < 0) {

				i++;
			} else {

				j++;
			}
			outList.add(l1.get(i));
		}
	}
		
//		Problem 3:[20 points] Extend the "unzip" algorithm discussed in class to
//		   "multiUnzip" on the SinglyLinkedList class:

			   void multiUnzip(LinkedList<Integer> l,int k) {
			   	// Rearrange elements of a singly linked list by chaining
			   	// together elements that are k apart.  k=2 is the unzip
			   	// function discussed in class.  If the list has elements
				// 1..10 in order, after multiUnzip(3), the elements will be
			   	// rearranged as: 1 4 7 10 2 5 8 3 6 9.  Instead if we call
				// multiUnzip(4), the list 1..10 will become 1 5 9 2 6 10 3 7 4 8.
				   
				   
			}
			  
		
		
	}
	
	
	

