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

import static cs6301.g1025.Flow.xgraph;
import static cs6301.g1025.RelabelToFront.inResidualGraph;
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
            for (Graph.Edge e : xgraph(g).getAdj(u)) {
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

    int maxFlow() {

        while (true) {
            BFS(source);
            if (xgraph(g).getDistance(sink) == INFINITY) break;

            bfsInit();
            Set<List<PathEdge>> paths = new LinkedHashSet<>();
            List<PathEdge> path = new ArrayList<>();
            getAllPaths(paths, path, 0, source);

            System.out.println("Number of paths: " + paths.size());

            for (List<PathEdge> pathEdges : paths) {
                int cMin = minCapacity(pathEdges);
//                System.out.println("Path: " + pathEdges);
//                System.out.println("Capacity: " + cMin);
                for (PathEdge pe : pathEdges) {
                    Edge e = pe.e;
                    Vertex u = pe.from;

                    if (e.fromVertex().equals(u)) {
                        xgraph(g).setFlow(e, xgraph(g).flow(e) + cMin );
                    } else {
                        xgraph(g).setFlow(e, xgraph(g).flow(e) - cMin );
                    }
                }
            }
        }

        bfsInit();

        int flow = 0;
        for (Edge e : xgraph(g).getRevAdj(sink)) {
            flow += xgraph(g).flow(e);
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
            Edge e = pe.e;
            if (minCapacity > xgraph(g).capacity(e)) {
                minCapacity = xgraph(g).capacity(e);
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
        if (dist > xgraph(g).getDistance(sink)) {
            return path;
        }
        if (src.equals(sink) && dist == xgraph(g).getDistance(sink)) {
            List<PathEdge> pathEdges = new ArrayList<>();
            pathEdges.addAll(path);
            paths.add(pathEdges);
        } else {
            for (Edge e : xgraph(g).getAdj(src)) {
                Vertex v = e.otherEnd(src);
                relax(src, e, v, paths, path, dist);
            }
            for (Edge e : xgraph(g).getRevAdj(src)) {
                Vertex v = e.otherEnd(src);
                relax(src, e, v, paths, path, dist);
            }
        }
        return path;
    }

    void relax(Vertex src, Edge e, Vertex v, Set<List<PathEdge>> paths, List<PathEdge> path, int dist) {
        XVertex xv = (XVertex) v;
        if (inResidualGraph(g, src, e) && !xgraph(g).seen(v) && xgraph(g).getDistance(v) == dist + 1) {
            path.add(new PathEdge(e, src, v));
            xgraph(g).setSeen(v);
            path = getAllPaths(paths, path, dist + 1, v);
            if (!path.isEmpty()) {
                path.remove(dist);
                xgraph(g).resetSeen(v);
            }
        }
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
}
