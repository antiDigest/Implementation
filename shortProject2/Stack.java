package shortProject2;

import java.util.NoSuchElementException;

public class Stack<T> {

	T[] arr;
	int cursor = -1;

	// Constructor

	public Stack (int size) {

		arr = (T[])new Object[size];

	}

	// push the elements in to the stack
	public void push(T ob) {
		if (cursor + 1 < arr.length) {
			cursor = cursor + 1;
			arr[cursor] = ob;

		} else {
			throw new IndexOutOfBoundsException("Overflow Exception");
		}
	}

	// pop the elements
	public T pop() {

		if (isEmpty()) {
			throw new NoSuchElementException("Underflow Exception");

		} else {
			T element = arr[cursor];
			cursor--;
			return element;
		}

	}

	// return the current stack size
	public int size() {
		return arr.length;
	}

	// returns true if stack is empty otherwise returns false
	public boolean isEmpty() {

		if (cursor == -1) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		Stack<Integer> s = new Stack<Integer>(5);
		 s.push(new Integer(2));
		 s.push(new Integer(3));
		 s.push(new Integer(5));
		 s.push(new Integer(6));
		 s.push(new Integer(7));
		
		 s.pop();
		 s.pop();
		 s.pop();
		 s.pop();
		 s.pop();
		

	}
}
