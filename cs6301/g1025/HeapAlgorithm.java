package cs6301.g1025;

import java.util.Scanner;

public class HeapAlgorithm extends PermutationCombination {

    void recursivePermute(int g, int n) {
        if (g == 1) {
            visit(n - 1);
        } else {
            for (int i = 0; i < g - 1; i++) {
                recursivePermute(g - 1, n);
                if (g % 2 == 0) {
                    swap(i, g - 1);
                } else {
                    swap(0, g - 1);
                }
            }
            recursivePermute(g - 1, n);
        }
    }

    void permute(int n) {
        int[] c = new int[n];

        visit(n - 1);

        int i = 0;
        while (i < n) {
            if (c[i] < i) {
                if (i % 2 == 0) {
                    swap(0, i);
                } else {
                    swap(c[i], i);
                }
                visit(n - 1);
                c[i]++;
                i = 0;
            } else {
                c[i] = 0;
                i++;
            }
        }
    }

    public static void main(String[] args) {

        Scanner scanner;
        int VERBOSE = 0;

        if (args.length > 0)
            VERBOSE = Integer.parseInt(args[0]);

        int n = 3;
        HeapAlgorithm pc = new HeapAlgorithm();
        pc.VERBOSE = VERBOSE;
        pc.A = new int[n];
        for (int i = 0; i < n; i++) {
            pc.A[i] = i + 1;
        }
        pc.permute(n);
    }
}
