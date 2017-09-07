package cs6301.g10;

import cs6301.g10.utils.Shuffle;
import cs6301.g10.utils.Timer;

import java.util.Arrays;


public class NonRecursiveMergeSort {

    final static int n = 100000000;
    public static void merge(int[] arr, int start, int end, int[] tmp) {

        int[] swap;
        int k = 1, temp;
        while(k < n){
            int left = start;
            int i,j = k;
            int right = k << 1;
            while(j < n){
                if(right > n){
                    right = n;
                }
                temp = left;
                i = j;
                while(left < j && i < right){
                    if(arr[left] <= arr[i]){
                        tmp[temp++] = arr[left++];
                    }
                    else {
                        tmp[temp++] = arr[i++];
                    }
                }
                while(left < j){
                    tmp[temp++] = arr[left++];
                }
                while(i < right){
                    tmp[temp++] = arr[i++];
                }

                left = right;
                j = left + k;
                right = j +  k;
            }

            while(left < n){
                tmp[left] = arr[left++];
            }

            swap = arr;
            arr = tmp;
            tmp = swap;
            k = k << 1;
           // System.out.println(Arrays.toString(arr));
        }
        //System.out.print(Arrays.toString(arr));
        //return arr;
    }


    public static void mergeSort(int[] A, int[] tmp){

        merge(A, 0, A.length - 1, tmp);
       // System.out.println(Arrays.toString(A));
    }


    public static void main(String[] args){

        Timer t = new Timer();
        System.out.println("n = " + n);
        Integer [] A = new Integer[n];
        for(int i = 0; i < n; i++){
            A[i] = i;
        }
        Shuffle.shuffle(A);
        int[] A1 = new int[n];
        for(int i=0; i<n; i++){
            A1[i] = A[i];
        }
        //System.out.println(Arrays.toString(A1));
        int[] tmp = new int[n];
        t.start();
        mergeSort(A1, tmp);
        t.end();
       // System.out.println(Arrays.toString(A1));
        System.out.println(t);

    }
}
