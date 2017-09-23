package cs6301.g1025;
//Reorder an int array A[] by moving negative elements to the front,

//followed by its positive elements.  The relative order of positive numbers
//must be the same as in the given array.  Similarly, the relative order of
//its negative numbers should also be retained.  Write an algorithm that
//runs in O(nlogn), and uses only O(1) extra space (for variables),
//but can use O(log n) space for recursion.

public class ArrangeNegativeAndPositive {

	public static void main(String[] args) {
		int[] arr = new int[] { 4, -1, 2, -2, -9, 0 ,9,0,23,4,-1,4,5,0,98,-90,98,78,65,97,23,45,-2,-8,90,87,90};
		rearrangeMinusPlus(arr);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}

	static void rearrangeMinusPlus(int[] arr) {

		rearrange(arr, 0, arr.length - 1);

	}

	static void rearrange(int[] arr, int start, int end) {

		if (start >= end)
			return;
		int mid = (start + end) >>> 1;
		rearrange(arr, start, mid);
		rearrange(arr, mid + 1, end);
		rearrangeHelper(arr, start, mid, end);

	}

	static void rearrangeHelper(int[] a, int start, int mid, int end) {
		int left = start;
		int right = end;
		//find the first positive index
		while (left <= mid) {
			if (a[left] < 0)
				left++;
			else
				break;
		}
		//find the last negative index
		while (right >= mid + 1) {
			if (a[right] >= 0) {
				right--;
			} else
				break;

		}
		if (left > mid || right < mid + 1) {
			return;
		}

		if (left == mid && right == mid + 1) {
			if (a[left] > 0 && a[right] > 0 || a[left] < 0 && a[right] < 0 || a[left] < 0 && a[right] >= 0) {
				return;
			}
		}

		// right-mid is number of places to shift and right-left+1 is the size
		rotate(a, left, right - mid, right - left + 1);

	}

	public static void rotate(int[] a, int start, int k, int size) {

		reverse(a, start, start + size - 1);
		reverse(a, start, start + k - 1);
		reverse(a, start + k, start + size - 1);
	}

	public static void reverse(int[] a, int start, int end) {
		while (start < end) {
			int temp = a[start];
			a[start] = a[end];
			a[end] = temp;
			start++;
			end--;
		}
	}

}