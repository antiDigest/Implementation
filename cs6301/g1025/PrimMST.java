
/* Ver 1.0: Starter code for Prim's MST algorithm */

package cs6301.g1025;

import java.util.Scanner;

import cs6301.g00.Graph;
import cs6301.g00.Graph.Edge;
import cs6301.g00.Graph.Vertex;
import cs6301.g00.GraphAlgorithm;
import cs6301.g00.Timer;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PrimMST extends GraphAlgorithm<PrimMST.PrimVertex> {


	static final int Infinity = Integer.MAX_VALUE;

	public PrimMST(Graph g) {
		super(g);
		node = new PrimVertex[g.size()];
		// Create array for storing vertex properties
		for (Vertex u : g) {
			node[u.getName()] = new PrimVertex(u);
		}

		initialize(g);
	}
	
	public void initialize(Graph g){
		for (Vertex u : g) {
			prim(u).seen = false;
			prim(u).parent = null;
		}
	}

	/**
	 * Wrapper to store component numbers
	 */
	static class PrimVertex {
		boolean seen;
		Graph.Vertex parent;

		PrimVertex(Graph.Vertex u) {
			seen = false;
			parent = null;
		}
	}

	public PrimVertex prim(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}

	// SP6.Q4: Prim's algorithm using PriorityQueue<Edge>:
	public int prim1(Vertex src) {
		int wmst = 0;
		PriorityQueue<Graph.Edge> pq = new PriorityQueue<Graph.Edge>(new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				return (e1.weight < e2.weight ? -1 : (e1.weight == e2.weight ? 0 : 1));
			}
		});
		for (Edge e : src) {
			pq.add(e);
		}
		// Graph.Vertex u = src;
		prim(src).seen = true;
		prim(src).parent = null;
		while (!pq.isEmpty()) {
			Edge e = pq.poll();
			Vertex u = e.from;// from edge
			Vertex v = e.to;// to edge
			if (prim(u).seen && prim(v).seen)
				continue;// if both seen continue
			prim(v).seen = true;
			prim(v).parent = u;
			wmst += e.weight;// only now add this edge weight to wmst
			for (Edge edge : v) {
				if (!prim(edge.otherEnd(v)).seen)
					pq.add(edge);
			}
		}
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
		timer.start();
		int wmst = mst.prim1(s);
		timer.end();
		System.out.println("Weight of MST with prim1 algorithm:" + wmst);
		System.out.println(timer);
		mst.initialize(g);
		
		timer.start();
		wmst = mst.prim2(s);
		timer.end();
		System.out.println("Weight of MST with prim2 algorithm:" + wmst);
		System.out.println(timer);
	}
}
