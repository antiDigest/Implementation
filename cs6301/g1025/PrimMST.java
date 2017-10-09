
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

	public PrimMST(Graph g, boolean index) {
		super(g);
		if (!index) {
			node = new PrimVertex[g.size()];
			for (Vertex u : g)
				node[u.getName()] = new PrimVertex(u);
		} else {
			node = new Prim2Vertex[g.size()];
			for (Vertex u : g)
				node[u.getName()] = new Prim2Vertex(u);
		}

		initialize(g, index);
	}

	public void initialize(Graph g, boolean index) {
		if (!index) {
			for (Vertex u : g) {
				prim(u).seen = false;
				prim(u).parent = null;
			}
		} else {
			for (Vertex u : g) {
				prim2Vertex(u).seen = false;
				prim2Vertex(u).parent = null;
				prim2Vertex(u).d = Integer.MAX_VALUE;
				
			}
		}
	}

	class Prim2Vertex extends PrimVertex implements Comparator<Prim2Vertex>, Index {

		Prim2Vertex(Vertex u) {
			super(u);
		}

		int d, index;

		public int compare(Prim2Vertex u, Prim2Vertex v) {
			return u.d < v.d ? -1 : u.d == v.d ? 0 : 1;
		}

		public void putIndex(int i) {
			index = i;
		}

		public int getIndex() {
			return index;
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

	public PrimVertex prim(Vertex u) {
		return Vertex.getVertex(node, u);
	}

	public Prim2Vertex prim2Vertex(Vertex u) {
		return (Prim2Vertex) Vertex.getVertex(node, u);
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
		prim(src).seen = true;
		prim(src).parent = null;
		while (!pq.isEmpty()) {
			Edge e = pq.poll();
			Vertex u = e.from;// from edge
			Vertex v = e.to;// to edge
			if (prim(u).seen && prim(v).seen) continue;
			else if(!prim(u).seen && prim(v).seen){
				v = e.from; u = e.to;
			}
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

	public int prim2(Vertex src, Graph g) throws Exception {
		int wmst = 0;
//		IndexedHeap<Prim2Vertex> pq = new IndexedHeap<Prim2Vertex>((Prim2Vertex[])node,
//		new Comparator<Prim2Vertex>() {
//			@Override
//			public int compare(Prim2Vertex u, Prim2Vertex v) {
//				return u.d < v.d ? -1 : u.d == v.d ? 0 : 1;
//			}
//		} , node.length);
//		prim2Vertex(src).d = 0;
//	
//		while(!pq.isEmpty()){
//			Prim2Vertex u = pq.remove();
//			u.seen = false;
//			wmst += u.d;
//			for(Edge e : g.getVertex(node[]) ){
//				v = e.otherEnd(u);
//				if
//			}
//			
//		}
//		
//		

		return wmst;
	}
	
//	public Vertex getVertex(Prim2Vertex pv){
//		return Vertex.getVertex(node, u);
//	}

	public static void main(String[] args) throws Exception {
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
		PrimMST mst = new PrimMST(g, false);
		timer.start();
		int wmst = mst.prim1(s);
		timer.end();
		System.out.println("Weight of MST with prim1 algorithm:" + wmst);
		System.out.println(timer);
		
		//mst.initialize(g, true);
		mst = new PrimMST(g, true);
		//Prim2Vertex[] vertices = new Prim2Vertex[g.size()];
//		timer.start();
//		wmst = mst.prim2(s);
//		timer.end();
		System.out.println("Weight of MST with prim2 algorithm:" + wmst);
		System.out.println(timer);
	}
}
