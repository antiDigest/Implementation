package cs6301.g1025;

public class GraphAlgorithm<T> {
    public XGraph g;
    // Algorithm uses a parallel array for storing information about vertices
    public T[] node;

    public GraphAlgorithm(XGraph g) {
        this.g = g;
    }

    protected T getVertex(XGraph.Vertex u) {
        return XGraph.Vertex.getVertex(node, u);
    }
}
