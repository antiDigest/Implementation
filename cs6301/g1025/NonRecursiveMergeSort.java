package cs6301.g1025;

import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

import java.util.Arrays;


public class NonRecursiveMergeSort {

    private static int N = 4;

    public static void merge(int[] A, int start, int mid, int end, int[] tmp) {
        int n = end - start + 1;

        for (int i = start; i < end; i++) {
            tmp[i] = A[i];
        }

        int i = start, b = start, c = mid + 1;
        while (b <= mid && c < end) {
            if (tmp[b] < tmp[c]) {
                A[i] = tmp[b];
                b++;
                i++;
            } else {
                A[i] = tmp[c];
                c++;
                i++;
            }
        }

        while (b <= mid) {
            A[i] = tmp[b];
            i++;
            b++;
        }
        while (c < end) {
            A[i] = tmp[c];
            i++;
            c++;
        }

    }

    public static void mergeSort(int[] A, int start, int end, int[] tmp) {
        int n = end - start + 1;
        for (int size = 1; size <= n; size *= 2) {
            for (int limit = 0; limit < n; limit += size) {
                int s = limit;
                int e = Math.min(limit + size, n);
                int mid = (s + e) >>> 1;
//                System.out.println();
                merge(A, s, mid, e, tmp);
            }
        }
    }

    public static void mergeSort(int[] A, int[] tmp) {
        mergeSort(A, 0, A.length - 1, tmp);
    }


    public static void main(String[] args) {

        Timer t = new Timer();
        System.out.println("n = " + N);
        Integer[] A = new Integer[N];
        for (int i = 0; i < N; i++) {
            A[i] = i + 1;
        }
        Shuffle.shuffle(A);
        int[] A1 = new int[N];
        for (int i = 0; i < N; i++) {
            A1[i] = A[i];
        }
        System.out.println("Unsorted: " + Arrays.toString(A1));
        int[] tmp = new int[N];
        t.start();
        mergeSort(A1, tmp);
        t.end();
        System.out.println("Sorted: " + Arrays.toString(A1));
        System.out.println(t);

    }
}
