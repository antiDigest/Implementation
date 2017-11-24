// Starter code for LP7
package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

import java.util.HashMap;
import java.util.Set;

import static java.lang.Math.abs;

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
        s = d.source;
        t = d.sink;
        minCutS = d.minCutS();
        minCutT = d.minCutT();
        return maxFlow;
    }

    // Return max flow found by relabelToFront algorithm
    public int relabelToFront() {
        RelabelToFront rtf = new RelabelToFront(g, s, t, capacity);
        rtf.relabelToFront();
        g = rtf.g;
        s = rtf.source;
        t = rtf.sink;
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

    boolean verify() {
        int outFlow = 0;
        XGraph.XVertex xsource = (XGraph.XVertex) s;
        for (Edge e : xsource) {
            XGraph.XEdge xe = (XGraph.XEdge) e;
            outFlow += flow(xe);
        }

        int inFlow = 0;
        XGraph.XVertex xsink = (XGraph.XVertex) t;
        for (Edge e : xsink.xrevAdj) {
            XGraph.XEdge xe = (XGraph.XEdge) e;
            inFlow += flow(xe);
        }

        if (inFlow != outFlow
                && inFlow != abs(((XGraph.XVertex) s).excess)
                && outFlow != abs(((XGraph.XVertex) t).excess)) {
            System.out.println("Invalid: total flow from the source (" + outFlow + ") != total flow to the sink (" + inFlow + ")");
            return false;
        }

        for(Vertex u: g){
            XGraph.XVertex xu = (XGraph.XVertex) u;
            xu.seen = false;
        }

        for (Vertex u : g) {
            if (u != s && u != t) {
                XGraph.XVertex xu = (XGraph.XVertex) u;
                int outVertexFlow = 0;
                for (Edge e : xu) {
                    XGraph.XEdge xe = (XGraph.XEdge) e;
                    outVertexFlow += flow(xe);
                }

                int inVertexFlow = 0;
                for (Edge e : xu.xrevAdj) {
                    XGraph.XEdge xe = (XGraph.XEdge) e;
                    inVertexFlow += flow(xe);
                }

                if (inVertexFlow != outVertexFlow) {
                    System.out.println("Invalid: Total incoming flow != Total outgoing flow at " + u);
                    return false;
                }
            }
        }

        if (minCutS.size() + minCutT.size() != g.size()) {
            System.out.println("Invalid size of Min-Cut: " + (minCutS.size() + minCutT.size()) + " vs " + g.size());
            return false;
        }

        return true;
    }
}