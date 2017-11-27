// Starter code for LP7
package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

import java.util.*;

public class Flow {
    HashMap<Edge, Integer> capacity;
    Graph g;
    Vertex s;
    Vertex t;

    public Flow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity) {
        this.g = new XGraph(g, capacity);
        this.s = ((XGraph) this.g).getVertex(s);
        this.t = ((XGraph) this.g).getVertex(t);
        this.capacity = capacity;
    }

    // Return max flow found by Dinitz's algorithm
    public int dinitzMaxFlow() {
        Dinitz d = new Dinitz(g, s, t);
        int maxFlow = d.maxFlow();
        g = d.g;
        s = d.source;
        t = d.sink;
        return maxFlow;
    }

    // Return max flow found by relabelToFront algorithm
    public int relabelToFront() {
        RelabelToFront rtf = new RelabelToFront(g, s, t);
        rtf.relabelToFront();
        g = rtf.g;
        s = rtf.source;
        t = rtf.sink;
        return rtf.maxFlow();
    }

    // flow going through edge e
    public int flow(Edge e) {
        return xgraph(g).flow(e);
    }

    // capacity of edge e
    public int capacity(Edge e) {
        return xgraph(g).capacity(e);
    }

    /**
     * All vertices reachable from the src vertex
     *
     * @param src: start vertex (could be source or sink)
     * @return Set of Vertices reachable from src
     */
    static Set<Vertex> reachableFrom(Graph g, Vertex src) {
        Set<Graph.Vertex> minCut = new LinkedHashSet<>();
        Queue<Vertex> q = new LinkedList<>();
        minCut.add(src);
        q.add(src);
        while (!q.isEmpty()) {
            Vertex u = q.remove();
            XGraph.XVertex xu = (XGraph.XVertex) u;
            for (Graph.Edge e : xu) {
                Vertex v = e.otherEnd(u);
                if (!xgraph(g).seen(v) && inResidualGraph(g, u, e)) {
                    xgraph(g).setSeen(v);
                    minCut.add(v);
                    q.add(v);
                }
            }
        }
        return minCut;
    }

    static XGraph xgraph(Graph g) {
        return (XGraph) g;
    }

    /**
     * Edge out of u in Residual Graph (Gf) because of e ?
     *
     * @param u From vertex
     * @param e Edge (u, ?)
     * @return true if in residual graph, else false
     */
    static boolean inResidualGraph(Graph g, Vertex u, Edge e) {
        return e.fromVertex().equals(u) ? xgraph(g).flow(e) < xgraph(g).capacity(e) : xgraph(g).flow(e) > 0;
    }

    /**
     * After maxflow has been computed, this method can be called to
     * get the "S"-side of the min-cut found by the algorithm
     *
     * @return Set
     */
    Set<Graph.Vertex> minCutS() {
        return reachableFrom(g, s);
    }

    /**
     * After maxflow has been computed, this method can be called to
     * get the "T"-side of the min-cut found by the algorithm
     *
     * @return Set
     */
    Set<Graph.Vertex> minCutT() {
        return reachableFrom(g, t);
    }

    boolean verify() {
        int outFlow = 0;
        int inFlow = 0;
        XGraph.XVertex xsource = (XGraph.XVertex) s;
        for (Edge e : xsource.xadj) {
            XGraph.XEdge xe = ((XGraph) g).getEdge(e);
            if(e.fromVertex().equals(s))
                outFlow += flow(xe);
        }
        XGraph.XVertex xsink = (XGraph.XVertex) t;
        for (Edge e : xsink.xadj) {
            XGraph.XEdge xe = ((XGraph) g).getEdge(e);
            if(e.toVertex().equals(t))
                inFlow += flow(xe);
        }

        if (inFlow != outFlow) {
            System.out.println("Invalid: total flow from the source (" + outFlow + ") " +
                    "!= total flow to the sink (" + inFlow + ")");
            return false;
        }

        for (Vertex u : g) {
            if (u != s && u != t) {
                int outVertexFlow = 0;
                int inVertexFlow = 0;
                for (Edge e : xgraph(g).getAdj(u)) {
                    XGraph.XEdge xe = (XGraph.XEdge) e;
                    if (!e.fromVertex().equals(u))
                        inVertexFlow += flow(xe);
                    else
                        outVertexFlow += flow(xe);
                }

                if (inVertexFlow != outVertexFlow) {
                    System.out.println("Invalid: Total incoming flow (" + inVertexFlow + ") " +
                            "!= Total outgoing flow (" + outVertexFlow + ") at " + u);
                    return false;
                }
            }
        }

        Set<Vertex> minCutS = minCutS();
        Set<Vertex> minCutT = minCutT();
        if (minCutS.size() + minCutT.size() != g.size()) {
            System.out.println("Invalid size of Min-Cut: " +
                    (minCutS.size() + minCutT.size()) + " vs " + g.size());
            return false;
        }

        return true;
    }
}