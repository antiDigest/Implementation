// Ver 1.0:  Starter code for bounded size Binary Heap implementation

package cs6301.g1025;

import java.util.Arrays;
import java.util.Comparator;

import cs6301.g00.Utils;

public class BinaryHeap<T> {
	T[] pq;
	Comparator<T> c;
	int size;

	/**
	 * Build a priority queue with a given array q, using q[0..n-1]. It is not
	 * necessary that n == q.length. Extra space available can be used to add
	 * new elements.
	 */
	public BinaryHeap(T[] q, Comparator<T> comp, int n) {
		pq = q;
		c = comp;
		size = n;
	}

	public BinaryHeap(T[] q, Comparator<T> comp) {
		pq = q;
		c = comp;
		size = q.length;
	}

	public void insert(T x) throws Exception {
		add(x);
	}

	public T deleteMin() throws Exception {
		return remove();
	}

	public T min() {
		return peek();
	}

	public void add(T x)
			throws Exception { /* TO DO. Throw exception if q is full. */
		if (size == pq.length)// can resize here
			throw new Exception("Priority queue is full");
		pq[size] = x;
		percolateUp(size);
		size++;
	}

	public T remove() throws Exception {
		if (size == 0)
			throw new Exception("Priority queue is empty");
		T min = pq[0];
		pq[0] = pq[--size];
		percolateDown(0);
		return min;
	}

	public T peek() { /* TO DO. Throw exception if q is empty. */
		return pq[0];
	}

	public void replace(T x) {
		/*
		 * TO DO. Replaces root of binary heap by x if x has higher priority
		 * (smaller) than root, and restore heap order. Otherwise do nothing.
		 * This operation is used in finding largest k elements in a stream.
		 */
	}

	/** pq[i] may violate heap order with parent */
	void percolateUp(int i) {
		T x = pq[i];
		while (i > 0 && c.compare(x, pq[parent(i)]) < 0) {
			pq[i] = pq[parent(i)];
			i = parent(i);
		}
		pq[i] = x;
	}

	void move(int i, T x) {
		pq[i] = x;
	}

	int parent(int i) {
		return (i - 1) / 2;
	}

	/** pq[i] may violate heap order with children */
	void percolateDown(int i) {
		T x = pq[i];
		int s = 2 * i + 1;
		while (s <= size - 1) {
			if (s < size - 1 && c.compare(pq[s], pq[s + 1]) > 0)
				s++;
			if (c.compare(x, pq[s]) <= 0)
				break;
			pq[i] = pq[s];
			i = s;
			s = 2 * i + 1;
		}
		pq[i] = x;

	}

	/** Create a heap. Precondition: none. */
	void buildHeap() {
		for (int i = (pq.length - 2) / 2; i >= 0; i--) {
			percolateDown(i);
		}

	}

	/*
	 * sort array A[]. Sorted order depends on comparator used to buid heap. min
	 * heap ==> descending order max heap ==> ascending order
	 */
	public static <T> void heapSort(T[] A, Comparator<T> comp) {
		BinaryHeap<T> pq = new BinaryHeap<T>(A, comp, A.length);
		pq.buildHeap();

		try {
			for (int i = A.length - 1; i >= 0; i--) {
				A[i] = pq.remove();

			}
		} catch (Exception e) {
			System.out.println("Element not found exception");
		}

	}
	
	public void move(T[] pq, int i, T x){
		pq[i] = x;
	}
	
	public boolean isEmpty(){
		return peek() == null;
	}

	public String toString() {
		return Arrays.toString(pq);
	}

	public static void main(String[] args) {

		//Integer[] arr = { 3, 4, 5, 2, 1 };
		/*Natural ordering*/
		Comparator<Integer> MaxComparator = new Comparator<Integer>() {
			@Override // Min Heap will be built 
			public int compare(Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		};
		/*Reverse ordering*/
		Comparator<Integer> MinComparator = new Comparator<Integer>() {
			@Override // Max Heap will be built
			public int compare(Integer i1, Integer i2) {
				return i2.compareTo(i1); 
			}
		};
		Utils util = new Utils();
		Integer[] A = util.getRandomArray(1000);
		heapSort(A, MinComparator);
		System.out.println(Arrays.toString(A));
		heapSort(A, MaxComparator);
		System.out.println(Arrays.toString(A));

	}
}
