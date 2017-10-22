package cs6301.g1025;



import java.util.HashMap;


//Given an array A, return an array B that has those elements of A that
//  occur exactly once, in the same order in which they appear in A:
//  static<T extends Comparable<? super T>> T[] exactlyOnce(T[] A) { // RT = O(nlogn).
//        // Ex: A = {6,3,4,5,3,5}.  exactlyOnce(A) returns {6,4}
//        }

public class ExactlyOnce {

	public static void main(String[] args) {
		Integer[] A = new Integer[] { 0,0,1,2,3,4,0,4 };
		exactlyOnce(A);

	}
	
	/**
	 * find the unique elements
	 */
	static <T extends Comparable<? super T>> T[] exactlyOnce(T[] A) {

		HashMap<T, Integer> map = new HashMap<T, Integer>();

		// size is no of unique elements
		int size = 0;
		for (int i = 0; i < A.length; i++) {
			if (map.containsKey(A[i])) {
				int count = map.get(A[i]);
				count = count + 1;
				if (count <= 2) {
					size--;
				}
				map.put(A[i], count);
			} else {
				size++;
				map.put(A[i], 1);
			}

		}
		
		T[] output = (T[]) new Comparable[size];
		int k = 0;
		for (int i = 0; i < A.length; i++) {
			if (map.get(A[i]) == 1) {
				System.out.println(A[i]);
				output[k++] = A[i];
			}
		}

		return output;
		}

}
