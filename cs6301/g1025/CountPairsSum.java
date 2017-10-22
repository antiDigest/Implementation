

package cs6301.g1025;
import java.util.TreeMap;

public class CountPairsSum {

	public static void main(String[] args) {

		int[] a = { 1, 5, 7, -1, 5,9,989};
		howMany(a, 6);

	}

	
	public static void howMany(int[] A, int X) {
		
		int count=0;
		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
        for (int i = 0; i < A.length; i++) {
		count=	map.getOrDefault(A[i],0);
		count=count+1;
		map.put(A[i], count);
		}
        
        int pairs=0;
    
       for (int i=0; i<A.length; i++)
       {
           if(map.get(X-A[i]) != null)
               pairs+= map.get(X-A[i]);

         if (X-A[i] == A[i])
               pairs--;
       }

       
       System.out.println("Total no of pairs : " +pairs/2);
		System.out.println();


	}
}
