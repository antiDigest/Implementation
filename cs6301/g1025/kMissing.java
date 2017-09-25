package cs6301.g1025;
//Class to find the k missing numbers
//@author swaroop, Saikumar,Antriksh and Gunjan Tomer -g1025 group



//Given an array of n distinct integers, in sorted order, starting

//at 1 and ending with n+k, find the k missing numbers in the sequence. 
//Your algorithm should run in O(k+log(n)) time.  Note that a simple
//linear scan of the array can find the answer, but it will not meet
//the requirement on the running time.

public class kMissing {

	public static void main(String[] args) {
		int[] arr = new int[] { 1, 2,4,6,8,12};
		int n=arr.length;
		int k=6;//no. of missing numbers
		int totalnumbers=n+k;
		printmissingnumbers(1,arr[0]);
		//find the remaining numbers after finding numbers from 1 to arr[0]
		k=k-(arr[0]-1);
		
		//find the remaining numbers after finding numbers from arr[arr.length-1] to n+k
		k=k-(totalnumbers-(arr[arr.length-1]));
		//now find the numbers which are missing inside the list
		findk(arr,k,0,arr.length-1);
		printmissingnumbers(arr[arr.length-1],totalnumbers);
		
	
		
	}
	
	static void printmissingnumbers(int first,int last){
		for(int i=first+1;i<last;i++){
			System.out.println(i);
		}
	}

	public static void findk(int[] a, int k, int start, int end) {
		if (k == 0||start>end) {
			return;
		}
		int mid = (start + end) / 2;
		if (a[mid] - a[start] == mid - start) {
			if (mid + 1 <= end && a[mid + 1] - a[mid] - 1 != 0) {
				printmissingnumbers(a[mid],a[mid+1]);
				k = k - (a[mid + 1] - a[mid] - 1);
			}
			//find the missing numbers in the other part
			findk(a, k, mid + 1, end);
		} else if (a[mid] - a[start] > mid - start) {
			int firstk = a[mid] - a[start] - (mid - start);
			findk(a, firstk, start, mid);
			//After finding missing numbers in the first part -subtract from k
			k = k - firstk;
			if (mid + 1 <= end && a[mid + 1] - a[mid] - 1 != 0) {
				printmissingnumbers(a[mid],a[mid+1]);
				k = k - (a[mid + 1] - a[mid] - 1);

			}
			//find the missing numbers in the other half
			findk(a, k, mid + 1, end);
		}

	}

}
