package sp3.cs6301.g1025;

import cs6301.g00.Graph;

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
}
