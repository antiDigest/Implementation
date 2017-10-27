package cs6301.g1025;

public class GraphAlgorithm<T> {
    public Graph g;
    // Algorithm uses a parallel array for storing information about vertices
    public T[] node;

    public GraphAlgorithm(Graph g) {
        this.g = g;
    }

    protected T getVertex(Graph.Vertex u) {
        return Graph.Vertex.getVertex(node, u);
    }
}
