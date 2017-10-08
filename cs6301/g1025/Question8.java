package cs6301.g1025;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;



/*Compare the running times of the following two algorithms for the problem of
   finding the k largest elements of a stream:
   (a) Use Java's priority queue to keep track of the k largest elements seen
   (b) Use your priority queue implementation (problem 5) using the replace()
       operation in that implementation, instead of delete+add to update PQ.
   Try large inputs that are randomly ordered and other inputs in increasing order.
 */
public class Question8 {

	public static void partA() {

		PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
		Random r = new Random(1000000);
		int count = 0;
		int k = 10;
		int obj = 0;

		while (++count < Integer.MAX_VALUE) {
			obj = r.nextInt();
			if (pq.size() >= k) {
				if (obj > pq.peek()) {
					pq.poll();
					pq.add(obj);
				}

			} else {
				pq.add(obj);

			}

		}

		System.out.println("Java PriorityQueue :");

		while (!pq.isEmpty()) {

			System.out.println(pq.poll());
		}

	}

	public static void partB() throws PQException {
		Integer a[] = new Integer[] {};
		int k = 10;
		BinaryHeap<Integer> PQ = new BinaryHeap<Integer>(a, new minPQ(), k);
		Random r = new Random(1000000);
		int count = 0;

		int obj = 0;
		while (++count < Integer.MAX_VALUE) {

			obj = r.nextInt();
			if (PQ.size() >= k) {
				try {
					PQ.replace(obj);
				} catch (PQException e) {

					e.printStackTrace();
				}

			} else {
				PQ.add(obj);

			}

		}
		System.out.println("Implemented PriorityQueue :");
		while (!PQ.isEmpty()) {

			System.out.println(PQ.poll());
		}
	}

	static class minPQ implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			// TODO Auto-generated method stub
			return o1.compareTo(o2);
		}

	}

	public static void main(String[] args) {

		partA();
		System.out.println();

		try {
			partB();
		} catch (PQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
