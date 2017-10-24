package cs6301.g1025;

public class KnuthL extends PermutationCombination {

    void permute(int n) {
        visit(n);
        int j;
        int l;
        while (!descending(0, n)) {
            j = n - 1;
            while (!(A[j] < A[j + 1]) && j>=0)
                j--;
            l = n;
            while (!(A[j] < A[l]) && l>j)
                l--;
            swap(j, l);
            reverse(j + 1, n - 1);
            visit(n);
        }
    }

    /**
     * HELPER FUNCTIONS
     */

    void visit(int n){
        System.out.print(++count + ": ");
        printList(0, n, A);
    }

    void reverse(int start, int end) {
        int k = end;
        int mid = (end - start + 1) / 2;
        for (int i = start; i <= mid; i++) {
            swap(i, k);
            k--;
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
        int n = 6;
        KnuthL pc = new KnuthL();
        pc.A = new int[]{1, 2, 2, 3, 3, 4};
//        for (int i = 0; i < n; i++) {
//            pc.A[i] = i + 1;
//        }
        pc.permute(n - 1);
    }

}
