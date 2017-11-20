package cs6301.g1025;

import cs6301.g00.Timer;
import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XEdge;
import cs6301.g1025.XGraph.XVertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Math.min;

public class CostScaling extends RelabelToFront {

    Graph g;
    Graph.Vertex source;
    Graph.Vertex sink;
    HashMap<Graph.Edge, Integer> capacity;
    HashMap<Graph.Edge, Integer> cost;
    int epsilon;
    public static final int INFINITY = Integer.MAX_VALUE;

    public CostScaling(Graph g, Graph.Vertex s, Graph.Vertex t,
                HashMap<Graph.Edge, Integer> capacity,
                HashMap<Graph.Edge, Integer> cost) {
        super(g, s, t, capacity);
        this.g = new XGraph(g, capacity, cost);
        this.source = ((XGraph)this.g).getVertex(s.getName());
        this.sink = ((XGraph)this.g).getVertex(t.getName());
        this.capacity = capacity;
        this.cost = cost;
    }

    private CostScaling(Graph g, Graph.Vertex s, Graph.Vertex t,
                HashMap<Graph.Edge, Integer> cost) {
        super(g, s, t);
        this.g = new XGraph(g, cost);
        this.source = ((XGraph)this.g).getVertex(s.getName());
        this.sink = ((XGraph)this.g).getVertex(t.getName());
        this.cost = cost;
        this.capacity = null;
    }

    private CostScaling(Graph g, Graph.Vertex s, Graph.Vertex t) {
        super(g, s, t);
        this.g = new XGraph(g);
        this.source = ((XGraph)this.g).getVertex(s.getName() + 1);
        this.sink = ((XGraph)this.g).getVertex(t.getName() + 1);
        this.cost = null;
        this.capacity = null;
    }

    void minCostCirculation() {
        super.relabelToFront();

        epsilon = maxCost();

        while (epsilon > 0) {
            System.out.println("EPSILON: " + epsilon);
            refine();
        }

        XVertex xsink = (XVertex) sink;
        int minCost = 0;
        for(Edge e: xsink.xrevAdj){
            XEdge xe = (XEdge) e;
            minCost += RC(xe);
        }

        System.out.println("Sink Price: " + minCost);
    }

    private void refine() {
        epsilon = epsilon / 2;
        for (Vertex u : g) {
            XVertex xu = (XVertex) u;
            for (Edge e : u) {
                XEdge xe = (XEdge) e;
                if (inResidualGraph(xu, xe) && RC(u, xe) < 0) {
                    xe.flow = xe.capacity;
                    ((XVertex) xe.toVertex()).excess = xe.flow;
                }
            }
        }
        while (true) {
            boolean flag = true;
            for (Vertex u : g) {
                XVertex xu = (XVertex) u;
                if (xu.excess > 0) {
                    discharge(xu);
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
    }

    void discharge(Vertex u) {
        XVertex xu = (XVertex) u;
        while (xu.excess > 0) {
            for (Edge e : xu) {
                XEdge xe = (XEdge) e;
                if (inResidualGraph(xu, xe) && RC(xe) > 0) {
                    push(xu, e.otherEnd(u), xe);
                }
            }
        }
        if (xu.excess > 0) relabel(u);
    }

    /**
     * Relabel
     * Precondition: u.excess > 0, cost(u, v) >= 0, (u, v) ∈ Gf
     * @param u Vertex
     */
    void relabel(Vertex u) {
        XVertex xu = (XVertex) u;

        int maxP = -INFINITY;
        for (Edge e : xu) {
            XEdge xe = (XEdge) e;
            XVertex xv = (XVertex) e.otherEnd(xu);
            if (xe.cost >= 0 && (xv.price - xe.cost - epsilon) > maxP) {
                maxP = (xv.price - xe.cost - epsilon);
            }
        }

        for (Edge e : xu.xrevAdj) {
            XEdge xe = (XEdge) e;
            XVertex xv = (XVertex) e.otherEnd(xu);
            if (xe.cost >= 0 && (xv.price - xe.cost - epsilon) > maxP) {
                maxP = (xv.price - xe.cost - epsilon);
            }
        }

        xu.price = maxP;

    }

    /**
     * Residual Cost
     * @param e Edge (u, v)
     * @return residual cost (int)
     */
    private int RC(Edge e) {
        XEdge xe = (XEdge) e;
        XVertex xu = (XVertex) xe.fromVertex();
        XVertex xv = (XVertex) xe.toVertex();

        return xe.cost + xu.price - xv.price;
    }

    /**
     * Residual Cost
     * @param u From Vertex (u)
     * @param e Edge (u, v)
     * @return residual cost (int)
     */
    private int RC(Vertex u, Edge e) {
        XEdge xe = (XEdge) e;
        XVertex xu = (XVertex) u;
        XVertex xv = (XVertex) xe.otherEnd(u);

        return xe.cost + xu.price - xv.price;
    }

    /**
     * Calculate Maximum cost of all the edges in the graph;
     * @return
     */
    private int maxCost() {
        int maxCost = -INFINITY;
        for (XEdge e : ((XGraph) g).edges) {
            if(e == null) continue;
            if (maxCost < e.cost) {
                maxCost = e.cost;
            }
        }
        return maxCost;
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
        CostScaling cs = new CostScaling(g, source, sink);
        cs.minCostCirculation();
        t.end();
        System.out.println(t);
    }

}
