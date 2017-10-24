package cs6301.g1025;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class PermutationCombination {

    protected int[] A;
    protected int count = 0;
    protected int VERBOSE;

    void permute(int n, int k) {
        permute(n, k, n);
    }

    void permute(int c, int k, int n) {
        if (c == 0) {
            visit(k - 1);
        } else {
            int d = k - c;
            permute(c - 1, k, n);
            if (d >= 0)
                for (int i = d + 1; i < n; i++) {
                    swap(d, i);
                    permute(c - 1, k, n);
                    swap(d, i);
                }
        }
    }

    void combination(int n, int k) {
        int[] chosen = new int[n];
        this.combination(0, k, k, chosen, n);
    }

    void combination(int i, int c, int k, int[] chosen, int n) {
        if (c == 0) {
            visit(k - 1);
        } else {
            chosen[k - c] = A[i];
            combination(i + 1, c - 1, k, chosen, n);
            if (n - i > c) {
                combination(i + 1, c, k, chosen, n);
            }
        }
    }

    /**
     * HELPER FUNCTIONS
     */

    protected static void printList(int start, int end, int[] A) {
        for (int i = start; i <= end; i++)
            System.out.print(A[i] + " ");
        System.out.println("");
    }

    protected void swap(int a, int b) {
        int tmp = A[a];
        A[a] = A[b];
        A[b] = tmp;
    }

    void visit(int n) {
        if (VERBOSE == 1) {
            System.out.print(++count + ": ");
            printList(0, n, A);
        } else count++;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner;
        int VERBOSE = 0;

        if (args.length > 0)
            VERBOSE = Integer.parseInt(args[0]);

        int n = 6;
        PermutationCombination pc = new PermutationCombination();
        pc.VERBOSE = VERBOSE;
        pc.A = new int[n];
        for (int i = 0; i < n; i++) {
            pc.A[i] = i + 1;
        }
//        pc.permute(n, n);
//        pc.count = 0;
        pc.combination(n, 4);
    }

}
