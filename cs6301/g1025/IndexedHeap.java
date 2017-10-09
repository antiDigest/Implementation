/******************************************************************************
 *  
 * IndexedHeap is a indexed priority queue which inherits binary heap class
 * 
 * Builds a heap using index of the vertices
 *
 * @author antriksh, swaroop, gunjan, saikumar
 * Ver 1.0: 2017/10/8. Implemented
 *
 ******************************************************************************/

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
