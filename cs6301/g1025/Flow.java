// Starter code for LP7
package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

import java.util.HashMap;
import java.util.Set;

public class Flow {
    HashMap<Edge, Integer> capacity;
    Graph g;
    Vertex s;
    Vertex t;
    Set<Vertex> minCutS;
    Set<Vertex> minCutT;

    public Flow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity) {
        this.g = new XGraph(g);
        this.s = s;
        this.t = t;
        this.capacity = capacity;
    }

    // Return max flow found by Dinitz's algorithm
    public int dinitzMaxFlow() {
        Dinitz d = new Dinitz(g, s, t, capacity);
        int maxFlow = d.maxFlow();
        g = d.g;
        minCutS = d.minCutS();
        minCutT = d.minCutT();
        return maxFlow;
    }

    // Return max flow found by relabelToFront algorithm
    public int relabelToFront() {
        RelabelToFront rtf = new RelabelToFront(g, s, t, capacity);
        rtf.relabelToFront();
        g = rtf.g;
        minCutS = rtf.minCutS();
        minCutT = rtf.minCutT();
        return rtf.maxFlow();
    }

    // flow going through edge e
    public int flow(Edge e) {
        return ((XGraph) g).flow(e);
    }

    // capacity of edge e
    public int capacity(Edge e) {
        if(((XGraph) g).capacity(e) != capacity.get(e)){
            ((XGraph) g).setCapacity(e, capacity.get(e));
        }
        return capacity.get(e);
    }

    /* After maxflow has been computed, this method can be called to
       get the "S"-side of the min-cut found by the algorithm
    */
    public Set<Vertex> minCutS() {
        return minCutS;
    }

    /* After maxflow has been computed, this method can be called to
       get the "T"-side of the min-cut found by the algorithm
    */
    public Set<Vertex> minCutT() {
        return minCutT;
    }
}