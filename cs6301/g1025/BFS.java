/**
 * Breadth-first search
 * @author Antriksh, Gunjan, Saikumar, Swaroop
 */

package cs6301.g1025;


import cs6301.g00.Graph;

import java.util.LinkedList;
import java.util.Queue;

public class BFS extends GraphAlgorithm<BFS.BFSVertex> {
    public static final int INFINITY = Integer.MAX_VALUE;

    int n = 0; // Number of extra edges added

    // Class to store information about a vertex in this algorithm
    static class BFSVertex {
        boolean seen;
        Graph.Vertex parent;
        int distance; // distance of vertex from source

        BFSVertex(Graph.Vertex u) {
            seen = false;
            parent = null;
            distance = INFINITY;
        }
    }

    Graph.Vertex src;

    public BFS(Graph g, Graph.Vertex src) {
        super(g);
        this.src = src;
        node = new BFSVertex[g.size()];
        // Create array for storing vertex properties
        for (Graph.Vertex u : g) {
            node[u.getName()] = new BFSVertex((Graph.Vertex) u);
        }
        // Set source to be at distance 0
        getVertex(src).distance = 0;
    }

    // reinitialize allows running BFS many times, with different sources
    void reinitialize(Graph.Vertex newSource) {
        src = newSource;
        for (Graph.Vertex u : g) {
            BFSVertex bu = getVertex((Graph.Vertex) u);
            bu.seen = false;
            bu.parent = null;
            bu.distance = INFINITY;
        }
        getVertex(src).distance = 0;
    }

    void bfs() {
        Queue<Graph.Vertex> q = new LinkedList<>();
        q.add(src);
        while (!q.isEmpty()) {
            Graph.Vertex u = q.remove();
            for (Graph.Edge e : u) {
                Graph.Vertex v = e.otherEnd(u);
                int weight = e.getWeight();
                if(weight > 1) {
                    Graph.Vertex prev = u;
                    while (weight != 1) {
                        n++;
                        Graph.Vertex newVertex = new Graph.Vertex(g.size() + n);
                        g.addEdge(prev, newVertex, 1, g.size() + n);
                        prev = newVertex;
                        weight--;
                    }
                    g.addEdge(prev, v, 1, g.size() + n);
                }
            }
            for (Graph.Edge e: u) {
                Graph.Vertex v = e.otherEnd(u);
                if (!seen(v) && e.getWeight()==1) {
                    visit(u, v);
                    q.add(v);
                }
            }
        }
    }

    boolean seen(Graph.Vertex u) {
        return getVertex(u).seen;
    }

    Graph.Vertex getParent(Graph.Vertex u) {
        return getVertex(u).parent;
    }

    int distance(Graph.Vertex u) {
        return getVertex(u).distance;
    }

    // Visit a node v from u
    void visit(Graph.Vertex u, Graph.Vertex v) {
        BFSVertex bv = getVertex(v);
        bv.seen = true;
        bv.parent = u;
        bv.distance = distance(u) + 1;
    }
}