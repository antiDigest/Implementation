/**
 * Class that implements sorting methods 
 *  @author Swaroop Pydisetty
 *  @contribution Sai Kumar Suvanam
 *  Ver 1.0: 2017/09/02. 
 *
 */
package cs6301.g25.sp1;

public class Sorting {
	/**
	 * Generic merge sort for generic type which extends comparable interface
	 */
	static <T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp) {
		mergeSort(arr, tmp, 0, arr.length - 1);
	}

	/**
	 * Implementation of merge sort using generic type.
	 */
	static <T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp, int p, int r) {
		if (p >= r)
			return;
		int q = (p + r) >>> 1;
		mergeSort(arr, tmp, p, q);
		mergeSort(arr, tmp, q + 1, r);
		merge(arr, tmp, p, q, r);
//		for (int i = p; i <= r; i++) {
//			tmp[i] = arr[i];
//		}
//		int i = p;
//		int j = q + 1;
//		for (int k = p; k <= r; k++) {
//			if (j > r || (i <= q && tmp[i].compareTo(tmp[j]) < 0)) {
//				arr[k] = tmp[i++];
//			} else {
//				arr[k] = tmp[j++];
//			}
//		}
	}
	
	static  <T extends Comparable<? super T>> void merge(T[] arr, T[] tmp, int p, int q, int r) {
		
		for (int i = p; i <= r; i++) {
			tmp[i] = arr[i];
		}
		int i = p;
		int j = q + 1;
		for (int k = p; k <= r; k++) {
			if (j > r || (i <= q && tmp[i].compareTo(tmp[j]) < 0)) {
				arr[k] = tmp[i++];
			} else {
				arr[k] = tmp[j++];
			}
		}

	}

	static void mergeSort(int[] arr, int[] tmp) {
		mergeSort(arr, tmp, 0, arr.length - 1);
	}

	/**
	 * tmp array is used to store values during the merge operation.
	 */
	static void mergeSort(int[] arr, int[] tmp, int p, int r) {
		if (p >= r)
			return;
		int q = (p + r) >>> 1;
		mergeSort(arr, tmp, p, q);
		mergeSort(arr, tmp, q + 1, r);
		merge(arr, tmp, p, q, r);
	}

	static void merge(int arr[], int tmp[], int p, int q, int r) {
		
		for (int i = p; i <= r; i++) {
			tmp[i] = arr[i];
		}
		int i = p;
		int j = q + 1;
		for (int k = p; k <= r; k++) {
			if (j > r || (i <= q && tmp[i] <= tmp[j])) {
				arr[k] = tmp[i++];
			} else {
				arr[k] = tmp[j++];
			}
		}

	}

	static <T extends Comparable<? super T>> void nSquareSort(T[] arr) {
		int n = arr.length;
		for (int i = 1; i < n; ++i) {
			T elem = arr[i];
			int j = i - 1;

			while (j >= 0 && arr[j].compareTo(elem) > 0) {
				arr[j + 1] = arr[j];
				j = j - 1;
			}
			arr[j + 1] = elem;
		}

	}

}
