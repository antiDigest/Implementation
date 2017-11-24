/**
 * @author Antriksh, Gunjan, Saikumar, Swaroop
 * Version 1: Implementing
 */

package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XEdge;
import cs6301.g1025.XGraph.XVertex;

import java.util.*;

import static cs6301.g1025.XGraph.INFINITY;


public class Dinitz {

    Graph g;
    Graph.Vertex source;
    Graph.Vertex sink;

    class PathEdge {
        Edge e;
        Vertex from;
        Vertex to;

        PathEdge(Edge e, Vertex from, Vertex to) {
            this.e = e;
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return this.e + "";
        }
    }

    Dinitz(Graph g, Graph.Vertex src, Graph.Vertex sink, HashMap<Graph.Edge, Integer> capacity) {
        this.g = new XGraph(g, capacity);
        this.source = this.g.getVertex(src.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    void bfsInit() {
        for (Vertex u : g) {
            XVertex xu = (XVertex) u;
            xu.seen = false;
        }
    }

    void BFS(Vertex src) {
        for (Vertex u : g) {
            XVertex xu = (XVertex) u;
            xu.seen = false;
            xu.parent = null;
            xu.distance = INFINITY;
        }

        Queue<Graph.Vertex> q = new LinkedList<>();

        ((XVertex) src).distance = 0;
        q.add(src);

        while (!q.isEmpty()) {
            Vertex u = q.remove();
            XGraph.XVertex xu = (XGraph.XVertex) u;
            for (Graph.Edge e : xu) {
                Vertex v = e.otherEnd(u);
                XVertex xv = (XVertex) v;
                if (!xv.seen && RelabelToFront.inResidualGraph(u, e)) {
                    visit(u, v, e);
                    q.add(v);
                }
            }
        }

    }

    void visit(Vertex u, Vertex v, Edge e) {
        XVertex xv = (XVertex) v;
        XVertex xu = (XVertex) u;
        XEdge xe = (XEdge) e;
        xv.seen = true;
        xv.parent = u;
        xv.parentEdge = xe;
        xv.distance = xu.distance + 1;
    }

    int maxFlow() {

        while (true) {
            BFS(source);
            XVertex xsink = (XVertex) sink;
            if (xsink.distance == INFINITY && xsink.parent == null) break;

            bfsInit();
            Set<List<PathEdge>> paths = new LinkedHashSet<>();
            List<PathEdge> path = new ArrayList<>();
            getAllPaths(paths, path, 0, source);

            System.out.println("Number of paths: " + paths.size());

            for (List<PathEdge> pathEdges : paths) {
                int cMin = minCapacity(pathEdges);
                for (PathEdge pe : pathEdges) {
                    XEdge xe = (XEdge) pe.e;

                    if (xe.fromVertex().equals(pe.from)) {
                        xe.flow = xe.flow + cMin;
                    } else {
                        xe.flow = xe.flow - cMin;
                    }
                }
            }
        }

        int flow = 0;
        XVertex xsink = (XVertex) sink;
        for (Edge e : xsink.xrevAdj) {
            XEdge xe = (XEdge) e;
            flow += xe.flow();
        }
        return flow;
    }

    /**
     * Calculate the minimum flow through the path
     *
     * @param path List of edges in the path
     * @return Minimum path capacity
     */
    int minCapacity(List<PathEdge> path) {
        int minCapacity = INFINITY;
        for (PathEdge pe : path) {
            XEdge xe = (XEdge) pe.e;
            if (minCapacity > xe.capacity()) {
                minCapacity = xe.capacity();
            }
        }
        return minCapacity;
    }

    /**
     * Enumerate all paths from source to sink at a distance sink.distance
     *
     * @param paths: Storing all paths from s to t
     * @param path:  Enumerating current path from s to t
     * @param dist:  distance reached from s
     * @param src:   current node visiting
     * @return List of edges of one of the paths
     */
    List<PathEdge> getAllPaths(Set<List<PathEdge>> paths, List<PathEdge> path, int dist, Vertex src) {
        if (dist > ((XVertex) sink).distance) {
            return path;
        }
        if (src.equals(sink) && dist == ((XVertex) sink).distance) {
            List<PathEdge> pathEdges = new ArrayList<>();
            pathEdges.addAll(path);
            paths.add(pathEdges);
        } else {
            for (Edge e : src) {
                Vertex v = e.otherEnd(src);
                relax(src, e, v, paths, path, dist);
            }
            for (Edge e : ((XVertex) src).xrevAdj) {
                Vertex v = e.otherEnd(src);
                relax(src, e, v, paths, path, dist);
            }
        }
        return path;
    }

    void relax(Vertex src, Edge e, Vertex v, Set<List<PathEdge>> paths, List<PathEdge> path, int dist) {
        XVertex xv = (XVertex) v;
        if (RelabelToFront.inResidualGraph(src, e) && !seen(v) && xv.distance == dist + 1) {
            path.add(new PathEdge(e, src, v));
            setSeen(v);
            path = getAllPaths(paths, path, dist + 1, v);
            if (!path.isEmpty()) {
                path.remove(dist);
                resetSeen(v);
            }
        }
    }

    /**
     * All vertices reachable from the src vertex
     *
     * @param src: start vertex (could be source or sink)
     * @return Set of Vertices reachable from src
     */
    static Set<Vertex> reachableFrom(Vertex src) {
        Set<Graph.Vertex> minCut = new LinkedHashSet<>();
        Queue<Graph.Vertex> q = new LinkedList<>();
        minCut.add((XVertex) src);
        q.add(src);
        while (!q.isEmpty()) {
            XGraph.XVertex xu = (XGraph.XVertex) q.remove();
            for (Graph.Edge e : xu) {
                XEdge xe = (XEdge) e;
                XGraph.XVertex xv = (XGraph.XVertex) e.otherEnd(xu);
                if (!xv.seen && RelabelToFront.inResidualGraph(xu, e)) {
                    xv.seen = true;
                    minCut.add(xv);
                    q.add(xv);
                }
            }

            for (Graph.Edge e : xu.xrevAdj) {
                XEdge xe = (XEdge) e;
                XGraph.XVertex xv = (XGraph.XVertex) e.otherEnd(xu);
                if (!xv.seen && RelabelToFront.inResidualGraph(xu, e)) {
                    xv.seen = true;
                    minCut.add(xv);
                    q.add(xv);
                }
            }
        }
        return minCut;
    }

    /**
     * Find Min-Cut reachable from s (source)
     *
     * @return Set
     */
    Set<Graph.Vertex> minCutS() {
        return reachableFrom(source);
    }

    /**
     * Find Min-Cut reachable from t (sink)
     *
     * @return Set
     */
    Set<Graph.Vertex> minCutT() {
        return reachableFrom(sink);
    }

    /**
     * Given from and to vertices with an edge, checking if the edge is reverse
     *
     * @param e
     * @param from
     * @return
     */
    boolean isReverse(Edge e, Vertex from) {
        return !(e.fromVertex().equals(from));
    }

    boolean seen(Graph.Vertex u) {
        return ((XVertex) g.getVertex(u.getName())).seen;
    }

    void setSeen(Vertex v) {
        XVertex xv = (XVertex) v;
        xv.seen = true;
    }

    void resetSeen(Vertex v) {
        XVertex xv = (XVertex) v;
        xv.seen = false;
    }
}
