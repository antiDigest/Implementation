// Ver 1.0:  Starter code for Indexed heaps

package cs6301.g1025;
import java.util.Comparator;

public class IndexedHeap<T extends Index> extends BinaryHeap<T> {

	/** Build a priority queue with a given array q */
	public IndexedHeap(T[] q, Comparator<T> comp, int n) {
		super(q, comp, n);
	}

	/**
	 * restore heap order property after the priority of x has decreased
	 * 
	 * @throws PQException
	 */
	public void decreaseKey(T x) throws PQException {

		percolateUp(x.getIndex());

	}

	void move(Object[] pq, int i, T x) {
		x.putIndex(i);
		pq[i] = x;

	}

}
