package cs6301.g1025;

import java.util.Scanner;

public class KnuthL extends PermutationCombination {

    void permute(int n) {
        visit(n);
        int j;
        int l;
        while (!descending(0, n)) {
            j = n - 1;
            while (!(A[j] < A[j + 1]) && j >= 0)
                j--;
            l = n;
            while (!(A[j] < A[l]) && l > j)
                l--;
            swap(j, l);
            reverse(j + 1, n);
            visit(n);
        }
    }

    /**
     * HELPER FUNCTIONS
     */

    void reverse(int start, int end) {
        while (start < end) {
            swap(start, end);
            start++;
            end--;
        }
    }

    boolean descending(int start, int end) {
        for (int i = start; i < end; i++) {
            if (A[i] < A[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        Scanner scanner;
        int VERBOSE = 0;

        if (args.length > 0)
            VERBOSE = Integer.parseInt(args[0]);

        int n = 6;
        KnuthL pc = new KnuthL();
        pc.VERBOSE = VERBOSE;
        pc.A = new int[]{1, 2, 2, 3, 3, 4};
//        for (int i = 0; i < n; i++) {
//            pc.A[i] = i + 1;
//        }
        pc.permute(n - 1);
    }

}
