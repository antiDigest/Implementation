package shortProject1;

public class insertionSort {

	public static void main(String[] args) {
		Integer[] arr = new Integer[] { -1, 0, 1, 6, 4, 3, 8 };
		nSquareSort(arr);
		for (int i = 0; i < arr.length; i++)
			System.out.println(arr[i]);
	}

	static <T extends Comparable<? super T>> void nSquareSort(T[] arr) {

		int n = arr.length;
		for (int i = 1; i < n; ++i) {
			T o = arr[i];
			int j = i - 1;

			while (j >= 0 && arr[j].compareTo(o) > 0) {
				arr[j + 1] = arr[j];
				j = j - 1;
			}
			arr[j + 1] = o;
		}

	}

}
