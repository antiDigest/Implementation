/**
 * Class to represent a graph
 *  @author swaroop, saikumar, antriksh
 *
 */

package cs6301.g1025;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SCC extends GraphAlgorithm<SCC.SCCVertex> {
    int topNum;
    int cno;

    public SCC(Graph g) {
        super(g);
        node = new SCCVertex[g.size()];
        topNum = g.size();
        cno = 0;
        // Create array for storing vertex properties
        for (Graph.Vertex u : g) {
            node[u.getName()] = new SCCVertex(u);
        }
    }

    /**
     * Wrapper to store component numbers
     */
    static class SCCVertex implements Comparable<SCCVertex> {
        boolean seen;
        int componentno;
        int top;
        Graph.Vertex vertex;

        SCCVertex(Graph.Vertex u) {
            vertex = u;
            seen = false;
            componentno = 0;
        }

        int getCno(){
            return componentno;
        }

        @Override
        public int compareTo(SCCVertex o) {
            if(this.top > o.top)
                return 1;
            else if (this.top < o.top)
                return -1;
            else return 0;
        }

        @Override
        public String toString() {
            return "(" + vertex.getName() + ", " + this.componentno + ")";
        }
    }

    /**
     * Main program to find SCC
     * @param g: graph
     * @return Number of components
     */
    int stronglyConnectedComponents(Graph g) {
        List<Graph.Vertex> order = this.dfs();
        reverseGraph((XGraph) g);
        reinitialize();
        System.out.println(node[5]);
        this.dfsRev(order);
        System.out.println(node[5]);
        return cno;
    }

    /**
     * Assign Component Number to vertex
     * @param u: vertex
     * @param componentno: integer
     */
    void assignComponentno(Graph.Vertex u, int componentno) {
        SCCVertex sc = getVertex(u);
        sc.componentno = componentno;
    }

    /**
     * Initializing DFS
     * @return List of vertices visited (Finish order)
     */
    List<Graph.Vertex> dfs() {
        List<Graph.Vertex> l = new ArrayList<>();
        for (Graph.Vertex u : this.g) {
            if (!seen(u)) {
                dfsVisit(u);
            }
        }

        Arrays.sort(node);

        for(SCCVertex v:node){
            l.add(v.vertex);
        }

        return l;
    }

    /**
     * DFS Visit procedure
     * @param u: start vertex
     */
    void dfsVisit(Graph.Vertex u) {
        visit(u);
        for (Graph.Edge e : u.adj) {
            Graph.Vertex v = e.otherEnd(u);
            if (!seen(v)) {
                dfsVisit(v);
            }
        }
    }

    /**
     * Visit v
     * @param v: Vertex
     */
    void visit(Graph.Vertex v) {
        SCCVertex sc = getVertex(v);
        sc.seen = true;
        sc.top = topNum--;
        sc.componentno = cno;
    }

    /**
     * Reverse DFS
     * @param lst: List of Vertices
     * @return number of components
     */
    void dfsRev(List<Graph.Vertex> lst) {
        for (int i = lst.size() - 1; i >= 0; i--) {
            Graph.Vertex v = lst.get(i);
            if (!seen(v)) {
                cno++;
                dfsVisit(v);
            }
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner;

        if (args.length > 0) {
            File in = new File(args[0]);
            scanner = new Scanner(in);
        } else {
            System.out.println("Enter the graph: ");
            scanner = new Scanner(System.in);
        }

        Graph graph = Graph.readGraph(scanner, true);
        SCC sc = new SCC(graph);
        System.out.println(sc.stronglyConnectedComponents(graph));

    }

    /**
     * Re-Initialise graph, reset everything
     */
    void reinitialize() {
        cno = 0;
        for (Graph.Vertex u : g) {
            SCCVertex v = this.getVertex(u);
            v.seen = false;
        }
    }

    /** Method to reverse the edges of a graph.  Applicable to directed graphs only. */
    public void reverseGraph(XGraph g) {
        if (g.directed) {
            for (Graph.Vertex u : g) {
                List<XGraph.XEdge> tmp = g.getVertex(u).getAdj();
                g.getVertex(u).xadj = g.getVertex(u).getRevAdj();
                g.getVertex(u).xrevAdj = tmp;
            }
        }
    }

    /**
     * u seen or not
     * @param u
     * @return: boolean
     */
    boolean seen(Graph.Vertex u) {
        return getVertex(u).seen;
    }


}