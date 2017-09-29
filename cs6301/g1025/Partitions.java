package cs6301.g1025;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Partitions<E> implements Comparable<E>{

	public static <T extends Comparable<? super T>>void swap(int x, int y, T[] arr){
		T tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
	}
	static <T extends Comparable<? super T>> int partition1(T[] arr, int p, int r){
		
		int q = ThreadLocalRandom.current().nextInt(p, r);
		swap(q, r, arr);
		T x = arr[r];
		int j = p-1, i = p;
		// LI: A[ p....i ] ≤ x, A[ i +1...j −1 ] > x
		// A[ j....r − 1 ] is unprocessed, A[ r ] = x.
		for(; i < r; i++){
			if(arr[i].compareTo(x) <=0 ){
				swap(i, ++j, arr);
			}
		}
		swap(j+1, r, arr);
		return j+1;
		
		
	}
	
	static<T extends Comparable<? super T>> int partition2(T[] arr, int p, int r){
		int k = ThreadLocalRandom.current().nextInt(p, r);
		//int k = (p + r) >>> 1;
		int i = p-1, j = r+1;
		T x = arr[k];
		// LI: A[ p...i ] ≤ x, A[ j....r ] ≥ x
		while(true){
			do{i++;} 
				while(arr[i].compareTo(x) <0);
			do{j--;} 
				while(arr[j].compareTo(x) >0);
			if(i >= j) return j;
			swap(i, j, arr);
			i++; j--;
			
		}
	}
	
	static<T extends Comparable<? super T>>  void quickSort(T[] arr, int p, int r){
		if(p>=r) return;
		int q = partition1(arr, p, r);
		quickSort(arr, p, q-1);
		quickSort(arr, q+1, r);
	}
	
	static<T extends Comparable<? super T>>  void quickSort2(T[] arr, int p, int r){
		if(p>=r) return;
		int q = partition2(arr, p, r);
		quickSort2(arr, p, q-1);
		quickSort2(arr, q, r);
	}
	public static void main(String[] args) {
		
		//Partitions<Integer> p = new Partitions<Integer>();
		Integer[] arr1 = { 2,3,4,5,2,1,4,5,4,3,8,3,9,2,4,1};
		Integer[] arr = {4,2,8,9,5,3,7,6};
		quickSort2(arr, 0, arr.length-1);
		System.out.println(Arrays.toString(arr));
		

	}
	@Override
	public int compareTo(E o) {
		return this.compareTo(o) == -1 ? -1 : this.compareTo(o) == 0 ? 0 : 1;
	}

}
