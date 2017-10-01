/**
 * Two versions of partition algorithm
 *
 * @author antriksh, swaroop, gunjan, saikumar
 * Ver 1.0: 2017/10/1. Implemented
 */

package cs6301.g1025;

import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

import java.util.Arrays;
import java.util.Random;

public class QuickSort {

    public static void quickSort(int[] A) {
        quickSort(A, 0, A.length - 1);
    }

    public static void quickSort2(int[] A) {
        quickSort2(A, 0, A.length - 1);
    }

    /**
     * Quick Sort algorithm:
     * Calls partition, gets a mid
     * sorts the two halves surrounding mid
     *
     * @param A
     * @param start
     * @param end
     */
    public static void quickSort(int[] A, int start, int end) {
        if (start < end) {
            int mid = partition(A, start, end);
            quickSort(A, start, mid - 1);
            quickSort(A, mid + 1, end);
        }
    }

    /**
     * Partition using Random Select
     * selects a random element from the array
     * partitions the array with respect to that element
     *
     * @param A
     * @param start
     * @param end
     * @return
     */
    public static int partition(int[] A, int start, int end) {
        Random rand = new Random();
        int x = rand.nextInt(end - start + 1);
        swap(A, start + x, end);
        int pivot = A[end];

        int i = start - 1;

        for (int j = start; j <= end; j++) {
            /**
             * LOOP INVARIANT:
             * A[p...i] < pivot; A[i+1...j-1] > pivot;
             * A[j] = being processed;
             * A[j+1 .. r-1] = not yet processed;
             * A[r] = pivot
             */
            if (A[j] < pivot) {
                i++;
                swap(A, i, j);
            }
        }
        swap(A, i + 1, end);

        return i + 1;
    }

    /**
     * Quick Sort algorithm:
     * Calls partition2, gets a mid
     * sorts the two halves surrounding mid
     *
     * @param A
     * @param start
     * @param end
     */
    public static void quickSort2(int[] A, int start, int end) {
        if (start < end) {
            int mid = partition2(A, start, end);
            quickSort2(A, start, mid);
            quickSort2(A, mid + 1, end);
        }
    }

    /**
     * Hoare's method of partition
     * if you are not careful in implementing this method,
     * it could run into an infinite loop
     *
     * @param A
     * @param start
     * @param end
     * @return
     */
    public static int partition2(int[] A, int start, int end) {
        Random rand = new Random();
        int x = rand.nextInt(end - start + 1);
        swap(A, start + x, end);
        int pivot = A[end];

        int i = start - 1;
        int j = end + 1;
        while (true) {
            /**
             *  LOOP INVARIANT:
             *  A[ p .. i ] ​​<= x;
             *  A[ j .. r ] ​>= x
             */
            do {
                i++;
            } while (A[i] < pivot);
            do {
                j--;
            } while (A[j] > pivot);
            if (i >= j) {
                return j;
            }
            swap(A, i, j);
        }
    }


    /**
     * Utility function swap
     * swaps a and b in array A
     *
     * @param A
     * @param a
     * @param b
     */
    public static void swap(int[] A, int a, int b) {
        int temp = A[a];
        A[a] = A[b];
        A[b] = temp;
    }

    public static void main(String[] args) {
        int n = 10000000;
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

        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = B[i];
        }

        System.out.println("Integer Quick Sort");
        Timer t = new Timer();
        t.start();
        quickSort(B1);
        t.end();
        System.out.println(t);

        System.out.println("Integer Quick Sort 2");
        t.start();
        quickSort2(A);
        t.end();
        System.out.println(t);

        int k = n - 1;
        for (int i = 0; i < n; i++) {
            B1[i] = A[k--];
        }

        System.out.println("Integer Quick Sort - Descending order");
        t.start();
        quickSort(B1);
        t.end();
        System.out.println(t);

        k = n - 1;
        for (int i = 0; i < n; i++) {
            B1[i] = A[k--];
        }

        System.out.println("Integer Quick Sort 2 - Descending order");
        t.start();
        quickSort2(B1);
        t.end();
        System.out.println(t);
    }
}
