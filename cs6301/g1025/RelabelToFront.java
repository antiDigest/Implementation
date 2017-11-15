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
import static java.lang.Integer.min;
import static java.lang.Math.abs;


public class RelabelToFront {

    Graph g;
    Vertex source;
    Vertex sink;

    RelabelToFront(Graph g, Vertex source, Vertex sink, HashMap<Edge, Integer> capacity) {
        this.g = new XGraph(g, capacity);
        this.source = this.g.getVertex(source.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    RelabelToFront(Graph g, Vertex source, Vertex sink) {
        this.g = new XGraph(g);
        this.source = this.g.getVertex(source.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    void initialize() {
        XVertex xsource = (XVertex) source;
        xsource.height = g.size();
        for (Edge e : xsource) {
            XVertex xu = (XVertex) e.otherEnd(xsource);
            XEdge xe = (XEdge) e;
            xe.flow = xe.capacity;
            xsource.excess = xsource.excess - xe.capacity;
            xu.excess = xu.excess + xe.capacity;
            ((XGraph) this.g).addNewEdge(xu, xsource, xe.flow());
        }
    }

    void push(Vertex u, Vertex v, Edge e) {
        XEdge xe = (XEdge) e;
        XVertex xu = (XVertex) u;
        XVertex xv = (XVertex) v;

        int delta = min(xu.excess, xe.capacity);
        if (xe.fromVertex() == xu) {
            xe.flow = xe.flow + delta;
        } else {
            xe.flow = xe.flow - delta;
        }

        xu.excess -= delta;
        xv.excess += delta;

        // Adding an edge for back flow
        XEdge revEdge = ((XGraph) this.g).getEdge(xv, xu);
        if (revEdge != null) {
            revEdge.flow -= xe.flow();
        } else {
            ((XGraph) this.g).addNewEdge(xv, xu, xe.flow());
        }
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

        if(xe.fromVertex() == xu ? xe.flow < xe.capacity : xe.flow > 0){
            return true;
        } else {
            xe.disable();
            return false;
        }
    }

    void relabel(Vertex u) {
        XVertex xu = (XVertex) u;

        int minHeight = INFINITY;
        for (Edge e : xu) {
            XEdge xe = (XEdge) e;
            XVertex xv = (XVertex) e.otherEnd(xu);
            if (xv.height < xu.height) continue;
            if (xv.height < minHeight) {
                minHeight = xv.height;
            }
        }

        xu.height = 1 + minHeight;

    }

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
            relabel(xu);
        }
    }

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

    int maxFlow() {
        XVertex xsink = (XVertex) sink;
        return abs(xsink.excess);
    }

    Vertex next(Iterator<Vertex> it) {
        return it.hasNext() ? it.next() : null;
    }

    Set<Vertex> minCutS(){
        Set<Vertex> minCutS = new LinkedHashSet<>();
        BFS b = new BFS(((XGraph)this.g).initialGraph, this.source);
        b.bfs();
        for(Vertex u: b.g){
            BFS.BFSVertex bu = b.getVertex(u);
            if(bu.parent!=null && !((XEdge)bu.parentEdge).isDisabled())
                minCutS.add(u);
        }

        return minCutS;
    }

    Set<Vertex> minCutT(){
        Set<Vertex> minCutT = new LinkedHashSet<>();
        BFS b = new BFS(((XGraph)this.g).initialGraph, this.sink);
        b.bfs();
        for(Vertex u: b.g){
            BFS.BFSVertex bu = b.getVertex(u);
            if(bu.parent!=null && !((XEdge)bu.parentEdge).isDisabled())
                minCutT.add(u);
        }

        return minCutT;
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

        Timer t = new Timer();
        RelabelToFront rtf = new RelabelToFront(g, source, sink);
        rtf.relabelToFront();
        t.end();
        System.out.println(t);
        System.out.println(rtf.maxFlow());
        System.out.println(rtf.minCutS());
        System.out.println(rtf.minCutT());
    }
}
