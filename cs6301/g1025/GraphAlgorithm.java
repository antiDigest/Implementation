package cs6301.g1025;

import java.util.List;

public class GraphAlgorithm<T> {
	// Algorithm uses a parallel array for storing information about vertices
	Graph g;
	public T[] node;

	public GraphAlgorithm(Graph g) {
		this.g = g;
	}

	T getVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}

	public List<Graph.Edge> getAdj(Graph.Vertex u) {
		return u.adj;
	}
	public List<Graph.Edge> getRevAdj(Graph.Vertex u) {
		return u.revAdj;
	}
}
