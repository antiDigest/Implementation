/**
 * Class to represent a graph
 *  @author swaroop, saikumar, antriksh
 *
 */

package cs6301.g1025;

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

	/**
	 * Wrapper to store component numbers
	 */
	static class SCCVertex {
		boolean seen;
		int componentno;

		SCCVertex(Graph.Vertex u) {
			seen = false;
			componentno = 0;

		}
	}

	/**
	 * Assign Component Number to vertex
	 * @param u: vertex
	 * @param componentno: integer
	 */
	void assignComponentno(Graph.Vertex u, int componentno) {

		SCCVertex sc = getVertex(u);
		sc.componentno = componentno;
	}

	/**
	 * u seen or not
	 * @param u
	 * @return: boolean
	 */
	boolean seen(Graph.Vertex u) {
		return getVertex(u).seen;
	}

	/**
	 * Visit v
	 * @param v: Vertex
	 */
	void visit(Graph.Vertex v) {
		SCCVertex sc = getVertex(v);
		sc.seen = true;

	}

	/**
	 * Reverse DFS
	 * @param lst: List of Vertices
	 * @return number of components
	 */
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

	/**
	 * Utility to reverse graph
	 * @param u: Vertex
	 * @param componentno: preserve component number of vertex
	 */
	void dfsRevUtil(Graph.Vertex u, int componentno) {
		visit(u);
		assignComponentno(u, componentno);
		for (Graph.Edge e : u.revAdj) {
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

	/**
	 * Initializing DFS
	 * @return List of vertices visited (Finish order)
	 */
	List<Graph.Vertex> dfsStraight() {
		List<Graph.Vertex> l = new ArrayList<Graph.Vertex>();
		for (Graph.Vertex u : this.g) {
			if (!seen(u)) {
				dfsStraightUtil(u, l);
			}
		}
		return l;

	}

	/**
	 * DFS Visit procedure
	 * @param u: start vertex
	 * @param l: List of vertices storing finish order
	 */
	void dfsStraightUtil(Graph.Vertex u, List<Graph.Vertex> l) {
		visit(u);

		for (Graph.Edge e : u.adj) {
			Graph.Vertex v = e.otherEnd(u);

			if (!seen(v)) {
				dfsStraightUtil(v, l);
			}
		}

		l.add(u);

	}

	/**
	 * Main program to find SCC
	 * @param g: graph
	 * @return Number of components
	 */
	int stronglyConnectedComponents(Graph g) {
		List<Graph.Vertex> order = this.dfsStraight();

		reinitialize();
		return this.dfsRev(order);

	}

	/**
	 * Re-Initialise graph, reset everything
	 */
	void reinitialize() {

		for (Graph.Vertex u : g) {
			SCCVertex v = this.getVertex(u);
			v.seen = false;

		}
	}

}
