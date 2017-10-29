/**
 * Class to represent a graph
 *  @author swaroop, saikumar, antriksh
 *
 */

/**
 * Class to represent a graph
 *  @author swaroop, saikumar, antriksh
 *
 */

package cs6301.g1025;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.ListIterator;
import java.util.Scanner;

import cs6301.g1025.Graph.Vertex;

public class SCC extends GraphAlgorithm<SCC.SCCVertex> {

	int components = 0;// no of components in the SCC
	ArrayList<Graph.Vertex> vertices = new ArrayList<Graph.Vertex>();// list of
																		// vertices
	ArrayList<Graph.Vertex> lstOrder = new ArrayList<Graph.Vertex>();// list of
																		// vertices
																		// visited
																		// by
																		// DFS
																		// order

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
	class SCCVertex {
		boolean seen;
		int componentno;

		SCCVertex(Graph.Vertex u) {
			seen = false;
			componentno = 0;

		}

	}

	/**
	 * Initializing DFS
	 * 
	 * @return List of vertices visited (Finish order)
	 */
	void dfs() {
		components = 0;
		this.vertices = (ArrayList<Graph.Vertex>) this.lstOrder.clone();
		this.lstOrder.clear();
		ListIterator<Graph.Vertex> itr = this.vertices.listIterator(vertices.size());
		while (itr.hasPrevious()) {
			Graph.Vertex u = itr.previous();
			if (!seen(u)) {
				assignComponentno(u, components++);
				dfsUtil(u);
			}
		}
	}

	/**
	 * DFS Visit procedure
	 * 
	 * @param u:
	 *            start vertex
	 * @param l:
	 *            List of vertices storing finish order
	 */
	void dfsUtil(Graph.Vertex u) {
		visit(u);

		for (Graph.Edge e : u) {
			Graph.Vertex v = e.otherEnd(u);
			if (!seen(v)) {
				assignComponentno(v, getComponentno(u));
				dfsUtil(v);
			}
		}

		this.lstOrder.add(u);

	}

	/**
	 * Main program to find SCC
	 * 
	 * @param g:
	 *            graph
	 * @return Number of components
	 */
	int stronglyConnectedComponents(Graph g) {
		// System.out.println("Given");
		// System.out.println(g.toString());
		for (Vertex v : g) {
			lstOrder.add(v);
		}
		this.dfs();
		reinitialize();
		g.reverseGraph();
		// System.out.println("After reverse");
		// System.out.println(g.toString());

		this.dfs();
		for (Graph.Vertex u : g) {
			// System.out.println(u+" "+"is in "+getComponentno(u));
		}
		g.reverseGraph();

		return this.components;

	}

	/**
	 * Assign Component Number to vertex
	 * 
	 * @param u:
	 *            vertex
	 * @param componentno:
	 *            integer
	 */
	void assignComponentno(Graph.Vertex u, int componentno) {

		SCCVertex sc = getVertex(u);
		sc.componentno = componentno;
		// System.out.println(u+" "+"is in "+getComponentno(u));
		// System.out.println(sc.componentno);
	}

	/**
	 * 
	 * @param u:
	 *            vertex return Component Number of vertex
	 */
	int getComponentno(Graph.Vertex u) {
		SCCVertex sc = getVertex(u);
		return sc.componentno;
	}

	/**
	 * u seen or not
	 * 
	 * @param u
	 * @return: boolean
	 */
	boolean seen(Graph.Vertex u) {
		return getVertex(u).seen;
	}

	/**
	 * Visit v
	 * 
	 * @param v:
	 *            Vertex
	 */
	void visit(Graph.Vertex v) {
		SCCVertex sc = getVertex(v);
		sc.seen = true;

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
		for (Graph.Vertex u : graph) {
			System.out.println(u + " " + "is in " + sc.getComponentno(u));
		}

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
