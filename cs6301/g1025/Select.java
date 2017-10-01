/**
 * Two versions of partition algorithm
 *
 * @author antriksh, swaroop, gunjan, saikumar
 * Ver 1.0: 2017/10/1. Implemented
 */

package cs6301.g1025;

import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

import java.util.*;

public class Select {
    static PriorityQueue<Integer> pq;
    static int T = 17;

    /**
     * O(n) Select Algorithm driver function
     *
     * @param A
     * @param k
     * @return
     */
    public static int select(int[] A, int k) {
        if (k <= 0) return Integer.parseInt(null);
        if (k >= A.length) return -1;
        return select(A, 0, A.length, k);
    }

    /**
     * PART(i)
     * O(n) Select Algorithm
     *
     * @param A
     * @param start
     * @param size
     * @param k
     * @return
     */
    public static int select(int[] A, int start, int size, int k) {
        int end = size + start - 1;
        if (size < T) {
            insertionSort(A, start, end);
            return A[start + size - k];
        } else {
            int mid = partition(A, start, end);
            int left = mid - start;
            int right = end - mid;
            if (right == k) {
                return A[mid];
            } else if (right <= k) {
                return select(A, start, left, k - (right + 1));
            } else {
                return select(A, mid + 1, right, k);
            }
        }
    }

    /**
     * PART(ii)
     * Select k using Priority Queue
     * Adding n elements and
     * removing the k highest elements
     *
     * @param A
     * @param k
     * @return
     */
    public static List<Integer> selectPQ(int[] A, int k) {
        pq = new PriorityQueue<>(Collections.reverseOrder());
        // O(nlogn)
        for (int a : A) {
            pq.add(a);
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            list.add(pq.remove());
        }
        return list;
    }

    /**
     * PART(iii)
     * Select k using Priority Queue
     * Adding k elements to pq and
     * processing the rest elements to
     * maintain only k elements in the PQ
     *
     * @param A
     * @param k
     * @return
     */
    public static List<Integer> selectPQk(int[] A, int k) {
        pq = new PriorityQueue<>(k);
        // O(klogk)
        for (int i = 0; i < k; i++) {
            pq.add(A[i]);
        }

        for (int i = k; i < A.length; i++) {
            if (A[i] > pq.peek()) {
                pq.remove();
                pq.add(A[i]);
            }
        }

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            list.add(pq.remove());
        }

        return list;
    }

    /**
     * HELPER FUNCTIONS
     */

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
        QuickSort.swap(A, start + x, end);
        int pivot = A[end];

        int i = start - 1;

        for (int j = start; j <= end; j++) {
            /**
             * LOOP INVARIANT: A[p...i] < pivot; A[i+1...j-1] > pivot; A[j .. r-1] = not yet processed;A[r] = pivot
             */
            if (A[j] < pivot) {
                i++;
                QuickSort.swap(A, i, j);
            }
        }
        QuickSort.swap(A, i + 1, end);

        return i + 1;
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
    public static void insertionSort(int[] A, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            int j = i - 1;
            while (j >= start) {
                if (A[j] > A[j + 1]) {
                    QuickSort.swap(A, j, j + 1);
                }
                j--;
            }
        }
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

        System.out.println("O(n) Select");
        Timer t = new Timer();
        t.start();
        select(B1, 5);
        t.end();
        System.out.println(t);

        System.out.println("O(n + klogn) Select");
        t.start();
        System.out.println("5 largest: " + selectPQ(B1, 5));
        t.end();
        System.out.println(t);

        System.out.println("O(nlogk) Select");
        t.start();
        System.out.println("5 largest: " + selectPQk(B1, 5));
        t.end();
        System.out.println(t);
    }

}
