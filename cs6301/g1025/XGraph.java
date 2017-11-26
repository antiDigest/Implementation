/**
 * @author rbk
 * Ver 1.0: 2017/09/29
 * Example to extend Graph/Vertex/Edge classes to implement algorithms in which nodes and edges
 * need to be disabled during execution.  Design goal: be able to call other graph algorithms
 * without changing their codes to account for disabled elements.
 * <p>
 * Ver 1.1: 2017/10/09
 * Updated iterator with boolean field ready. Previously, if hasNext() is called multiple
 * times, then cursor keeps moving forward, even though the elements were not accessed
 * by next().  Also, if program calls next() multiple times, without calling hasNext()
 * in between, same element is returned.  Added UnsupportedOperationException to remove.
 **/

package cs6301.g1025;

import cs6301.g00.ArrayIterator;

import java.util.*;

public class XGraph extends Graph {
    public static final int INFINITY = Integer.MAX_VALUE;

    public static class XVertex extends Vertex {
        boolean disabled;
        List<XEdge> xadj;
        List<XEdge> xrevAdj;
        int height;
        int excess;
        boolean seen;
        int distance;
        Vertex parent;
        Edge parentEdge;

        XVertex(Vertex u) {
            super(u);
            disabled = false;
            xadj = new LinkedList<>();
            xrevAdj = new LinkedList<>();
            height = 0;
            excess = 0;
            distance = INFINITY;
            parent = null;
            parentEdge = null;
        }

        boolean isDisabled() {
            return disabled;
        }

        void disable() {
            disabled = true;
        }

        @Override
        public Iterator<Edge> iterator() {
            return new XVertexIterator(this);
        }

        public Iterator<Edge> reverseIterator() {
            return new XVertexIterator(this, this.xrevAdj);
        }

        class XVertexIterator implements Iterator<Edge> {
            XEdge cur;
            Iterator<XEdge> it;
            boolean ready;
            XVertex u;

            XVertexIterator(XVertex u) {
                this.u = u;
                this.it = u.xadj.iterator();
                ready = false;
            }

            XVertexIterator(XVertex u, List<XEdge> edges) {
                this.u = u;
                this.it = edges.iterator();
                ready = false;
            }

            public boolean hasNext() {
                if (ready) {
                    return true;
                }
                if (!it.hasNext()) {
                    return false;
                }
                cur = it.next();
                while (!inResidualGraph(cur) && it.hasNext()) {
                    cur = it.next();
                }
                ready = true;
                return (inResidualGraph(cur) && !cur.isDisabled());
            }

            public Edge next() {
                if (!ready) {
                    if (!hasNext()) {
                        throw new java.util.NoSuchElementException();
                    }
                }
                ready = false;
                return cur;
            }

            public void remove() {
                throw new java.lang.UnsupportedOperationException();
            }
        }

        /**
         * Edge out of u in Residual Graph (Gf) because of e ?
         * @param u From vertex
         * @param e Edge (u, ?)
         * @return true if in residual graph, else false
         */
        boolean inResidualGraph(Vertex u, Edge e) {
            XEdge xe = (XEdge) e;
            return e.fromVertex().equals(u) ? xe.flow() < xe.capacity() : xe.flow() > 0;
        }

        /**
         * Edge out of "this" vertex in Residual Graph (Gf) because of e ?
         * @param e Edge (u, ?)
         * @return true if in residual graph, else false
         */
        boolean inResidualGraph(Edge e) {
            XEdge xe = (XEdge) e;
            return e.fromVertex().equals(this) ? xe.flow() < xe.capacity() : xe.flow() > 0;
        }
    }

    static class XEdge extends Edge {
        boolean disabled;
        int capacity;
        int flow;

        XEdge(XVertex from, XVertex to, int weight, int capacity) {
            super(from, to, weight);
            disabled = false;
            this.weight = weight;
            this.capacity = capacity;
            flow = 0;
        }

        boolean isDisabled() {
            XVertex xfrom = (XVertex) from;
            XVertex xto = (XVertex) to;
            return disabled || xfrom.isDisabled() || xto.isDisabled();
        }

        void disable() {
            this.disabled = true;
        }

        int capacity() {
            return capacity;
        }

        int flow() {
            return flow;
        }
    }

    XVertex[] xv; // vertices of graph
    XEdge[] edges; //Edges of Graph
    int numEdges;
    Graph initialGraph;

    public XGraph(Graph g, HashMap<Edge, Integer> capacity) {
        super(g);
        xv = new XVertex[g.size()];  // Extra space is allocated in array for nodes to be added later
        for (Vertex u : g) {
            xv[u.getName()] = new XVertex(u);
        }
        numEdges = g.m;
        edges = new XEdge[g.m + 1];

        // Make copy of edges
        for (Vertex u : g) {
            for (Edge e : u.adj) {
                Vertex v = e.otherEnd(u);
                XVertex x1 = getVertex(u);
                XVertex x2 = getVertex(v);
                XEdge edge = new XEdge(x1, x2, e.getWeight(), capacity.get(e));
                x1.xadj.add(edge);
                x2.xrevAdj.add(edge);
                edge.name = e.getName();
                edges[e.getName()] = edge;
            }
        }
        initialGraph = g;
    }

    public XGraph(Graph g) {
        super(g);
        xv = new XVertex[g.size()];  // Extra space is allocated in array for nodes to be added later
        for (Vertex u : g) {
            xv[u.getName()] = new XVertex(u);
        }
        numEdges = g.m;
        edges = new XEdge[g.m + 1];

        // Make copy of edges
        for (Vertex u : g) {
            for (Edge e : u) {
                Vertex v = e.otherEnd(u);
                XVertex x1 = getVertex(u);
                XVertex x2 = getVertex(v);
                XEdge edge = new XEdge(x1, x2, e.getWeight(), e.getWeight());
                x1.xadj.add(edge);
                x2.xrevAdj.add(edge);
                edge.name = e.getName();
                edges[e.getName()] = edge;
            }
        }
        initialGraph = g;
    }

    /**
     * ITERATOR
     */

    @Override
    public Iterator<Vertex> iterator() {
        return new XGraphIterator(this);
    }

    class XGraphIterator implements Iterator<Vertex> {
        Iterator<XVertex> it;
        XVertex xcur;

        XGraphIterator(XGraph xg) {
            this.it = new ArrayIterator<XVertex>(xg.xv, 0, xg.size() - 1);  // Iterate over existing elements only
        }

        public boolean hasNext() {
            if (!it.hasNext()) {
                return false;
            }
            xcur = it.next();
            while (xcur.isDisabled() && it.hasNext()) {
                xcur = it.next();
            }
            return !xcur.isDisabled();
        }

        public Vertex next() {
            return xcur;
        }

        public void remove() {
        }

    }

    /**
     * HELPER METHODS
     */

    boolean seen(Vertex v){
        XVertex xv = (XVertex) v;
        return xv.seen;
    }

    void setSeen(Vertex v){
        XVertex xv = (XVertex) v;
        xv.seen = true;
    }

    void setDistance(Vertex v, int distance){
        XVertex xv = (XVertex) v;
        xv.distance = distance;
    }

    int getDistance(Vertex v){
        XVertex xv = (XVertex) v;
        return xv.distance;
    }

    void setParent(Vertex v, Vertex parent){
        XVertex xv = (XVertex) v;
        xv.parent = parent;
    }

    void resetDistance(Vertex v){
        XVertex xv = (XVertex) v;
        xv.distance = INFINITY;
    }

    void resetParent(Vertex v){
        XVertex xv = (XVertex) v;
        xv.parent = null;
        xv.parentEdge = null;
    }

    void resetSeen(Vertex v){
        XVertex xv = (XVertex) v;
        xv.seen = false;
    }

    int height(Vertex v){
        XVertex xv = (XVertex) v;
        return xv.height;
    }

    void setHeight(Vertex v, int value){
        XVertex xv = (XVertex) v;
        xv.height = value;
    }

    int excess(Vertex v){
        XVertex xv = (XVertex) v;
        return xv.excess;
    }

    void setExcess(Vertex v, int value){
        XVertex xv = (XVertex) v;
        xv.excess = value;
    }

    int flow(Edge e) {
        XEdge xe = getEdge(e.getName());
        return xe.flow();
    }

    void setFlow(Edge e, int value) {
        XEdge xe = getEdge(e.getName());
        xe.flow = value;
    }

    int capacity(Edge e) {
        XEdge xe = getEdge(e.getName());
        return xe.capacity();
    }

    void setCapacity(Edge e, int value) {
        XEdge xe = getEdge(e.getName());
        xe.capacity = value;
    }

    List<XEdge> getAdj(Vertex v){
        XVertex xv = (XVertex) v;
        return xv.xadj;
    }

    List<XEdge> getRevAdj(Vertex v){
        XVertex xv = (XVertex) v;
        return xv.xrevAdj;
    }

    @Override
    public Vertex getVertex(int n) {
        return xv[n];
    }

    XVertex getVertex(Vertex u) {
        return Vertex.getVertex(xv, u);
    }

    XEdge getEdge(int name) {
        return edges[name];
    }

//    void printGraph(BFS b) {
//        for (Vertex u : this) {
//            System.out.print("  " + u + "  :   " + b.distance(u) + "  : ");
//            for (Edge e : u) {
//                System.out.print(e);
//            }
//            System.out.println();
//        }
//
//    }

}

/*
Sample output:

Node : Dist : Edges
  1  :   0  : (1,2)(1,3)
  2  :   1  : (2,1)(2,4)(2,5)
  3  :   1  : (3,1)(3,6)(3,7)
  4  :   2  : (4,2)(4,8)
  5  :   2  : (5,2)
  6  :   2  : (6,3)
  7  :   2  : (7,3)(7,9)
  8  :   3  : (8,4)
  9  :   3  : (9,7)
Source: 1 Farthest: 8 Distance: 3

Disabling vertices 8 and 9
  1  :   0  : (1,2)(1,3)
  2  :   1  : (2,1)(2,4)(2,5)
  3  :   1  : (3,1)(3,6)(3,7)
  4  :   2  : (4,2)
  5  :   2  : (5,2)
  6  :   2  : (6,3)
  7  :   2  : (7,3)
Source: 1 Farthest: 4 Distance: 2

*/