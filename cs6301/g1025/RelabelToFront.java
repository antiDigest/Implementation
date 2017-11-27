package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static cs6301.g1025.Flow.inResidualGraph;
import static cs6301.g1025.Flow.xgraph;
import static cs6301.g1025.XGraph.INFINITY;
import static java.lang.Math.min;
import static java.lang.Math.abs;
import static java.lang.Math.max;


public class RelabelToFront {

    Graph g;
    Vertex source;
    Vertex sink;

    RelabelToFront(Graph g, Vertex source, Vertex sink) {
        this.g = g;
        this.source = this.g.getVertex(source.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    /**
     * Initializing Max-Flow Relabel to Front
     */
    void initialize() {

        xgraph(g).setHeight(source, g.size());

        for (Edge e : source) {
            Vertex u = e.otherEnd(source);
            xgraph(g).setFlow(e, xgraph(g).capacity(e));
            xgraph(g).setExcess(source, xgraph(g).excess(source) - xgraph(g).capacity(e));
            xgraph(g).setExcess(u, xgraph(g).excess(u) + xgraph(g).capacity(e));
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

        int delta = min(xgraph(g).excess(u), xgraph(g).capacity(e) - xgraph(g).flow(e));
        if (!e.fromVertex().equals(u)) {
            delta = min(xgraph(g).excess(u), xgraph(g).flow(e));
        }

        if (e.fromVertex().equals(u)) {
            xgraph(g).setFlow(e, xgraph(g).flow(e) + delta);
        } else {
            xgraph(g).setFlow(e, xgraph(g).flow(e) - delta);
        }

        xgraph(g).setExcess(u, xgraph(g).excess(u) - delta);
        xgraph(g).setExcess(v, xgraph(g).excess(v) + delta);
    }

    /**
     * increase the height of u, to allow u to get rid of its excess
     * Precondition: u.excess > 0, and for all ( u, v ) in Gf, u.height <= v.height
     *
     * @param u Vertex
     */
    void relabel(Vertex u) {

        int minHeight = INFINITY;

        for (Edge e : u) {
            Vertex v = e.otherEnd(u);
            if (xgraph(g).height(u) > xgraph(g).height(v)) continue;
            if (xgraph(g).height(v) < minHeight) {
                minHeight = xgraph(g).height(v);
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

        while (xgraph(g).excess(u) > 0) {
            for (Edge e : u) {
                Vertex v = e.otherEnd(u);
                if (xgraph(g).height(u) == 1 + xgraph(g).height(v)) {
                    push(u, v, e);
                    if (xgraph(g).excess(u) == 0) return;
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
                if (xgraph(g).excess(u) == 0) {
                    u = next(it);
                    continue;
                }
                int oldHeight = xgraph(g).height(u);

                discharge(u);

                if (xgraph(g).height(u) != oldHeight) {
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

        for (Vertex u : g) {
            xgraph(g).resetSeen(u);
        }
    }

    /**
     * gives max flow value: excess value at sink node
     *
     * @return int
     */
    int maxFlow() {
        return abs(xgraph(g).excess(sink));
    }

    Vertex next(Iterator<Vertex> it) {
        return it.hasNext() ? it.next() : null;
    }

}
