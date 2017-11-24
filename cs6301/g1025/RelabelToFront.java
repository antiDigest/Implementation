package cs6301.g1025;

import cs6301.g00.Timer;
import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XEdge;
import cs6301.g1025.XGraph.XVertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
        XVertex xsource = (XVertex) source;
        xsource.height = g.size();
        for (Edge e : xsource) {
            XVertex xu = (XVertex) e.otherEnd(xsource);
            XEdge xe = (XEdge) e;
            xe.flow = xe.capacity;
            xsource.excess = xsource.excess - xe.capacity;
            xu.excess = xu.excess + xe.capacity;
        }
    }

    /**
     * Push flow out of u using e
     * edge (u, v) = e: is in Residual Graph (Gf)
     * @param u From Vertex
     * @param v To Vertex
     * @param e Edge: (u, v)
     */
    void push(Vertex u, Vertex v, Edge e) {
        XEdge xe = (XEdge) e;
        XVertex xu = (XVertex) u;
        XVertex xv = (XVertex) v;

        int delta = min(xu.excess, xe.capacity);
        if (xe.fromVertex().equals(xu)) {
            xe.flow = xe.flow + delta;
        } else {
            xe.flow = xe.flow - delta;
        }

        xu.excess -= delta;
        xv.excess += delta;
    }

    /**
     * Edge out of u in Residual Graph (Gf) because of e ?
     * @param u From vertex
     * @param e Edge (u, ?)
     * @return true if in residual graph, else false
     */
    static boolean inResidualGraph(Vertex u, Edge e) {
        XEdge xe = (XEdge) e;
        XVertex xu = (XVertex) u;
        return xe.fromVertex() == xu ? xe.flow() < xe.capacity() : xe.flow() > 0;
    }

    /**
     * increase the height of u, to allow u to get rid of its excess
     * Precondition: u.excess > 0, and for all ( u, v ) in Gf, u.height <= v.height
     * @param u Vertex
     */
    void relabel(Vertex u) {
        XVertex xu = (XVertex) u;

        int minHeight = INFINITY;
        for (Edge e : xu) {
            XVertex xv = (XVertex) e.otherEnd(xu);
            if (xv.height >= xu.height && xv.height < minHeight && inResidualGraph(xu, e)) {
                minHeight = xv.height;
            }
        }
        for (Edge e : xu.xrevAdj) {
            XVertex xv = (XVertex) e.otherEnd(xu);
            if (xv.height >= xu.height && xv.height < minHeight && inResidualGraph(xu, e)) {
                minHeight = xv.height;
            }
        }

        xu.height = 1 + minHeight;
    }

    /**
     * push all excess flow out of u, raising its height, as needed
     * @param u Vertex
     */
    void discharge(Vertex u) {
        XVertex xu = (XVertex) u;
        while (xu.excess > 0) {
            for (Edge e : u) {
                XEdge xe = (XEdge) e;
                XVertex xv = (XVertex) e.otherEnd(xu);
                if (inResidualGraph(xu, xe) && xu.height == 1 + xv.height) {
                    push(xu, xv, xe);
                    if (xu.excess == 0) return;
                }
            }
            for (Edge e : ((XVertex) u).xrevAdj) {
                XEdge xe = (XEdge) e;
                XVertex xv = (XVertex) e.otherEnd(xu);
                if (inResidualGraph(xu, xe) && xu.height == 1 + xv.height) {
                    push(xu, xv, xe);
                    if (xu.excess == 0) return;
                }
            }
            relabel(xu);
        }
    }

    /**
     * Algorithm to find max flow !
     */
    void relabelToFront() {
        initialize();
        List<Vertex> L = new LinkedList<>();
        for (Vertex u : g) {
            if (u != source && u != sink) {
                L.add(u);
            }
        }

        boolean done = false;

        while (!done) {

            Iterator<Vertex> it = L.iterator();
            done = true;
            Vertex u = next(it);
            while (u != null) {
                XVertex xu = (XVertex) u;
                if (xu.excess == 0) {
                    u = next(it);
                    continue;
                }
                int oldHeight = xu.height;
                discharge(xu);

                if (xu.height != oldHeight) {
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
     * @return int
     */
    int maxFlow() {
        XVertex xsink = (XVertex) sink;
        return abs(xsink.excess);
    }

    Vertex next(Iterator<Vertex> it) {
        return it.hasNext() ? it.next() : null;
    }


    /**
     * Find Min-Cut reachable from s (source)
     * @return Set
     */
    Set<Graph.Vertex> minCutS() {
        return Dinitz.reachableFrom(source);
    }

    /**
     * Find Min-Cut reachable from t (sink)
     * @return Set
     */
    Set<Graph.Vertex> minCutT() {
        return Dinitz.reachableFrom(sink);
    }

}
