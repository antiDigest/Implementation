<<<<<<< HEAD
package cs6301.g00;

public class GraphAlgorithm<T> {
    Graph g;
    // Algorithm uses a parallel array for storing information about vertices
    public T[] node;

    public GraphAlgorithm(Graph g) {
        this.g = g;
    }

    T getVertex(Graph.Vertex u) {
        return Graph.Vertex.getVertex(node, u);
    }
=======

package cs6301.g00;

public class GraphAlgorithm<T> {
	Graph g;
	// Algorithm uses a parallel array for storing information about vertices
	public T[] node;

	public GraphAlgorithm(Graph g) {
		this.g = g;
	}

	T getVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
}
