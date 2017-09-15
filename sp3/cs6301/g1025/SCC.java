
package sp3.cs6301.g1025;

import cs6301.g00.Graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SCC extends GraphAlgorithm<SCC.SCCVertex> {

	public SCC(Graph g) {
		super(g);
		node = new SCCVertex[g.size()];
		// Create array for storing vertex properties
		for (Graph.Vertex u : g) {
			node[u.getName()] = new SCCVertex(u);
		}
	}

	static class SCCVertex {
		boolean seen;
		int componentno;

		SCCVertex(Graph.Vertex u) {
			seen = false;
			componentno = 0;

		}
	}

	void assignComponentno(Graph.Vertex u, int componentno) {

		SCCVertex sc = getVertex(u);
		sc.componentno = componentno;
	}

	boolean seen(Graph.Vertex u) {
		return getVertex(u).seen;
	}

	// Visit a node v from u
	void visit(Graph.Vertex v) {
		SCCVertex sc = getVertex(v);
		sc.seen = true;

	}

	int dfsRev(List<Graph.Vertex> lst) {
		int components = 0;

		for (int i=lst.size()-1; i>=0;i--) {
		  Graph.Vertex v=lst.get(i);
			if (!seen(v)) {
				components = components + 1;
				dfsRevUtil(v, components);
			}
		}
		return components;

	}

	void dfsRevUtil(Graph.Vertex u, int componentno) {
		visit(u);
		assignComponentno(u, componentno);
		for (Graph.Edge e : u.getRevAdj()) {
			Graph.Vertex v = e.otherEnd(u);

			if (!seen(v)) {
				dfsRevUtil(v, componentno);
			}
		}

	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner;

		if (args.length > 0) {
			File in = new File(args[0]);
			scanner = new Scanner(in);
		} else {
			System.out.println("Enter the graph: ");
			scanner = new Scanner(System.in);
		}

		Graph graph = Graph.readGraph(scanner, true);
		SCC sc = new SCC(graph);
		System.out.println(sc.stronglyConnectedComponents(graph));

	}

	List<Graph.Vertex> dfsStraight() {
		List<Graph.Vertex> l = new ArrayList<Graph.Vertex>();
		for (Graph.Vertex u : this.g) {
			if (!seen(u)) {
				dfsStraightUtil(u, l);
			}
		}
		return l;

	}

	void dfsStraightUtil(Graph.Vertex u, List<Graph.Vertex> l) {
		visit(u);

		for (Graph.Edge e : u.getAdj()) {
			Graph.Vertex v = e.otherEnd(u);

			if (!seen(v)) {
				dfsStraightUtil(v, l);
			}
		}

		l.add(u);

	}

	int stronglyConnectedComponents(Graph g) {
		List<Graph.Vertex> order = this.dfsStraight();

		reinitialize();
		return this.dfsRev(order);

	}

	void reinitialize() {

		for (Graph.Vertex u : g) {
			SCCVertex v = this.getVertex(u);
			v.seen = false;

		}
	}

}
