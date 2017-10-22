package cs6301.g1025;






import java.util.TreeMap;


//Given an array A, return an array B that has those elements of A that
//  occur exactly once, in the same order in which they appear in A:
//  static<T extends Comparable<? super T>> T[] exactlyOnce(T[] A) { // RT = O(nlogn).
//        // Ex: A = {6,3,4,5,3,5}.  exactlyOnce(A) returns {6,4}
//        }

public class ExactlyOnce {

	public static void main(String[] args) {
		Integer[] A = new Integer[] { 6,3,4,5,3,5,3,4,-9,0,6,5,2,12,34,9};
		exactlyOnce(A);

	}
	
	/**
	 * find the unique elements
	 */
	static <T extends Comparable<? super T>> T[] exactlyOnce(T[] A) {

		TreeMap<T, Integer> map = new TreeMap<T, Integer>();

		// size is no of unique elements
		int size = 0;
		for (T ele:A) {
			if (map.containsKey(ele)) {
				int count = map.get(ele);
				count=count+1;
				if(count>2) continue;
				else{
					size--;
				}
				map.put(ele, count);
			} else {
				size++;
				map.put(ele, 1);
			}

		}
		
		T[] output = (T[]) new Comparable[size];
		int k = 0;
		for (T ele:A) {
			if (map.get(ele) == 1) {
				System.out.println(ele);
				output[k++] = ele;
			}
		}

		return output;
		}

}
