package cs6301.g1025;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import cs6301.g00.Timer;
import cs6301.g1025.Graph.*;
import cs6301.g1025.XGraph.*;

public class SuccessiveShortestPaths extends RelabelToFront {

    Graph g;
    Graph.Vertex source;
    Graph.Vertex sink;
    HashMap<Graph.Edge, Integer> capacity;
    HashMap<Graph.Edge, Integer> cost;
    int minCost;
    public static final int INFINITY = Integer.MAX_VALUE;

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

    public SuccessiveShortestPaths(Graph g, Graph.Vertex s, Graph.Vertex t,
                       HashMap<Graph.Edge, Integer> capacity,
                       HashMap<Graph.Edge, Integer> cost) {
        super(g, s, t, capacity);
        this.g = new XGraph(g, capacity, cost);
        this.source = ((XGraph)this.g).getVertex(s.getName());
        this.sink = ((XGraph)this.g).getVertex(t.getName());
        this.capacity = capacity;
        this.cost = cost;
        minCost = 0;
    }

    private SuccessiveShortestPaths(Graph g, Graph.Vertex s, Graph.Vertex t,
                        HashMap<Graph.Edge, Integer> cost) {
        super(g, s, t);
        this.g = new XGraph(g, cost);
        this.source = ((XGraph)this.g).getVertex(s.getName());
        this.sink = ((XGraph)this.g).getVertex(t.getName());
        this.cost = cost;
        this.capacity = null;
        minCost = 0;
    }

    private SuccessiveShortestPaths(Graph g, Graph.Vertex s, Graph.Vertex t) {
        super(g, s, t);
        this.g = new XGraph(g);
        this.source = ((XGraph)this.g).getVertex(s.getName() + 1);
        this.sink = ((XGraph)this.g).getVertex(t.getName() + 1);
        this.cost = null;
        this.capacity = null;
        minCost = 0;
    }

    void minCostFlow(){
        super.relabelToFront();
        this.g = super.g;

        for(Vertex u: g){
            XVertex xu = (XVertex) u;
            for(Edge e: u){
                XEdge xe = (XEdge) e;
                Vertex v = e.otherEnd(u);
                XVertex xv = (XVertex) v;
                xe.capacity = xe.capacity() - xe.flow();
                ((XGraph) g).addNewEdge(xv, xu, xe.flow(), -xe.cost());
            }
        }

        for(Edge e: ((XGraph) g).edges){
            XEdge xe = (XEdge) e;
            if(xe == null) continue;
            if(xe.capacity < 0){
                xe.disable();
            } else {
                xe.setWeight(xe.cost);
            }
        }

        BellmanFord.bellmanFord(this.g, this.source);

        Set<List<PathEdge>> paths = new LinkedHashSet<>();
        List<PathEdge> path = new ArrayList<>();
        getAllPaths(paths, path, source);

        for (List<PathEdge> pathEdges : paths) {
            int cMin = minCapacity(pathEdges);
            for (PathEdge pe : pathEdges) {
                XEdge xe = (XEdge) pe.e;

                if (!isReverse(xe, pe.from)) {
                    xe.flow = xe.flow + cMin;
                } else {
                    xe.flow = xe.flow - cMin;
                }

                minCost += cMin * xe.cost();

                if(xe.flow() == xe.capacity()){
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
        System.out.println("FLOW: " + flow);

        System.out.println("Min Cost: " + minCost);
    }


    List<PathEdge> getAllPaths(Set<List<PathEdge>> paths, List<PathEdge> path, Vertex src) {
        if (src.equals(sink)) {
            List<PathEdge> pathEdges = new ArrayList<>();
            pathEdges.addAll(path);
            paths.add(pathEdges);
//            return new ArrayList<>();
        } else {
            for (Edge e : src) {
                Vertex v = e.otherEnd(src);
                XVertex xv = (XVertex) v;
                if (RelabelToFront.inResidualGraph(src, e) && !xv.seen
                        && xv.distance == ((XVertex) src).distance + e.weight) {
                    path.add(new PathEdge(e, src, v));
                    xv.seen = true;
                    path = getAllPaths(paths, path, v);
                    if (!path.isEmpty()) {
                        path.remove(path.size() - 1);
                        xv.seen = false;
                    }
                }
            }

            for (Edge e : ((XVertex) src).xrevAdj) {
                Vertex v = e.otherEnd(src);
                XVertex xv = (XVertex) v;
                if (RelabelToFront.inResidualGraph(src, e) && !xv.seen
                        && xv.distance == ((XVertex) src).distance + e.weight) {
                    path.add(new PathEdge(e, src, v));
                    xv.seen = true;
                    path = getAllPaths(paths, path, v);
                    if (!path.isEmpty()) {
                        path.remove(path.size() - 1); // Kya karu iska ?
                        xv.seen = false;
                    }
                }
            }
        }
        return path;
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

        int start = in.nextInt(); // root node of the MST
        int end = in.nextInt();
        Graph g = Graph.readDirectedGraph(in);
        Vertex source = g.getVertex(start);
        Vertex sink = g.getVertex(end);

        cs6301.g00.Timer t = new Timer();
        SuccessiveShortestPaths ssp = new SuccessiveShortestPaths(g, source, sink);
        ssp.minCostFlow();
        t.end();
        System.out.println(t);
    }


}
