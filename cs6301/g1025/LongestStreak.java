package cs6301.g1025;
import java.util.TreeSet;

/*Given an array A of integers, find the length of a longest streak of
   consecutive integers that occur in A (not necessarily contiguously):
   static int longestStreak(int[] A) { // RT = O(nlogn).
     // Ex: A = {1,7,9,4,1,7,4,8,7,1}.  longestStreak(A) return 3,
     //    corresponding to the streak {7,8,9} of consecutive integers
     //    that occur somewhere in A.
   }
 * 
 * 
 */

public class LongestStreak {

	public static void main(String[] args) {
		int[] A = new int[] { 1, 7, 9, 4, 1, 7, 4, 8, 7, 1 };
		
		System.out.println("length of the longestStreak: "+longestStreak(A));
		
	}

	// Result class to store the output and the longest streak starting and ending indices
	static class Result {
		int startIndex;
		public int getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		public int getEndIndex() {
			return endIndex;
		}

		public void setEndIndex(int endIndex) {
			this.endIndex = endIndex;
		}

		public int getLongest() {
			return longest;
		}

		public void setLongest(int longest) {
			this.longest = longest;
		}

		int endIndex;
		int longest;

		Result(int startIndex, int endIndex, int longest) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.longest = longest;
		}
	}

	static int longestStreak(int[] A) {
		TreeSet<Integer> set = new TreeSet<Integer>();
		for (int i = 0; i < A.length; i++) {
			set.add(A[i]);
		}
		Integer[] arr = new Integer[set.size()];
		set.toArray(arr);
        Result r=new Result(0,0,0);
		int startIndex = 0;
		int endIndex = 0;
		for (int i = 1; i <arr.length; i++) {
			if (arr[i - 1] + 1 == arr[i]) {
				endIndex = i;
				if(i==arr.length-1){//end of the array
					updateResult(r,startIndex,endIndex);
				}
			}
			else {
				updateResult(r,startIndex,endIndex);
				startIndex = i;
			}
		}
		 // just to check the longest streak
		System.out.println("Longest streak consecutive elements are:");
		for(int i=r.getStartIndex();i<=r.getEndIndex();i++){
			System.out.print(arr[i]+" ");
		}
		System.out.println();
		return r.getLongest();
		
	}
	
	// update the Result
	static void updateResult(Result r,int startIndex,int endIndex){
		if(r.getLongest()<endIndex - startIndex+1){
			r.setEndIndex(endIndex);
			r.setLongest(endIndex - startIndex+1);
			r.setStartIndex(startIndex);
		}
	}

}
