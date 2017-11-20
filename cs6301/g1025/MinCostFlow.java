// Starter code for LP8
package cs6301.g1025;
import cs6301.g1025.Graph.*;
import java.util.HashMap;

public class MinCostFlow {

    Graph g;
    Vertex source;
    Vertex sink;
    HashMap<Edge, Integer> capacity;
    HashMap<Edge, Integer> cost;

    public MinCostFlow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity, HashMap<Edge, Integer> cost) {
        this.g = (XGraph) g;
        this.source = s;
        this.sink = t;
        this.capacity = capacity;
        this.cost = cost;
    }

    // Return cost of d units of flow found by cycle cancellation algorithm
    int cycleCancellingMinCostFlow(int d) {
        return 0;
    }

    // Return cost of d units of flow found by successive shortest paths
    int successiveSPMinCostFlow(int d) {
        return 0;
    }

    // Return cost of d units of flow found by cost scaling algorithm
    int costScalingMinCostFlow(int d) {
        CostScaling cs = new CostScaling(g, source, sink, capacity, cost);
        return 0;
    }

    // flow going through edge e
    public int flow(Edge e) {
        return 0;
    }

    // capacity of edge e
    public int capacity(Edge e) {
        return 0;
    }

    // cost of edge e
    public int cost(Edge e) {
        return 0;
    }
}