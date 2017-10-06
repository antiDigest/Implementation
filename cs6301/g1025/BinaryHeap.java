// Ver 1.0:  Starter code for bounded size Binary Heap implementation

package cs6301.g1025;
import java.util.Arrays;

// Ver 1.0:  Starter code for bounded size Binary Heap implementation

import java.util.Comparator;

public class BinaryHeap<T> {
	T[] pq;
	Comparator<T> c;
	int heapSize = 0;
	int capacity = 0;

	/**
	 * Build a priority queue with a given pqay q, using q[0..n-1]. It is not
	 * necessary that n == q.length. Extra space available can be used to add
	 * new elements.
	 */
	public BinaryHeap(T[] q, Comparator<T> comp, int n) {
		capacity = n;
		pq = (T[]) new Object[capacity];
		for (int i = 0; i < q.length; i++) {
			pq[i] = q[i];
		}
		c = comp;
		heapSize = q.length;

	}

	public void insert(T x) {
		add(x);
	}

	public T deleteMin() throws MyException {
		return remove();
	}

	public T min() throws MyException {
		return peek();
	}

	public void add(T x) { /* TO DO. Throw exception if q is full. */
		if (heapSize >= capacity) {
			try {
				throw new MyException("PriorityQueue is full");
			} catch (MyException exp) {
				System.out.println(exp);
			}
		} else {
			pq[heapSize] = x;
			percolateUp(heapSize);
			heapSize++;
		}

	}

	public T remove()
			throws MyException { /* TO DO. Throw exception if q is empty. */
		if (heapSize <= 0) {
			throw new MyException("PriorityQueue is Empty");

		}
		T min = pq[0];
		pq[0] = pq[--heapSize];
		percolateDown(0);
		return min;

	}

	public T peek()
			throws MyException { /* TO DO. Throw exception if q is empty. */
		if (heapSize <= 0) {
			throw new MyException("PriorityQueue is Empty");

		}
		return pq[0];
	}

	public void replace(T x) {
		/*
		 * TO DO. Replaces root of binary heap by x if x has higher priority
		 * (smaller) than root, and restore heap order. Otherwise do nothing.
		 * This operation is used in finding largest k elements in a stream.
		 */
		if (heapSize > 0) {
			pq[0] = x;
			percolateDown(0);
		}
	}

	/** pq[i] may violate heap order with parent */
	void percolateUp(int i) { /* to be implemented */
		T elem = pq[i];
		while (i > 0 && this.c.compare(elem, (pq[parent(i)])) < 0) {
			pq[i] = pq[parent(i)];
			i = parent(i);
		}
		pq[i] = elem;
	}

	/** pq[i] may violate heap order with children */
	void percolateDown(int i) { /* to be implemented */

		T elem = pq[i];
		int c = 2 * i + 1;

		while (c <= heapSize - 1) {
			if (c < heapSize - 1 && this.c.compare(pq[c], (pq[c + 1])) > 0) {
				c++;
			}
			if (this.c.compare(elem, (pq[c])) <= 0) {
				break;
			}
			pq[i] = pq[c];
			i = c;
			c = 2 * i + 1;
		}
		pq[i] = elem;
	}

	/** Create a heap. Precondition: none. */
	void buildHeap() {
		for (int i = (pq.length - 2) / 2; i >= 0; i--) {
			percolateDown(i);
		}
	}

	/*
	 * sort pqay A[]. Sorted order depends on comparator used to buid heap. min
	 * heap ==> descending order max heap ==> ascending order
	 */
	public static <T> void heapSort(T[] A, Comparator<T> comp)
			throws MyException { /* to be implemented */
		BinaryHeap<T> h = new BinaryHeap<T>(A, comp, A.length);
		h.buildHeap();
		for (int i = h.pq.length - 1; i >= 0; i--) {
			h.pq[i] = h.remove();
		}
		for (int i = 0; i < h.pq.length; i++) {
			A[i] = h.pq[i];
		}

	}

	// utility methods
	int parent(int pos) {
		return pos / 2;

	}

	@Override
	public String toString() {
		String str = "";
		int i = 0;
		while (i<heapSize && pq[i] != null) {
			str += pq[i] + "\n";
			i++;
		}
		return str;
	}

}

class MyException extends Exception {
	String str;

	MyException(String str) {
		this.str = str;
	}

	public String toString() {
		return (str);
	}
}

