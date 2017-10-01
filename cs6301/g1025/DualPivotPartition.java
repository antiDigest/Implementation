/**
 * Dual Pivot Partition
 *
 * @author antriksh, swaroop, gunjan, saikumar
 * Ver 1.0: 2017/10/1. Implemented
 */

package cs6301.g1025;

import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

import java.util.Arrays;
import java.util.Random;

public class DualPivotPartition {
    static int T = 17;

    public static void quickSort(int[] A) {
        quickSort(A, 0, A.length - 1);
    }

    /**
     * Quick Sort calling
     * dual pivot partition for partition
     * and recursively sorting the parts
     *
     * @param A
     * @param start
     * @param end
     */
    public static void quickSort(int[] A, int start, int end) {
        int size = end - start + 1;
        if (size < T) {
            Select.insertionSort(A, start, end);
        } else if (start < end) {
            int[] mid = dualPivotPartition(A, start, end);
            quickSort(A, start, mid[0] - 1);
            quickSort(A, mid[1] + 1, end);
            if (A[mid[0]] != A[mid[1]]) {
                quickSort(A, mid[0] + 1, mid[1] - 1);
            }
        }
    }

    /**
     * Yaroslavskiy's method to perform
     * dual pivot partition
     *
     * @param A
     * @param start
     * @param end
     * @return
     */
    public static int[] dualPivotPartition(int[] A, int start, int end) {

        // Calling method to select dual pivots s.t. x <= y
        selectPivots(A, start, end);
        int x = A[start];
        int y = A[end];

        int k = start + 1;
        int i = start + 1;
        int j = end - 1;

        while (i < j) {
            /**
             * LOOP INVARIANT:
             * A[p] = x; Pivot 1
             * A[p+1 .. k] < x;
             * x <= A[k+1 .. i] <= y;
             * A[i+1 ... j-1] = Unprocessed;
             * A[j...r-1] > y;
             * A[r] = y; Pivot 2
             */
            // Checking if the element for S2 is found in S3
            if (A[i] > y && (A[j] >= x && A[j] <= y)) {
                // Both S1 and S3 grow
                QuickSort.swap(A, i, j);
                i++;
                j--;
            } // Checking validity for circular swap
            else if (A[i] > y && A[j] < x) {
                // Circular Swap: k -> i -> j -> k
                circularSwap(A, k, i, j);
                k++;
                i++;
                j--;
            } // Checking validity for S1
            else if (A[i] < x) {
                QuickSort.swap(A, i, k); // S1 grows, swap A[i] and A[k]
                k++;
                i++;
            } // Checking Validity for S3
            else if (A[j] > y) {
                j--; // S3 grows
            } // Checking validity for S2
            else if (x <= A[i] && y >= A[i]) {
                i++; // S2 grows
            }

        }
        QuickSort.swap(A, start, k - 1);
        QuickSort.swap(A, end, j + 1);

        int[] val = new int[2];
        val[0] = k - 1;
        val[1] = j + 1;
        return val;
    }

    /**
     * Procedure to select two pivots
     * s.t. x1 <= x2
     *
     * @param A
     * @param start
     * @param end
     * @return
     */
    public static void selectPivots(int[] A, int start, int end) {
        Random rand = new Random();
        int x = rand.nextInt(end - start + 1);
        QuickSort.swap(A, start + x, start); //Re-using code !!
        int y = rand.nextInt(end - start + 1);
        QuickSort.swap(A, start + y, end); //Re-using code !!
        if (A[start] > A[end]) {
            QuickSort.swap(A, start, end); //Re-using code !!
        }
    }

    public static void circularSwap(int[] A, int k, int i, int j) {

        // k -> i -> j -> k
        int temp = A[k];

        A[k] = A[i];
        A[i] = A[j];
        A[j] = temp;

    }

    public static void main(String[] args) {
        int n = 35;
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

        System.out.println("Dual Pivot Partition Quick Sort");
        System.out.println(Arrays.toString(B1));
        Timer t = new Timer();
        t.start();
        quickSort(B1);
        t.end();
        System.out.println(t);
        System.out.println(Arrays.toString(B1));

        Random rand = new Random();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt((i + 1) * 5);
        }

        System.out.println("Dual Pivot Partition Quick Sort - Duplicates");
        System.out.println(Arrays.toString(A));
        t.start();
        quickSort(A);
        t.end();
        System.out.println(t);
        System.out.println(Arrays.toString(A));
    }

}
