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
		
		int i = 0;
		for (T Obj : q) {
			pq[i++] = Obj;
		}
	
		c = comp;
		heapSize = q.length;

	}

	public void insert(T x) {
		add(x);
	}

	public T deleteMin() throws PQException {
		return remove();
	}

	public T min() throws PQException {
		return peek();
	}

	public void add(T x) { /* TO DO. Throw exception if q is full. */
		if (heapSize >= capacity) {
			try {
				throw new PQException("PriorityQueue is full");
			} catch (PQException exp) {
				System.out.println(exp);
			}
		} else {
			pq[heapSize] = x;
			percolateUp(heapSize);
			heapSize++;
		}

	}

	public T remove()
			throws PQException { /* TO DO. Throw exception if q is empty. */
		if (heapSize <= 0) {
			throw new PQException("PriorityQueue is Empty");

		}
		T min = pq[0];
		move(pq,0,pq[heapSize-1]);
		heapSize--;
		
		percolateDown(0);
		return min;

	}

	public T peek()
			throws PQException { /* TO DO. Throw exception if q is empty. */
		if (heapSize <= 0) {
			throw new PQException("PriorityQueue is Empty");

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
			move(pq,0,x);
			
			percolateDown(0);
		}
	}

	/** pq[i] may violate heap order with parent */
	void percolateUp(int i) { /* to be implemented */
		T elem = pq[i];
		while (i > 0 && this.c.compare(elem, (pq[parent(i)])) < 0) {
			move(pq, i, pq[parent(i)]);
            i = parent(i);
		}
		move(pq,i,elem);
		
	}

	
	void move(Object[] pq, int i, T t) {
    	
		pq[i] = t;
		 

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
			move(pq,i,pq[c]);
			
			i = c;
			c = 2 * i + 1;
		}
		move(pq,i,elem);
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
			throws PQException { /* to be implemented */
	    BinaryHeap<T> h = new BinaryHeap<T>(A, comp, A.length);
	    h.buildHeap();
		for (int i = h.pq.length - 1; i >= 0; i--) {
			 h.move(h.pq,i,h.remove());
			
		}

		for (int i = 0; i < h.pq.length; i++) {
			A[i] = h.pq[i];
		}

	}

	// utility methods
	int parent(int pos) {
		return (pos - 1) / 2;

	}

	boolean isEmpty() {
		if (heapSize <= 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public String toString() {
		String str = "";
		int i = 0;
		while (i < heapSize && pq[i] != null) {
			str += pq[i] + ",";
			i++;
		}
		return str;
	}

}

class PQException extends Exception {
	String str;

	PQException(String str) {
		this.str = str;
	}

	public String toString() {
		return (str);
	}
}
