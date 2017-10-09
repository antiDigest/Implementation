
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

	public void initialize(Graph g) {
		for (Vertex u : g) {
			prim(u).seen = false;
			prim(u).parent = null;
		}
	}

	class PrimVertex {
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
		
	
		prim(src).seen = true;
		prim(src).parent = null;
		while (!pq.isEmpty()) {
			Edge e = pq.poll();
			Vertex u = e.from;// from edge
			Vertex v = e.to;// to edge
			if (prim(u).seen && prim(v).seen)
				continue;// if both seen continue
			else if(!prim(u).seen && prim(v).seen){
				v = e.from;
				u = e.to;
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

	// Algorithm uses a parallel array for storing information about
	// Primvertices2

	// Comparator for minheap
	static class minPQ implements Comparator<PrimVertex2> {

		@Override
		public int compare(PrimVertex2 u, PrimVertex2 v) {
			if (u.d < v.d) {
				return -1;
			} else if (u.d == v.d) {
				return 0;
			} else {
				return 1;
			}

		}

	}

	
	
	//create a Indexheap instance
	static IndexedHeap<PrimVertex2> pqi=null;
	
	//create primVertex2 vertices
	static PrimVertex2[] primVertex2= null;

	//create a heap by adding vertices to primVertex2
	public void CreateQueue(Graph g) throws PQException {
        PrimVertex2 v2 = null;
		primVertex2 = new PrimVertex2[g.size()];

		for (Vertex v : g) {
			v2 = new PrimVertex2(node[v.getName()], Infinity, v);
			v2.putIndex(v.getName());
			primVertex2[v.getName()] = v2;

		}
		pqi = new IndexedHeap<PrimVertex2>(primVertex2, new minPQ(), g.size());
		

	}

	public int prim2(Graph.Vertex s) throws PQException {
		
	
		int wmst = 0;
		// SP6.Q6: Prim's algorithm using IndexedHeap<PrimVertex>:
		PrimVertex2 u = primVertex2[s.getName()];
		
		u.d = 0;
		pqi.decreaseKey(u);
		
		while (!pqi.isEmpty()) {
			u = pqi.remove();
			
			u.primv.seen = true;
			wmst = wmst + u.d;
			
			for (Edge e : u.mapVertex) {

				PrimVertex2 v = primVertex2[e.otherEnd(u.mapVertex).getName()];
				if (!v.primv.seen && e.weight < v.d) {
					v.d = e.weight;
					v.primv.parent = u.mapVertex;
					pqi.decreaseKey(v);
					
				}
			}
		}
		return wmst;
	}

	class PrimVertex2 implements Index {
		PrimVertex primv;
		int d;
		int index;
		Vertex mapVertex;

		PrimVertex2(PrimVertex primv, int d, Vertex Source) {

			this.d = d;
			this.primv = primv;
			this.mapVertex = Source;
		}

		@Override
		public void putIndex(int i) {
			index = i;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public String toString() {
			return (this.index + 1) + "";
		}

	}

	public static void main(String[] args) throws FileNotFoundException, PQException {
		Scanner in;

		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}

		Graph g = Graph.readGraph(in);
		Graph.Vertex s = g.getVertex(7);

		Timer timer = new Timer();
		PrimMST mst = new PrimMST(g);
		timer.start();
		int wmst = mst.prim1(s);
		timer.end();
		System.out.println("Weight of MST with prim1 algorithm:" + wmst);
		System.out.println(timer);
		mst.initialize(g);

		timer.start();
		
		mst.CreateQueue(g);

		wmst = mst.prim2(s);
		timer.end();
		System.out.println("Weight of MST with prim2 algorithm:" + wmst);
		System.out.println(timer);
	}

}
