/******************************************************************************
 *  Compilation:  javac KruskalMST.java
 *  Execution:    java cs6301.g1025.KruskalMST input.txt
 *  
 *  Implementation of Kruskal using Disjoint set ADT
 *
 * @author antriksh, swaroop, gunjan, saikumar
 * Ver 1.0: 2017/10/8. Implemented
 *
 ******************************************************************************/

package cs6301.g1025;

import java.util.Scanner;

import cs6301.g00.Graph;
import cs6301.g00.Timer;

import java.lang.Comparable;
import java.io.FileNotFoundException;
import java.io.File;

public class KruskalMST {
	public KruskalMST(Graph g) {
	}

	public int kruskal() {
		int wmst = 0;

		// SP6.Q7: Kruskal's algorithm:

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
		KruskalMST mst = new KruskalMST(g);
		int wmst = mst.kruskal();
		timer.end();
		System.out.println(wmst);
	}
}
