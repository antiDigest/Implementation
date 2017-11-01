
// Change this to your group number
package cs6301.g1025;

import cs6301.g00.*;
import java.util.Iterator;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
	
	int size;
	int maxLevel;
	// Constructor
	public SkipList() {
		maxLevel = 0;
		size = 0;
	}
	
	public class Entry{
		int val;
		Entry[] next;
		Entry(int val){
			val = val;
			//next = new Entry[maxLevel];
		}
	}

	// Add x to list. If x already exists, replace it. Returns true if new node
	// is added to list
	public boolean add(T x) {
		return true;
	}

	// Find smallest element that is greater or equal to x
	public T ceiling(T x) {
		return null;
	}

	// Does list contain x?
	public boolean contains(T x) {
		return false;
	}

	// Return first element of list
	public T first() {
		return null;
	}

	// Find largest element that is less than or equal to x
	public T floor(T x) {
		return null;
	}

	// Return element at index n of list. First element is at index 0.
	public T get(int n) {
		return null;
	}

	// Is the list empty?
	public boolean isEmpty() {
		return false;
	}

	// Iterate through the elements of list in sorted order
	public Iterator<T> iterator() {
		return null;
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
		return null;
	}

	// Return the number of elements in the list
	public int size() {
		return 0;
	}
	
	private int chooseLevel(){
		return 0;
	}
	
	public static void main(String[] args){
		
	}
}
