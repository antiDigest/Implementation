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

import static cs6301.g1025.BFS.INFINITY;

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

    int maxFlow() {
        while (true) {
            BFS b = new BFS((XGraph) g, (XGraph.XVertex) source);
            b.bfs();
            List<Graph.Edge> pathEdges = getPath(source, sink, b);
            if (pathEdges == null || pathEdges.isEmpty())
                break;
            int cMin = minCapacity(pathEdges);
            System.out.println("FLOW: " + cMin);
            for (Graph.Edge e : pathEdges) {
                XEdge xe = (XEdge) e;
                XVertex xu = (XVertex) e.fromVertex();
                XVertex xv = (XVertex) e.toVertex();

                if (inResidualGraph(xe)) {
                    xe.flow = xe.flow + cMin;
                } else {
                    xe.flow = xe.flow - cMin;
                }

                // Adding an edge for back flow - if it doesn't already exist.
                XEdge revEdge = ((XGraph) this.g).getEdge(xv, xu);

                if (revEdge != null && revEdge.reverseEdge) {
                    revEdge.capacity += cMin;
                }

                if (revEdge != null) {
                    revEdge.flow -= cMin;
                } else {
                    ((XGraph) this.g).addNewEdge(xv, xu, xe.flow(), true);
                }

                if (!inResidualGraph(xe) || xe.flow == xe.capacity) {
                    xe.disable();
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

    int minCapacity(List<Graph.Edge> path) {
        int minCapacity = INFINITY;
        for (Edge e : path) {
            XEdge xe = (XEdge) e;
            if (minCapacity > xe.capacity()) {
                minCapacity = xe.capacity();
            }
        }
        return minCapacity;
    }

    List<Graph.Edge> getPath(Graph.Vertex src, Graph.Vertex snk, BFS b) {
        List<Graph.Edge> L = new LinkedList<>();
        BFS.BFSVertex bsink = b.getVertex(snk);
        BFS.BFSVertex bsource = b.getVertex(src);
        while (bsink.parent != null) {
            L.add(bsink.parentEdge);
            bsink = b.getVertex(bsink.parent);
            if (bsink == bsource) break;
        }

        if (bsink != bsource) {
            return null;
        }

        Collections.reverse(L);
        System.out.println("PATH: " + L);

        return L;
    }

    Set<Graph.Vertex> minCutS() {

        Graph.Vertex src = source;

        for (Vertex u : g) {
            XVertex xu = (XVertex) u;
            xu.seen = false;
        }

        Set<Graph.Vertex> minCut = new LinkedHashSet<>();
        Queue<Graph.Vertex> q = new LinkedList<>();
        minCut.add((XVertex) src);
        q.add(src);
        while (!q.isEmpty()) {
            XGraph.XVertex xu = (XGraph.XVertex) q.remove();
            for (Graph.Edge e : xu) {
                XEdge xe = (XEdge) e;
                XGraph.XVertex xv = (XGraph.XVertex) e.otherEnd(xu);
                if (!xv.seen && (inResidualGraph(xu, e) || !xe.reverseEdge)) {
                    xv.seen = true;
                    minCut.add(xv);
                    q.add(xv);
                }
            }
        }
        return minCut;
    }

    Set<Graph.Vertex> minCutT() {
        Graph.Vertex src = sink;

        for (Vertex u : g) {
            XVertex xu = (XVertex) u;
            xu.seen = false;
        }

        Set<Graph.Vertex> minCut = new LinkedHashSet<>();
        Queue<Graph.Vertex> q = new LinkedList<>();
        minCut.add((XVertex) src);
        q.add(src);
        while (!q.isEmpty()) {
            XGraph.XVertex xu = (XGraph.XVertex) q.remove();
            Iterator<Edge> revit = xu.reverseIterator();
            while (revit.hasNext()) {
                Edge e = revit.next();
                XEdge xe = (XEdge) e;
                XGraph.XVertex xv = (XGraph.XVertex) e.otherEnd(xu);
                if (!xv.seen && (inResidualGraph(xu, e) || !xe.reverseEdge)) {
                    xv.seen = true;
                    minCut.add(xv);
                    q.add(xv);
                }
            }
        }
        return minCut;
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
     * Edge out of u in Residual Graph (Gf) because of e ?
     *
     * @param u
     * @param e
     * @return
     */
    boolean inResidualGraph(Vertex u, Edge e) {
        XEdge xe = (XEdge) e;
        XVertex xu = (XVertex) u;

        return xe.fromVertex() == xu ? xe.flow < xe.capacity : xe.flow > 0;
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
