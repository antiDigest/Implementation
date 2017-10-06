
package cs6301.g1025;
import java.util.Comparator;

public class DriverHeap {

	public static void main(String[] args) throws MyException {
		Integer[] a = new Integer[] { 9, 6, 5, 0, 8, 2, 2, 98, -1 };

		BinaryHeap<Integer> h = new BinaryHeap<Integer>(a, new maxPQ(), 25);

		// h.add(9);

		h.buildHeap();

		h.remove();
		h.remove();
		h.remove();
		h.insert(98);
		BinaryHeap.heapSort(a, new maxPQ());
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);

	}

	static class minPQ implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			// TODO Auto-generated method stub
			return o1.compareTo(o2);
		}

	}

	static class maxPQ implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			// TODO Auto-generated method stub
			return o2.compareTo(o1);
		}

	}

}
