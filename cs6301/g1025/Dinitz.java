/**
 * @author Antriksh, Gunjan, Saikumar, Swaroop
 * Version 1: Implementing
 */

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

    Dinitz(Graph g, Graph.Vertex src, Graph.Vertex sink) {
        this.g = new XGraph(g);
        this.source = this.g.getVertex(src.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    void bfsInit() {
        for (Vertex u : g) {
            XVertex xu = (XVertex) u;
            xu.seen = false;
        }
    }

    void visit(Vertex v, Vertex u, Edge e) {
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
            BFS bfs = new BFS(g, source);
            bfs.bfs();
            BFS.BFSVertex bsink = bfs.getVertex(sink);
            if (bsink.distance == INFINITY) break;

            Set<List<PathEdge>> paths = new LinkedHashSet<>();
            List<PathEdge> path = new ArrayList<>();
            getAllPaths(paths, path, 0, source, bfs);

            for (List<PathEdge> pathEdges : paths) {
                int cMin = minCapacity(pathEdges);
                for (PathEdge pe : pathEdges) {
                    XEdge xe = (XEdge) pe.e;

                    if (!isReverse(xe, pe.from)) {
                        xe.flow = xe.flow + cMin;
                    } else {
                        xe.flow = xe.flow - cMin;
                    }

                    if(xe.flow() == xe.capacity()){
                        xe.disable();
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

    List<PathEdge> getAllPaths(Set<List<PathEdge>> paths, List<PathEdge> path, int dist, Vertex src, BFS b) {
        if (dist >= b.getVertex(sink).distance && !src.equals(sink)) {
            return path;
        }
        if (src.equals(sink) && dist==b.getVertex(sink).distance) {
            List<PathEdge> pathEdges = new ArrayList<>();
            pathEdges.addAll(path);
            paths.add(pathEdges);
//            return new ArrayList<>();
        } else {
            for (Edge e : src) {
                Vertex v = e.otherEnd(src);
                XVertex xv = (XVertex) v;
                BFS.BFSVertex bv = (b.getVertex(xv));
                if (RelabelToFront.inResidualGraph(src, e) && !xv.seen && bv.distance == dist + 1) {
                    path.add(new PathEdge(e, src, v));
                    xv.seen = true;
                    path = getAllPaths(paths, path, dist + 1, v, b);
                    if (!path.isEmpty()) {
                        path.remove(dist);
                        xv.seen = false;
                    }
                }
            }

            for (Edge e : ((XVertex) src).xrevAdj) {
                Vertex v = e.otherEnd(src);
                XVertex xv = (XVertex) v;
                BFS.BFSVertex bv = (b.getVertex(xv));
                if (RelabelToFront.inResidualGraph(src, e) && !xv.seen && bv.distance == dist + 1) {
                    path.add(new PathEdge(e, src, v));
                    xv.seen = true;
                    path = getAllPaths(paths, path, dist + 1, v, b);
                    if (!path.isEmpty()) {
                        path.remove(dist);
                        xv.seen = false;
                    }
                }
            }
        }
        return path;
    }

    List<Graph.Edge> getPath(Graph.Vertex src, Graph.Vertex snk) {
        List<Graph.Edge> L = new LinkedList<>();
        XVertex bsink = (XVertex) snk;
        XVertex bsource = (XVertex) src;
        while (bsink.parent != null) {
            L.add(bsink.parentEdge);
            bsink = (XVertex) bsink.parent;
            if (bsink.equals(bsource)) break;
        }

        if (!bsink.equals(bsource)) {
            return null;
        }

        Collections.reverse(L);
        System.out.println("PATH: " + L);

        return L;
    }

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
     * Edge out of u in Residual Graph (Gf) because of e ?
     *
     * @param e
     * @return
     */
    boolean inResidualGraph(Edge e) {
        XEdge xe = (XEdge) e;
        return (xe.flow < xe.capacity) || (xe.flow < 0);
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

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
//        if (args.length > 1) {
//            VERBOSE = Integer.parseInt(args[1]);
//        }

        int start = in.nextInt(); // source node
        int end = in.nextInt(); // sink node
        Graph g = Graph.readDirectedGraph(in);
        Vertex source = g.getVertex(start);
        Vertex sink = g.getVertex(end);

        Timer t = new Timer();
        Dinitz d = new Dinitz(g, source, sink);
        int maxFlow = d.maxFlow();
        t.end();
        System.out.println(t);
        System.out.println(maxFlow);
        System.out.println(d.minCutS());
        System.out.println(d.minCutT());

        d.verify();
    }

    boolean verify() {
        int outFlow = 0;
        XVertex xsource = (XVertex) source;
        for (Edge e : xsource.xadj) {
            XEdge xe = (XEdge) e;
            outFlow += xe.flow();
        }

        int inFlow = 0;
        XVertex xsink = (XVertex) sink;
        for (Edge e : xsink.xrevAdj) {
            XEdge xe = (XEdge) e;
            inFlow += xe.flow();
        }

        if (inFlow != outFlow) {
            System.out.println("Invalid: total flow from the source (" + outFlow + ") != total flow to the sink (" + inFlow + ")");
            return false;
        }

        for(Vertex u: g){
            XVertex xu = (XVertex) u;
            xu.seen = false;
        }

        Set<Vertex> minCutS = this.minCutS();
        Set<Vertex> minCutT = this.minCutT();

        if (minCutS.size() + minCutT.size() != g.size()) {
            System.out.println("Invalid size of Min-Cut: " + (minCutS.size() + minCutT.size()));
            return false;
        }

        for (Vertex u : g) {
            if (u != source && u != sink) {
                XVertex xu = (XVertex) u;
                int outVertexFlow = 0;
                for (Edge e : xu.xadj) {
                    XEdge xe = (XEdge) e;
                    if (xe.reverseEdge) continue;
                    outVertexFlow += xe.flow();
                }

                int inVertexFlow = 0;
                for (Edge e : xu.xrevAdj) {
                    XEdge xe = (XEdge) e;
                    if (xe.reverseEdge) continue;
                    inVertexFlow += xe.flow();
                }

                if (inVertexFlow != outVertexFlow) {
                    System.out.println("Invalid: Total incoming flow != Total outgoing flow at " + u);
                    return false;
                }
            }
        }

        return true;
    }

}
