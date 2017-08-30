/** Sort class which will contain all sort functions.
 *  @author Antriksh, Gunjan, Atif
 *  Ver 1.0: 2017/08/28
 */

package cs6301.g10;
import java.util.Arrays;

import cs6301.g10.utils.Shuffle;
import cs6301.g10.utils.Timer;

public class Sort{
	/*  Contains Merge Sort in the form of generic and int sorting.
	*	Usage: intMergeSort(A, start, end)
	*			genericMergeSort(A, start, end) -- uses ItemExt class.
	*	
	*	Also contains Insertion Sort as an O(n^2) running algorithm.
	*
	*/
	static int n=6000000;

	public static void intMerge(Integer[] A, int start, int mid, int end){
		int n = end-start+1;
		Integer[] B = new Integer[n];
		Integer[] C = new Integer[n];

		int k=0;
		for(int i=start;i<mid+1;i++){
			B[k] = A[i];
			k++;
		}
		int p = k-1;

		k=0;
		for(int i=mid+1;i<=end;i++){
			C[k] = A[i];
			k++;
		}
		int q = k-1;

		int i=start, b=0, c=0;
		while(b<=p && c<=q){
			if(B[b] > C[c]){
				A[i] = C[c];
				c++; i++;
			}
			else if(B[b] <= C[c]){
				A[i] = B[b];
				b++; i++;
			}
		}

		while(b<=p){
			A[i] = B[b];
			i++; b++;
		}
		while(c<=q){
			A[i] = C[c];
			i++; c++;
		}

	}

	public static void intMergeSort(Integer[] A, int start, int end){
        if(start<end){
        	int mid = (start+end) >>> 1;
        	intMergeSort(A, start, mid);
        	intMergeSort(A, mid+1, end);
        	intMerge(A, start, mid, end);
        }
    }

    public static<T extends Comparable<? super T>> void genericMerge(T[] A, int start, int mid, int end){
		int n = end-start+1;
		T[] B = (T[]) new Comparable[n];
		T[] C = (T[]) new Comparable[n];

		int k=0;
		for(int i=start;i<mid+1;i++){
			B[k] = A[i];
			k++;
		}
		int p = k-1;

		k=0;
		for(int i=mid+1;i<=end;i++){
			C[k] = A[i];
			k++;
		}
		int q = k-1;

		int i=start, b=0, c=0;
		while(b<=p && c<=q){
			int cmp = B[b].compareTo(C[c]);
			if(cmp<0){
				A[i] = C[c];
				c++; i++;
			}
			else if(cmp>=0){
				A[i] = B[b];
				b++; i++;
			}
		}

		while(b<=p){
			A[i] = B[b];
			i++; b++;
		}
		while(c<=q){
			A[i] = C[c];
			i++; c++;
		}

	}

	public static<T extends Comparable<? super T>> void genericMergeSort(T[] A, int start, int end){
        if(start<end){
        	int mid = (start+end) >>> 1;
        	genericMergeSort(A, start, mid);
        	genericMergeSort(A, mid+1, end);
        	genericMerge(A, start, mid, end);
        }
    }

    public static<T> void swap(T[] arr, int x, int y) {
		T tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
    }

    public static Integer[] intInsertionSort(Integer[] A, int start, int end){
    	for(int i=start+1;i<=end;i++){
    		int j=i-1;
    		while(j>=start && A[j]>A[j+1]){
    			swap(A, j, j+1);
    			j--;
    		}
    	}

    	return A;
    }

    public static<T extends Comparable<? super T>> void genericInsertionSort(T[] A, int start, int end){
    	for(int i=start+1;i<=end;i++){
    		int j=i-1;
    		int cmp = A[j].compareTo(A[j+1]);
    		while(j>=start && cmp<0){
    			swap(A, j, j+1);
    			j--;
    			if(j>=start)
	    			cmp = A[j].compareTo(A[j+1]);
    		}
    	}
    }

    public static void main(String[] args){

    	Integer[] A1 = new Integer[n];
		for(int i=0;i<n;i++){
			A1[i] = new Integer(i+1);
		}
		Shuffle.shuffle(A1);
		System.out.println("Int Merge Sort");
		Timer t = new Timer();
		t.start();
		Integer[] B1 = new Integer[n];
		intMergeSort(A1, 0, n-1);
		t.end();
		System.out.println(t);

		ItemExt[] A2 = new ItemExt[n];
		for(int i=0;i<n;i++){
			A2[i] = new ItemExt(i+1);
		}
		Shuffle.shuffle(A2);
		System.out.println("Generic Merge Sort");
		t.start();
		ItemExt[] B2 = new ItemExt[n];
		genericMergeSort(A2, 0, n-1);
		t.end();
		System.out.println(t);

		Integer[] A3 = new Integer[n];
		for(int i=0;i<n;i++){
			A3[i] = new Integer(i+1);
		}
		Shuffle.shuffle(A3);
		System.out.println("Int Insertion Sort");		
		t.start();
		Integer[] B3 = new Integer[n];
		intInsertionSort(A3, 0, n-1);
		t.end();
		System.out.println(t);

		ItemExt[] A = new ItemExt[n];
		for(int i=0;i<n;i++){
			A[i] = new ItemExt(i+1);
		}
		Shuffle.shuffle(A);
		System.out.println("Generic Insertion Sort");
		t.start();
		ItemExt[] B = new ItemExt[n];
		genericInsertionSort(A, 0, n-1);
		t.end();
		System.out.println(t);
		
	}

}