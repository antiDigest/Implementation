/**
 * Class to represent a graph
 *
<<<<<<< HEAD
 * @author rbk
 * Ver 1.1: 2017/08/28.  Updated some methods to public.  Added getName() to Vertex
=======
 *  @author rbk
 *  Ver 1.1: 2017/08/28.  Updated some methods to public.  Added getName() to Vertex
 *  Ver 1.2: 2017/09/08.  Added getVertex() method for GraphAlgorithm.java
 *
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
 */

package cs6301.g00;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Graph implements Iterable<Graph.Vertex> {
    Vertex[] v; // vertices of graph
<<<<<<< HEAD
    int n; // number of verices in the graph
    boolean directed; // true if graph is directed, false otherwise
=======
    int n; // number of vertices in the graph
    boolean directed;  // true if graph is directed, false otherwise

>>>>>>> 00b8023c27308018abd7772bd8e4893683704900

    /**
     * Nested class to represent a vertex of a graph
     */

    public static class Vertex implements Iterable<Edge> {
<<<<<<< HEAD
        int name; // name of the vertex
=======
        public int name; // name of the vertex
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
        List<Edge> adj, revAdj; // adjacency list; use LinkedList or ArrayList

        /**
         * Constructor for the vertex
         *
<<<<<<< HEAD
         * @param n : int - name of the vertex
         */
        Vertex(int n) {
            name = n;
            adj = new LinkedList<Edge>();
            revAdj = new LinkedList<Edge>(); /* only for directed graphs */
=======
         * @param n
         *            : int - name of the vertex
         */
        public Vertex(int n) {
            name = n;
            adj = new LinkedList<Edge>();
            revAdj = new LinkedList<Edge>();   /* only for directed graphs */
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
        }

        /**
         * Method to get name of a vertex.
<<<<<<< HEAD
=======
         *
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
         */
        public int getName() {
            return name;
        }

        public Iterator<Edge> iterator() {
            return adj.iterator();
        }

<<<<<<< HEAD
        // Helper function for parallel arrays used to store vertex attributes
        public static <T> T getVertex(T[] node, Vertex u) {
            return node[u.name];
        }

        /**
         * Method to get vertex number. +1 is needed because [0] is vertex 1.
=======
        /**
         * Method to get vertex number.  +1 is needed because [0] is vertex 1.
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
         */
        public String toString() {
            return Integer.toString(name + 1);
        }
<<<<<<< HEAD

        public List<Edge> getAdj() {
            return adj;
        }

        public List<Edge> getRevAdj() {
            return revAdj;
        }
=======
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
    }

    /**
     * Nested class that represents an edge of a Graph
     */

    public static class Edge {
<<<<<<< HEAD
        Vertex from; // head vertex
        Vertex to; // tail vertex
=======
        public Vertex from; // head vertex
        public Vertex to; // tail vertex
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
        int weight;// weight of edge

        /**
         * Constructor for Edge
         *
<<<<<<< HEAD
         * @param u : Vertex - Vertex from which edge starts
         * @param v : Vertex - Vertex on which edge lands
         * @param w : int - Weight of edge
         */
        Edge(Vertex u, Vertex v, int w) {
=======
         * @param u
         *            : Vertex - Vertex from which edge starts
         * @param v
         *            : Vertex - Vertex on which edge lands
         * @param w
         *            : int - Weight of edge
         */
        public Edge(Vertex u, Vertex v, int w) {
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
            from = u;
            to = v;
            weight = w;
        }

        /**
         * Method to find the other end end of an edge, given a vertex reference
         * This method is used for undirected graphs
         *
<<<<<<< HEAD
         * @param u : Vertex
         * @return : Vertex - other end of edge
         */
        public Vertex otherEnd(Vertex u) {
            assert from == u || to == u;
            // if the vertex u is the head of the arc, then return the tail else
            // return the head
=======
         * @param u
         *            : Vertex
         * @return
         *             : Vertex - other end of edge
         */
        public Vertex otherEnd(Vertex u) {
            assert from == u || to == u;
            // if the vertex u is the head of the arc, then return the tail else return the head
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
            if (from == u) {
                return to;
            } else {
                return from;
            }
        }

        /**
         * Return the string "(x,y)", where edge goes from x to y
         */
        public String toString() {
            return "(" + from + "," + to + ")";
        }

        public String stringWithSpaces() {
            return from + " " + to + " " + weight;
        }
    }

<<<<<<< HEAD
    /**
     * Constructor for Graph
     *
     * @param n : int - number of vertices
=======

    /**
     * Constructor for Graph
     *
     * @param n
     *            : int - number of vertices
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
     */
    public Graph(int n) {
        this.n = n;
        this.v = new Vertex[n];
<<<<<<< HEAD
        this.directed = false; // default is undirected graph
=======
        this.directed = false;  // default is undirected graph
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
        // create an array of Vertex objects
        for (int i = 0; i < n; i++)
            v[i] = new Vertex(i);
    }

    /**
     * Find vertex no. n
<<<<<<< HEAD
     *
     * @param n : int
=======
     * @param n
     *           : int
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
     */
    public Vertex getVertex(int n) {
        return v[n - 1];
    }

    /**
     * Method to add an edge to the graph
     *
<<<<<<< HEAD
     * @param from   : int - one end of edge
     * @param to     : int - other end of edge
     * @param weight : int - the weight of the edge
=======
     * @param from
     *            : int - one end of edge
     * @param to
     *            : int - other end of edge
     * @param weight
     *            : int - the weight of the edge
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
     */
    public void addEdge(Vertex from, Vertex to, int weight) {
        Edge e = new Edge(from, to, weight);
        if (this.directed) {
            from.adj.add(e);
            to.revAdj.add(e);
        } else {
            from.adj.add(e);
            to.adj.add(e);
        }
    }

    public int size() {
        return n;
    }

    /**
     * Method to create iterator for vertices of graph
     */
    public Iterator<Vertex> iterator() {
        return new ArrayIterator<Vertex>(v);
    }

    // read a directed graph using the Scanner interface
    public static Graph readDirectedGraph(Scanner in) {
        return readGraph(in, true);
    }

    // read an undirected graph using the Scanner interface
    public static Graph readGraph(Scanner in) {
        return readGraph(in, false);
    }

    public static Graph readGraph(Scanner in, boolean directed) {
        // read the graph related parameters
        int n = in.nextInt(); // number of vertices in the graph
        int m = in.nextInt(); // number of edges in the graph

        // create a graph instance
        Graph g = new Graph(n);
        g.directed = directed;
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int w = in.nextInt();
            g.addEdge(g.getVertex(u), g.getVertex(v), w);
        }
        return g;
    }
<<<<<<< HEAD

=======
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
}