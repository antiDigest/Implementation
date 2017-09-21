package shortProject1;

import java.lang.reflect.Array;
import java.util.Iterator;

import java.util.Scanner;

public class Graph<T> implements Iterable<T> {
	T[] v;
	int n;
	boolean directed;

	/**
	 * Nested class to represent a vertex of a graph
	 */

	/**
	 * Nested class that represents an edge of a Graph
	 */

	/**
	 * Constructor for Graph
	 * 
	 * @param n
	 *            : int - number of vertices
	 */
	Graph(int n) {
		this.n = n;
	
		
		this.v =Array.newInstance(T, n);
		this.directed = false; // default is undirected graph
		// create an array of Vertex objects
		for (int i = 0; i < n; i++)
			v[i] = new T(i);
	}

	/**
	 * Find vertex no. n
	 * 
	 * @param n
	 *            : int
	 */
	Vertex getVertex(int n) {
		return v[n - 1];
	}

	/**
	 * Method to add an edge to the graph
	 * 
	 * @param a
	 *            : int - one end of edge
	 * @param b
	 *            : int - other end of edge
	 * @param weight
	 *            : int - the weight of the edge
	 */
	void addEdge(Vertex from, Vertex to, int weight) {
		Edge e = new Edge(from, to, weight);
		if (this.directed) {
			from.adj.add(e);
			to.revAdj.add(e);
		} else {
			from.adj.add(e);
			to.adj.add(e);
		}
	}

	int size() {
		return n;
	}

	/**
	 * Method to create iterator for vertices of graph
	 */
	public Iterator<T> iterator() {

		return new ArrayIterator<T>(v);
	}

	// read a directed graph using the Scanner interface
	public static Graph readDirectedGraph(Scanner in) {
		return readGraph(in, true);
	}

	// read an undirected graph using the Scanner interface
	public static Graph readGraph(Scanner in) {
		return readGraph(in, false);
	}

	public static Graph readGraph(Scanner in, boolean directed) {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph

		// create a graph instance
		Graph g = new Graph(n);
		g.directed = directed;
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			g.addEdge(g.getVertex(u), g.getVertex(v), w);
		}

		return g;
	}

}
