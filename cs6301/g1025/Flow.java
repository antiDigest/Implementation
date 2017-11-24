// Starter code for LP7
package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

import java.util.*;

import static cs6301.g1025.RelabelToFront.inResidualGraph;
import static java.lang.Math.abs;

public class Flow {
    HashMap<Edge, Integer> capacity;
    Graph g;
    Vertex s;
    Vertex t;

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
        Set<Vertex> minCutS = minCutS();
        Set<Vertex> minCutT = minCutT();
        System.out.println(minCutS);
        System.out.println(minCutT);
        return maxFlow;
    }

    // Return max flow found by relabelToFront algorithm
    public int relabelToFront() {
        RelabelToFront rtf = new RelabelToFront(g, s, t, capacity);
        rtf.relabelToFront();
        g = rtf.g;
        s = rtf.source;
        t = rtf.sink;
        Set<Vertex> minCutS = minCutS();
        Set<Vertex> minCutT = minCutT();
        System.out.println(minCutS);
        System.out.println(minCutT);
        return rtf.maxFlow();
    }

    // flow going through edge e
    public int flow(Edge e) {
        return ((XGraph) g).flow(e);
    }

    // capacity of edge e
    public int capacity(Edge e) {
        if(xgraph(g).capacity(e) != capacity.get(e)){
            xgraph(g).setCapacity(e, capacity.get(e));
        }
        return capacity.get(e);
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
            for (Graph.Edge e : xgraph(g).getAdj(u)) {
                Vertex v = e.otherEnd(u);
                if (!xgraph(g).seen(v) && inResidualGraph(g, u, e)) {
                    xgraph(g).setSeen(v);
                    minCut.add(v);
                    q.add(v);
                }
            }

            for (Graph.Edge e : xgraph(g).getRevAdj(u)) {
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

    public static XGraph xgraph(Graph g){
        return (XGraph) g;
    }

    /**
     * After maxflow has been computed, this method can be called to
     * get the "S"-side of the min-cut found by the algorithm
     *
     * @return Set
     */
    Set<Graph.Vertex> minCutS() {
        for (Vertex u : g) {
            xgraph(g).resetSeen(u);
        }
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

        Set<Vertex> minCutS = minCutS();
        Set<Vertex> minCutT = minCutT();

        if (minCutS.size() + minCutT.size() != g.size()) {
            System.out.println("Invalid size of Min-Cut: " + (minCutS.size() + minCutT.size()) + " vs " + g.size());
            return false;
        }

        return true;
    }
}