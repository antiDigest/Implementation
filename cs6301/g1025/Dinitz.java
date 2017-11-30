/**
 * @author Antriksh, Gunjan, Saikumar, Swaroop
 * Version 1: Implementing
 */

package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;
import cs6301.g1025.XGraph.XEdge;

import java.nio.file.Path;
import java.util.*;

import static cs6301.g1025.Flow.inResidualGraph;
import static cs6301.g1025.Flow.xgraph;
import static cs6301.g1025.XGraph.INFINITY;
import static java.lang.Math.min;


public class Dinitz {

    Graph g;
    Graph.Vertex source;
    Graph.Vertex sink;

    Dinitz(Graph g, Graph.Vertex src, Graph.Vertex sink, HashMap<Graph.Edge, Integer> capacity) {
        this.g = new XGraph(g, capacity);
        this.source = this.g.getVertex(src.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    Dinitz(Graph g, Graph.Vertex src, Graph.Vertex sink) {
        this.g = new XGraph(g);
        this.source = this.g.getVertex(src.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    void bfsInit() {
        for (Vertex u : g) {
            xgraph(g).resetSeen(u);
        }
    }

    void BFS(Vertex src) {
        for (Vertex u : g) {
            xgraph(g).resetSeen(u);
            xgraph(g).resetParent(u);
            xgraph(g).resetDistance(u);
        }

        Queue<Graph.Vertex> q = new LinkedList<>();

        xgraph(g).setDistance(src, 0);
        q.add(src);

        while (!q.isEmpty()) {
            Vertex u = q.remove();
            XVertex xu = (XVertex) u;
            for (Graph.Edge e : xu) {
                Vertex v = e.otherEnd(u);
                if (!xgraph(g).seen(v) && inResidualGraph(g, u, e)) {
                    visit(u, v, e);
                    q.add(v);
                }
            }
        }

    }

    void visit(Vertex u, Vertex v, Edge e) {
        xgraph(g).setSeen(v);
        xgraph(g).setParent(v, u);
        xgraph(g).setDistance(v, xgraph(g).getDistance(u) + 1);
    }

    /**
     * Caclulating maximum flow
     *
     * @return
     */
    int maxFlowDinitz() {

        int flow = 0;
        while (true) {
            BFS(source);
            if (xgraph(g).getDistance(sink) == INFINITY)
                break;
            while (true) {
                bfsInit();
                xgraph(g).setSeen(source);
                int minflow = EnumBFSTree(source, INFINITY, source.iterator(), 0);
                if (minflow == -1) {
                    break;
                } else {
                    flow = flow + minflow;
                }
            }
        }
        bfsInit();
        return flow;
    }

    /**
     * Approach:
     * updating minflow for a path while enumerating augmenting paths-
     * (optimizing like reward problem  in lp4 without storing all paths) and
     * skipping over edges with the help of iterator
     *
     * @param s    Vertex
     * @param flow Min flow calculated
     * @param itr  Edge iterator
     * @return Max Flow through the path
     */
    int EnumBFSTree(Vertex s, int flow, Iterator<Edge> itr, int dist) {
        if (dist > xgraph(g).getDistance(sink)) {
            return -1;
        }
        if (s.equals(sink) && dist == xgraph(g).getDistance(sink)) {
            return flow;
        }
        while (itr.hasNext()) {
            Edge e = itr.next();
            XEdge xe = (XEdge) e;
            Vertex v = xe.otherEnd(s);
            if (!xgraph(g).seen(v) && inResidualGraph(g, s, e) && xgraph(g).getDistance(v) == dist + 1) {
                if(xgraph(g).cf(s, e) > 0)
                    flow = min(flow, xgraph(g).cf(s, e));

                xgraph(g).setSeen(v);
                int minedgeflow = EnumBFSTree(xe.otherEnd(s), flow, xe.otherEnd(s).iterator(), dist + 1);

                if (minedgeflow > 0) {
                    if (e.fromVertex().equals(s)) {
                        xgraph(g).setFlow(e, xgraph(g).flow(e) + minedgeflow);
                    } else {
                        xgraph(g).setFlow(e, xgraph(g).flow(e) - minedgeflow);
                    }
                    return minedgeflow;
                }
            }
        }

        // no augmenting path found
        return -1;
    }
}