package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;

import java.util.*;

import static cs6301.g1025.Flow.reachableFrom;
import static cs6301.g1025.Flow.xgraph;
import static cs6301.g1025.XGraph.INFINITY;
import static java.lang.Integer.min;
import static java.lang.Math.abs;


public class RelabelToFront {

    Graph g;
    Vertex source;
    Vertex sink;
    HashMap<Edge, Integer> capacity;

    RelabelToFront(Graph g, Vertex source, Vertex sink, HashMap<Edge, Integer> capacity) {
        this.g = new XGraph(g, capacity);
        this.source = this.g.getVertex(source.getName());
        this.sink = this.g.getVertex(sink.getName());
        this.capacity = capacity;
    }

    RelabelToFront(Graph g, Vertex source, Vertex sink) {
        this.g = new XGraph(g);
        this.source = this.g.getVertex(source.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    /**
     * Initializing Max-Flow Relabel to Front
     */
    void initialize() {

        xgraph(g).setHeight(source, g.size());

        for (Edge e : xgraph(g).getAdj(source)) {
            Vertex u = e.otherEnd(source);
            xgraph(g).setFlow(e, xgraph(g).capacity(e));
            xgraph(g).setExcess(source, xgraph(g).getExcess(source) - xgraph(g).capacity(e));
            xgraph(g).setExcess(u, xgraph(g).getExcess(u) + xgraph(g).capacity(e));
        }
    }

    /**
     * Push flow out of u using e
     * edge (u, v) = e: is in Residual Graph (Gf)
     *
     * @param u From Vertex
     * @param v To Vertex
     * @param e Edge: (u, v)
     */
    void push(Vertex u, Vertex v, Edge e) {

        int delta = min(xgraph(g).getExcess(u), xgraph(g).capacity(e));

        if (e.fromVertex().equals(u)) {
            xgraph(g).setFlow(e, xgraph(g).flow(e) + delta);
        } else {
            xgraph(g).setFlow(e, xgraph(g).flow(e) - delta);
        }

        xgraph(g).setExcess(u, xgraph(g).getExcess(u) - delta);
        xgraph(g).setExcess(v, xgraph(g).getExcess(v) + delta);
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
     * increase the height of u, to allow u to get rid of its excess
     * Precondition: u.excess > 0, and for all ( u, v ) in Gf, u.height <= v.height
     *
     * @param u Vertex
     */
    void relabel(Vertex u) {
        int minHeight = INFINITY;
        for (Edge e : xgraph(g).getAdj(u)) {
            Vertex v = e.otherEnd(u);
            if (xgraph(g).getHeight(u) <= xgraph(g).getHeight(v)
                    && xgraph(g).getHeight(v) < minHeight && inResidualGraph(g, u, e)) {
                minHeight = xgraph(g).getHeight(v);
            }
        }
        for (Edge e : xgraph(g).getRevAdj(u)) {
            Vertex v = e.otherEnd(u);
            if (xgraph(g).getHeight(u) <= xgraph(g).getHeight(v)
                    && xgraph(g).getHeight(v) < minHeight && inResidualGraph(g, u, e)) {
                minHeight = xgraph(g).getHeight(v);
            }
        }

        xgraph(g).setHeight(u, 1 + minHeight);
    }

    /**
     * push all excess flow out of u, raising its height, as needed
     *
     * @param u Vertex
     */
    void discharge(Vertex u) {

        while (xgraph(g).getExcess(u) > 0) {
            for (Edge e : xgraph(g).getAdj(u)) {
                Vertex v = e.otherEnd(u);
                if (inResidualGraph(g, u, e) && xgraph(g).getHeight(u) == 1 + xgraph(g).getHeight(v)) {
                    push(u, v, e);
                    if (xgraph(g).getExcess(v) == 0) return;
                }
            }
            for (Edge e : xgraph(g).getRevAdj(u)) {
                Vertex v = e.otherEnd(u);
                if (inResidualGraph(g, u, e) && xgraph(g).getHeight(u) == 1 + xgraph(g).getHeight(v)) {
                    push(u, v, e);
                    if (xgraph(g).getExcess(v) == 0) return;
                }
            }
            relabel(u);
        }

    }

    /**
     * Algorithm to find max flow !
     */
    void relabelToFront() {
        initialize();
        List<Vertex> L = new LinkedList<>();
        for (Vertex u : g) {
            if (!u.equals(source) && !u.equals(sink)) {
                L.add(u);
            }
        }

        boolean done = false;

        while (!done) {

            Iterator<Vertex> it = L.iterator();
            done = true;
            Vertex u = next(it);
            while (u != null) {
                if (xgraph(g).getExcess(u) == 0) {
                    u = next(it);
                    continue;
                }
                int oldHeight = xgraph(g).getHeight(u);
                discharge(u);

                if (xgraph(g).getHeight(u) != oldHeight) {
                    done = false;
                    break;
                }
                u = next(it);
            }

            if (!done) {
                it.remove();
                L.add(0, u);
            }
        }
    }

    /**
     * gives max flow value: excess value at sink node
     *
     * @return int
     */
    int maxFlow() {
        return abs(xgraph(g).getExcess(sink));
    }

    Vertex next(Iterator<Vertex> it) {
        return it.hasNext() ? it.next() : null;
    }

}
