/**
 * Sort class which will contain all sort functions.
 *
 * @author Antriksh, Gunjan
 * Ver 1.0: 2017/08/28
 */

package cs6301.g10;

import cs6301.g10.utils.Item;
import cs6301.g10.utils.Timer;
import cs6301.g10.utils.Shuffle;

import java.util.*;

public class Sort {
    static int n = 10;

    /**
     * Merge Procedure
     * Usage: merge(A, start, end, tmp)
     *
     * @param start = start location of merge
     * @param end   = end location
     */
    public static void merge(int[] arr, int start, int end, int[] tmp) {
        int mid = (start + end) / 2;
        int p = start;
        int q = mid + 1;

        int k = 0;
        for (int i = start; i <= end; i++) {
            if (p > mid)
                tmp[k++] = arr[q++];
            else if (q > end)
                tmp[k++] = arr[p++];
            else if (arr[p] < arr[q])
                tmp[k++] = arr[p++];
            else
                tmp[k++] = arr[q++];
        }

        for (int r = 0; r < k; r++) {
            arr[start++] = tmp[r];
        }
    }

    /**
     * Merge Sort
     * Sorts the input array in ascending order, using the temporary array in the merge procedure.
     * Usage: mergeSort(A, tmp)
     */
    public static void mergeSort(int[] A, int[] tmp) {
        mergeSort(A, 0, A.length-1, tmp);
    }

    /** Merge Sort
     * Sorts the input array A[start..end] in ascending order, using the temporary array in the merge procedure.
     * @param A: input array
     * @param start: start index in array
     * @param end: end index of array
     * @param tmp: temporary array for merge
     */

    public static void mergeSort(int[] A, int start, int end, int[] tmp) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(A, start, mid, tmp);
            mergeSort(A, mid + 1, end, tmp);
            merge(A, start, end, tmp);
        }
    }

    /** Merge Procedure
     *  Generic Merge procedure of the divide and conquer merge sort algorithm. Merges the two sorted halves.
     *  Usage: merge(A, start, end, tmp)
     *
     *  @param start = start index of merge
     *  @param end = end index
     */
    public static <T extends Comparable<? super T>> void merge(T[] A, int start, int end, T[] tmp) {
        int mid = (start + end) / 2;
        int p = start;
        int q = mid + 1;

        int m = 0;
        for (int i = start; i <= end; i++) {
            if (p > mid)
                tmp[m++] = A[q++];
            else if (q > end)
                tmp[m++] = A[p++];
            else if (A[p].compareTo(A[q]) < 0)
                tmp[m++] = A[p++];
            else
                tmp[m++] = A[q++];
        }

        for (int r = 0; r < m; r++) {
            A[start++] = tmp[r];
        }
    }

    /**
     * Generic Merge Sort
     * Sorts the input array A in ascending order, using the temporary array in the merge procedure.
     *
     * @param A:   input array
     * @param tmp: temporary array for merge
     */

    public static <T extends Comparable<? super T>> void mergeSort(T[] A, T[] tmp) {
        mergeSort(A, 0, A.length-1, tmp);
    }

    /** Generic Merge Sort
     * Sorts the input array A[start..end] in ascending order, using the temporary array in the merge procedure.
     * @param A: input array
     * @param start: start index in array
     * @param end: end index of array
     * @param tmp: temporary array for merge
     */
    public static <T extends Comparable<? super T>> void mergeSort(T[] A, int start, int end, T[] tmp) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(A, start, mid, tmp);
            mergeSort(A, mid + 1, end, tmp);
            merge(A, start, end, tmp);
        }
    }

    /**
     * Swap
     * swap values at i and j in the array.
     *
     * @param A: input array
     * @param i: first index
     * @param j: second index
     */
    public static <T> void swap(T[] A, int i, int j) {
        T temp;
        temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

    /**
     * Generic Insertion Sort
     * Sorts the input array A in ascending order.
     * RunTime: O(n^2)
     *
     * @param A: input array
     */
    public static <T extends Comparable<? super T>> void nSquareSort(T[] A) {
        nSquareSort(A, 0, A.length - 1);
    }

    /**
     * Generic Insertion Sort
     * Sorts the input array A[start..end] in ascending order.
     * RunTime: O(n^2)
     *
     * @param A:     input array
     * @param start: start index
     * @param end:   end index
     */
    public static <T extends Comparable<? super T>> void nSquareSort(T[] A, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            int j = i - 1;
            while (j >= start) {
                if (A[j].compareTo(A[j + 1]) > 0) {
                    swap(A, j, j + 1);
                }
                j--;
            }
        }
    }

    public static void main(String[] args) {

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
        t.start();
        mergeSort(B1, tmp);
        t.end();
        System.out.println(t);


        Item[] A = new Item[n];
        for (int i = 0; i < n; i++) {
            A[i] = new Item(i+1);
        }
        Shuffle.shuffle(A);
        System.out.println("Generic Merge Sort");
        Item[] tmp1 = new Item[n];
        t.start();
        mergeSort(A, tmp1);
        t.end();
        System.out.println(t);


        Shuffle.shuffle(A);
        System.out.println("Generic n Square Sort");
        t.start();
        nSquareSort(A);
        t.end();
        System.out.println(t);
    }
}
