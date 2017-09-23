package cs6301.g1025;
//Binary search: in class we saw a version of binary search that returned

//a boolean to indicate whether x occurs in the array or not.
//Rewrite the function to return the index of the largest element that
//is less than or equal to x.
//
//// Preconditions: arr[0..n-1] is sorted, and arr[0] <= x < arr[n-1].
//// Returns index i such that arr[i] <= x < arr[i+1].
//public static<T extends Comparable<? super T>> int binarySearch(T[] arr, T x)

public class BinarySearch {

	public static void main(String[] args) {
		Integer[] arr = new Integer[] { -1,2,3,4,9,10,13,56};
		binarySearch(arr, 55);
	}

	public static <T extends Comparable<? super T>> int binarySearch(T[] arr, T x) {
		System.out.println(rebinarySearch(arr, x, 0, arr.length - 1));
		return 0;
	}

	public static <T extends Comparable<? super T>> int rebinarySearch(T[] arr, T x, int start, int end) {
		if (start > end) {
			return -1;
		}
		int mid = (start + end) / 2;
		int r = arr[mid].compareTo(x);
		if (r <= 0) {
			if (mid + 1 <= end && arr[mid + 1].compareTo(x) > 0) {
				return mid;}
			else
				return rebinarySearch(arr, x, mid + 1, end);

		} else {
			if (mid - 1 >= 0 && arr[mid - 1].compareTo(x) <= 0) {
				return mid - 1;
			} else {
				return rebinarySearch(arr, x, start, mid);
			}

		}

	}

}
