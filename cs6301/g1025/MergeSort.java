/**
 * Merge Sort implementation using various methods with different time and space complexities
 */
package cs6301.g1025;

import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

import java.util.Arrays;

import static cs6301.g1025.MergeSort.mergeThreeSort;

public class MergeSort {

    /**
     * Textbook method for merge sort with a new temp array allocated for each instance
     *
     */

    public static void mergeOne(int arr[], int start, int mid, int end){
        int p = mid - start + 1;
        int q = end - mid;

        int L[] = new int[p];
        int R[] = new int[q];

        for(int i = 0; i < p; i++)
            L[i] = arr[start + i];
        for(int j = 0; j < q; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0;

        int k = start;
        while(i < p && j < q){
            if(L[i] < R[j]){
                arr[k] = L[i];
                i++;
            }
            else{
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while(i < p){
            arr[k] = L[i];
            i++;
            k++;
        }
        while(j < q){
            arr[k] = R[j];
            k++;
            j++;
        }
    }

    static void mergeTwo(int[] arr, int start, int mid, int end, int[] tmp){

        for(int i = start; i <= end; i++){
            tmp[i] = arr[i];
        }
        int i = start, j = mid + 1;
        for(int k = start; k <= end; k++){
            if(j > end || (i <= mid && tmp[i] <= tmp[j])){
                arr[k] = tmp[i++];
            }
            else
                arr[k] = tmp[j++];
        }
    }

    void mergeThree(int arr[], int start, int mid, int end, int tmp[]){

        for(int i = 0; i < end; i++){
            tmp[i] = arr[i];
        }
        int i = start, j = mid + 1;
        for(int k = start; k < end; k++){
            if(j > end || (i <= mid && tmp[i] <= tmp[j])){
                arr[k] = tmp[i++];
            }
            else
                arr[k] = tmp[j++];
        }
    }

    static void mergeFour(int arr[], int start, int mid, int end, int[] tmp){
        int i = start, j = mid + 1;
        for(int k = start; k <= end; k++){
            if(j > end || (i <= mid && arr[i] <= arr[j])){
                tmp[k] = arr[i++];
            }
            else
                tmp[k] = arr[j++];
        }
    }

    static void insertionSort(int[] arr, int start, int end){
        int n = arr.length;
        for(int i = start; i <= end; ++i){
            int k = arr[i];
            int j = i-1;

            while(j >= 0 && arr[j] > k){
                arr[j+1] = arr[j];
                j = j-1;
            }
            arr[j+1] = k;
        }
    }

    public void mergeSort(int[] A){

        int start = 0;
        int end = A.length;
        int[] B = new int[A.length];
       // mergeOneSort(A, start, end);
        //mergeTwoSort(A, start, end);
        //mergeThreeSort(A, start, end);
        System.arraycopy(A, start, B,0, A.length );
        mergeFourSort(A, B, start, end);

    }

    static void mergeOneSort(int[] arr, int start, int end){
        if(start < end) {
            int mid = (start + end) / 2;
            mergeOneSort(arr, start, mid);
            mergeOneSort(arr, mid+1, end);
            mergeOne(arr, start, mid, end);
        }
    }

    static void mergeTwoSort(int[] arr, int start, int end){
        int[] tmp = new int[arr.length];
        if(start < end) {
            int mid = (start + end) / 2;
            mergeTwoSort(arr, start, mid);
            mergeTwoSort(arr, mid+1, end);
            mergeTwo(arr, start, mid, end, tmp);
        }
    }

    static void mergeThreeSort(int[] arr, int start, int end) {
        int[] tmp = new int[arr.length];
        int T = 10;
//        if (arr.length < T) {
//            insertionSort(arr, start, end);
//        } else {
            if (start < end) {
                int mid = (start + end) / 2;
                mergeThreeSort(arr, start, mid);
                mergeThreeSort(arr, mid + 1, end);
                mergeTwo(arr, start, mid, end, tmp);
            }
//        }
    }

    static void mergeFourSort(int[] arr, int[] tmp, int start, int end){
        int T = 17;
        if (end - start < T) {
            insertionSort(arr, start, end);
        } else {
            if (start < end) {
                int mid = (start + end) / 2;
                mergeFourSort(tmp, arr, start, mid);
                mergeFourSort(tmp, arr,mid + 1, end);
                mergeFour(tmp, start, mid, end, arr);
            }
        }
    }

    public static void main(String[] args){

        int n=20;
        System.out.println("n = " + n);
        Integer[] B = new Integer[n];
        for (int i = 0; i < n; i++) {
            B[i] = i + 1;
        }
        Shuffle.shuffle(B);
        int[] B1 = new int[n];
        for (int i = 0; i < n; i++) {
            B1[i] = B[i];
        }
        System.out.println("Integer Merge Sort");
        Timer t = new Timer();
        int[] tmp = new int[n];
        System.arraycopy(B1, 0, tmp, 0, B1.length);
        System.out.println(Arrays.toString(B1));
        t.start();
//        mergeOneSort(B1, 0, B1.length-1);
//        mergeTwoSort(B1, 0, B1.length-1);
        mergeThreeSort(B1, 0,B1.length-1);
        mergeFourSort(B1, tmp, 0, B1.length - 1);
        t.end();
        System.out.println(t);
        System.out.println(Arrays.toString(B1));
    }

}

