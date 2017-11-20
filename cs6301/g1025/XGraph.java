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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



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

            XVertexIterator(XVertex u) {
                this.it = u.xadj.iterator();
                ready = false;
            }

            XVertexIterator(XVertex u, List<XEdge> edges) {
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
                while (cur.isDisabled() && it.hasNext()) {
                    cur = it.next();
                }
                ready = true;
                return !cur.isDisabled();
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
    }

    static class XEdge extends Edge {
        boolean disabled;
        int capacity;
        int flow;
        int name;
        boolean reverseEdge;

        XEdge(XVertex from, XVertex to, int weight, int name) {
            super(from, to, weight);
            disabled = false;
            capacity = weight;
            flow = 0;
            this.name = name;
            reverseEdge = false;
        }

        XEdge(XVertex from, XVertex to, int weight, int name, boolean reverse) {
            super(from, to, weight);
            disabled = false;
            capacity = weight;
            flow = 0;
            this.name = name;
            reverseEdge = reverse;
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

        boolean isReverse(){
            return reverseEdge;
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
        numEdges = 0;

        // Make copy of edges
        for (Vertex u : g) {
            for (Edge e : u) {
                Vertex v = e.otherEnd(u);
                XVertex x1 = getVertex(u);
                XVertex x2 = getVertex(v);
                XEdge edge = new XEdge(x1, x2, capacity.get(e), numEdges);
                x1.xadj.add(edge);
                x2.xrevAdj.add(edge);
                edges[numEdges++] = edge;
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
        numEdges = 0;
        edges = new XEdge[g.m * 2];

        // Make copy of edges
        for (Vertex u : g) {
            for (Edge e : u) {
                Vertex v = e.otherEnd(u);
                XVertex x1 = getVertex(u);
                XVertex x2 = getVertex(v);
                XEdge edge = new XEdge(x1, x2, e.getWeight(), numEdges);
                x1.xadj.add(edge);
                x2.xrevAdj.add(edge);
                edges[numEdges++] = edge;
            }
        }
        initialGraph = g;
    }

    public void addNewEdge(Vertex from, Vertex to, int weight, boolean reverse) {
        XVertex x1 = getVertex(from);
        XVertex x2 = getVertex(to);
        XEdge edge = new XEdge(x1, x2, weight, numEdges, reverse);
        x1.xadj.add(edge);
        x2.xrevAdj.add(edge);
        edges[numEdges++] = edge;
    }

    XEdge getEdge(Vertex from, Vertex to) {
        XVertex x1 = getVertex(from);
        XVertex x2 = getVertex(to);
        for (Edge e : from) {
            XEdge xe = (XEdge) e;
            XVertex xv = (XVertex) e.otherEnd(x1);
            if (xv == x2) return xe;
        }
        return null;
    }

    int flow(Edge e) {
        XEdge xe = (XEdge) e;
        return xe.flow();
    }

    int capacity(Edge e) {
        XEdge xe = (XEdge) e;
        return xe.capacity();
    }

    void setCapacity(Edge e, int capacity) {
        XEdge xe = (XEdge) e;
        xe.capacity = capacity;
    }

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


    @Override
    public Vertex getVertex(int n) {
        return xv[n];
    }

    XVertex getVertex(Vertex u) {
        return Vertex.getVertex(xv, u);
    }

    void disable(int i) {
        XVertex u = (XVertex) getVertex(i);
        u.disable();
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