
/* Ver 1.0: Starter code for Prim's MST algorithm */

package cs6301.g00;

import java.util.Scanner;
import java.lang.Comparable;
import java.io.FileNotFoundException;
import java.io.File;

public class PrimMST {
    static final int Infinity = Integer.MAX_VALUE;

    public PrimMST(Graph g) {
    }

    public int prim1(Graph.Vertex s) {
        int wmst = 0;

        // SP6.Q4: Prim's algorithm using PriorityQueue<Edge>:

        return wmst;
    }

    public int prim2(Graph.Vertex s) {
        int wmst = 0;

        // SP6.Q6: Prim's algorithm using IndexedHeap<PrimVertex>:

        return wmst;
    }

    public static void main(String[] args) throws FileNotFoundException {
	Scanner in;

        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

	Graph g = Graph.readGraph(in);
        Graph.Vertex s = g.getVertex(1);

	Timer timer = new Timer();
	PrimMST mst = new PrimMST(g);
	int wmst = mst.prim2(s);
	timer.end();
        System.out.println(wmst);
    }
}
