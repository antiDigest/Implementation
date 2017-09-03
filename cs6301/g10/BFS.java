/**
 * Sort class which will contain all sort functions.
 *
 * @author Antriksh, Gunjan
 * Ver 1.0: 2017/08/28
 */

package cs6301.g10;

import cs6301.g10.utils.Graph;

import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    /**
     * BFSVertex
     * Wrapper for Graph.Vertex class to include extra information.
     */
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

    /**
     * Init for BFS
     * Creates parallel array for storing extra information about vertices.
     *
     * @param G: Graph input
     */
    public BFS(Graph G) {
        q = new LinkedList<Graph.Vertex>();
        this.G = G;
        bfsvertex = new BFSVertex[G.size()];
        for (Graph.Vertex u : G) {
            bfsvertex[u.name] = new BFSVertex(u);
        }
    }

    /** getBFSVertex
     * returns parallel array BFSVertex from Graph.Vertex
     *
     * @param u: input Graph.Vertex
     * @return BFSVertex class instance
     */
    BFSVertex getBFSVertex(Graph.Vertex u) {
        return bfsvertex[u.name];
    }

    /** seen
     * returns if the Graph.Vertex has already been visited by the BFS procedure.
     * Uses information stored in parallel array
     * @param u: input Graph.Vertex
     * @return true or false -- boolean value
     */
    boolean seen(Graph.Vertex u) {
        BFSVertex s = getBFSVertex(u);
        return s.seen;
    }

    /** getOtherEnd
     * get other end of an edge e
     *
     * @param e: edge
     * @param S: one end of the edge
     * @return Graph.Vertex of the other end
     */
    Graph.Vertex getOtherEnd(Graph.Edge e, Graph.Vertex S) {
        assert S == e.to || S == e.from;
        if (S == e.to)
            return e.from;
        return e.to;
    }

    /** BFS Procedure
     * Parses the Graph using Breadth First Search
     *
     * @param u: start Vertex
     * @return Vertex ending at
     */
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
                    q.add(v);
                }
            }
        }
        return S;
    }
}