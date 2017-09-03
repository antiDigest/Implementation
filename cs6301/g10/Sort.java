/**
 * Sort class which will contain all sort functions.
 *
 * @author Antriksh, Gunjan, Atif
 * Ver 1.0: 2017/08/28
 */

package cs6301.g10;

import cs6301.g10.utils.ItemExt;
import cs6301.g10.utils.Shuffle;
import cs6301.g10.utils.Timer;

public class Sort {
    /*  Contains Merge Sort in the form of mand int sorting.
    *	Usage: mergeSort(A, start, end)
    *			mergeSort(A, start, end) -- uses ItemExt class.
    *
    *	Also contains Insertion Sort as an O(n^2) running algorithm.
    *
    */
    static int n = 6000000;

    public static void merge(int[] A, int start, int mid, int end, int[] tmp) {
        int n = end - start + 1;

        for (int i = start; i <= end; i++) {
            tmp[i] = A[i];
        }

        int i = start, b = start, c = mid+1;
        while (b <= mid && c <= end) {
            if (tmp[b] < tmp[c]) {
                A[i] = tmp[c];
                c++;
                i++;
            } else {
                A[i] = tmp[b];
                b++;
                i++;
            }
        }

        while (b <= mid) {
            A[i] = tmp[b];
            i++;
            b++;
        }
        while (c <= end) {
            A[i] = tmp[c];
            i++;
            c++;
        }

    }

    public static void mergeSort(int[] A, int[] tmp) {
        mergeSort(A, 0, A.length-1, tmp);
    }

    public static void mergeSort(int[] A, int start, int end, int[] tmp) {
        if (start < end) {
            int mid = (start + end) >>> 1;
            mergeSort(A, start, mid, tmp);
            mergeSort(A, mid + 1, end, tmp);
            merge(A, start, mid, end, tmp);
        }
    }

    public static <T extends Comparable<? super T>> void merge(T[] A, int start, int mid, int end, T[] tmp) {
        int n = end - start + 1;

        for (int i = start; i <= end; i++) {
            tmp[i] = A[i];
        }

        int i = start, b = start, c = mid+1;
        while (b <= mid && c <= end) {
            if (tmp[b].compareTo(tmp[c]) < 0) {
                A[i] = tmp[c];
                c++;
                i++;
            } else {
                A[i] = tmp[b];
                b++;
                i++;
            }
        }

        while (b <= mid) {
            A[i] = tmp[b];
            i++;
            b++;
        }
        while (c <= end) {
            A[i] = tmp[c];
            i++;
            c++;
        }

    }

    public static <T extends Comparable<? super T>> void mergeSort(T[] A, T[] tmp) {
        mergeSort(A, 0, A.length-1, tmp);
    }

    public static <T extends Comparable<? super T>> void mergeSort(T[] A, int start, int end, T[] tmp) {
        if (start < end) {
            int mid = (start + end) >>> 1;
            mergeSort(A, start, mid, tmp);
            mergeSort(A, mid + 1, end, tmp);
            merge(A, start, mid, end, tmp);
        }
    }

    public static <T> void swap(T[] arr, int x, int y) {
        T tmp = arr[x];
        arr[x] = arr[y];
        arr[y] = tmp;
    }

    public static Integer[] intInsertionSort(Integer[] A, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            int j = i - 1;
            while (j >= start && A[j] > A[j + 1]) {
                swap(A, j, j + 1);
                j--;
            }
        }

        return A;
    }

    public static <T extends Comparable<? super T>> void nSquareSort(T[] A){
        nSquareSort(A, 0, A.length-1);
    }

    public static <T extends Comparable<? super T>> void nSquareSort(T[] A, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            int j = i - 1;
            int cmp = A[j].compareTo(A[j + 1]);
            while (j >= start && cmp < 0) {
                swap(A, j, j + 1);
                j--;
                if (j >= start)
                    cmp = A[j].compareTo(A[j + 1]);
            }
        }
    }

    public static void main(String[] args) {

        Integer[] A1 = new Integer[n];
        for (int i = 0; i < n; i++) {
            A1[i] = i + 1;
        }
        Shuffle.shuffle(A1);
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = A1[i];
        }
        System.out.println("Int Merge Sort");
        Timer t = new Timer();
        t.start();
        int[] tmp = new int[n];
        mergeSort(A, tmp);
        t.end();
        System.out.println(t);

        ItemExt[] A2 = new ItemExt[n];
        for (int i = 0; i < n; i++) {
            A2[i] = new ItemExt(i + 1);
        }
        Shuffle.shuffle(A2);
        System.out.println("Generic Merge Sort");
        t.start();
        ItemExt[] tmp2 = new ItemExt[n];
        mergeSort(A2, tmp2);
        t.end();
        System.out.println(t);

        ItemExt[] A4 = new ItemExt[n];
        for (int i = 0; i < n; i++) {
            A4[i] = new ItemExt(i + 1);
        }
        Shuffle.shuffle(A4);
        System.out.println("n Square Sort");
        t.start();
        ItemExt[] B = new ItemExt[n];
        nSquareSort(A4, 0, n - 1);
        t.end();
        System.out.println(t);

    }

}