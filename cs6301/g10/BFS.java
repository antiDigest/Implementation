package cs6301.g10;

import cs6301.g10.utils.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class BFS {
    public class BFSVertex {
        Graph.Vertex element;
        boolean seen;
        BFSVertex prev;

        BFSVertex(Graph.Vertex u) {
            element = u;
            seen = false;
            prev = null;
        }

        public String toString() {
            if (this.prev != null)
                return "<element:" + (this.element.name + 1)
                        + ", seen:" + this.seen + ", prev:"
                        + (this.prev.element.name + 1) + ">";
            return "<element:" + (this.element.name + 1)
                    + ", seen:" + this.seen
                    + ", prev:null>";
        }

    }

    Queue<Graph.Vertex> q;
    Graph G;
    BFSVertex start;
    BFSVertex[] bfsvertex;

    // Parallel Array for sorting information about vertices.
    public BFS(Graph G) {
        q = new LinkedList<Graph.Vertex>();
        this.G = G;
        bfsvertex = new BFSVertex[G.size()];
        for (Graph.Vertex u : G) {
            bfsvertex[u.name] = new BFSVertex(u);
        }
    }

    BFSVertex getBFSVertex(Graph.Vertex u) {
        return bfsvertex[u.name];
    }

    boolean seen(Graph.Vertex u) {
        BFSVertex s = getBFSVertex(u);
//        System.out.println(s.element.name + " " + s.seen);
        return s.seen;
    }

    Graph.Vertex getOtherEnd(Graph.Edge e, Graph.Vertex S) {
        assert S == e.to || S == e.from;
        if (S == e.to)
            return e.from;
        return e.to;
    }

    public Graph.Vertex bfsVisit(Graph.Vertex u) {
        q.add(u);
        Graph.Vertex S = null;
        while (!q.isEmpty()) {
            S = q.remove();
            getBFSVertex(S).seen = true;
            for (Graph.Edge e : S) {
                Graph.Vertex v = getOtherEnd(e, S);
                if (!seen(v)) {
                    getBFSVertex(v).prev = getBFSVertex(S);
//                    System.out.println("Adding");
                    q.add(v);
                }
            }
        }
        return S;
    }
}