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

import java.util.*;

import static cs6301.g1025.LP3.INFINITY;


public class XGraph extends Graph {
    int graphSize;

    public static class XVertex extends Vertex {
        boolean seen;
        boolean disabled;
        List<XEdge> xadj;
        List<XEdge> xrevAdj;
        int cno;
        List<Vertex> collab;
        int size;
        XVertex parentVertex;

        XVertex(Vertex u) {
            super(u);
            disabled = false;
            xadj = new LinkedList<>();
            xrevAdj = new LinkedList<>();
            size = 1;
            seen = false;
        }

        XVertex(int n, List<Vertex> component) {
            super(n);
            name = n;
            disabled = false;
            xadj = new LinkedList<>();
            xrevAdj = new LinkedList<>();
            collab = new ArrayList<>();
            size = component.size();
            collab.addAll(component);
            seen = false;
        }

        boolean isDisabled() {
            return disabled;
        }

        void disable() {
            disabled = true;
        }

        void setVertex(XVertex v) {
            parentVertex = v;
        }

        XVertex getParent(){
            return parentVertex;
        }

        void enable() {
            disabled = false;
        }

        List<XEdge> getRevAdj() {
            return this.xrevAdj;
        }

        List<XEdge> getAdj() {
            return this.xadj;
        }

        List<XEdge> getXRevAdj() {
            return this.xrevAdj;
        }

        List<XEdge> getXAdj() {
            return this.xadj;
        }

        int getCno() {
            return cno;
        }

        int size() {
            return size;
        }

        boolean seen(){
            return seen;
        }

        @Override
        public String toString() {
            return this.getName() + "";
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

            XVertexIterator(XVertex u, List<XEdge> list) {
                this.it = list.iterator();
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

            public void disable() {
                cur.disable();
            }
        }
    }

    static class XEdge extends Edge {
        boolean disabled;
        XEdge minEdge;
        XVertex minEdgeTo;
        XVertex minEdgeFrom;
        int modifyWeight;

        public int getModifyWeight() {
            return modifyWeight;
        }

        public void setModifyWeight(int modifyWeight) {
            this.modifyWeight = modifyWeight;
        }

        XEdge(XVertex from, XVertex to, int weight) {
            super(from, to, weight);
            disabled = false;
            minEdge = null;
            modifyWeight = weight;
        }

        XEdge(XVertex from, XVertex to, int weight, int modifyWeight) {
            super(from, to, weight);
            disabled = false;
            minEdge = null;
            this.modifyWeight = modifyWeight;
        }

        boolean isDisabled() {
            XVertex xfrom = (XVertex) from;
            XVertex xto = (XVertex) to;
            return disabled || xfrom.isDisabled() || xto.isDisabled();
        }

        void disable() {
            disabled = true;
        }

        void enable() {
            disabled = false;
        }

        void setMinEdgeTo(XVertex v){
            minEdgeTo = v;
        }

        void setMinEdgeFrom(XVertex v){
            minEdgeFrom = v;
        }

        @Override
        public String toString() {
            return "(" + from + ", " + to + ", " + weight + ")";
        }
    }

    XVertex[] xv; // vertices of graph
    Graph original;

    public XGraph(Graph g) {
        super(g);
        this.original = g;
        graphSize = g.size();
        xv = new XVertex[2 * g.size()];  // Extra space is allocated in array for nodes to be added later
        for (Vertex u : g) {
            xv[u.getName()] = new XVertex(u);
        }

        // Make copy of edges
        for (Vertex u : g) {
            for (Edge e : u) {
                Vertex v = e.otherEnd(u);
                XVertex x1 = getVertex(u);
                XVertex x2 = getVertex(v);
                XEdge edge = new XEdge(x1, x2, e.getWeight());
                x1.xadj.add(edge);
                x2.xrevAdj.add(edge);
            }
        }
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new XGraphIterator(this);
    }

    class XGraphIterator implements Iterator<Vertex> {
        Iterator<XVertex> it;
        XVertex xcur;

        XGraphIterator(XGraph xg) {
            this.it = new ArrayIterator<XVertex>(xg.xv, 0, xg.graphSize - 1);  // Iterate over existing elements only
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

        public void disable() {
            xcur.disable();
        }

    }

    public void addVertex(List<Vertex> cycle) {
        incrementSize();
        XGraph.XVertex vertex = new XGraph.XVertex(size() - 1, cycle);
        this.xv[size() - 1] = vertex;

        disableAll(cycle, vertex);

        for (Vertex u : this) {
            int minWeight = INFINITY;
            int minZeroWeight = INFINITY;
            XEdge minEdge = null;
            XEdge minZeroEdge = null;
            for (Vertex v : vertex.collab) {
                XEdge e = getEdge(getVertex(u), getVertex(v)); //Inefficient TODO
                if (e != null && minWeight > e.getModifyWeight() && e.getModifyWeight() > 0) {
                    minWeight = e.getModifyWeight();
                    minEdge = new XEdge(getVertex(u), vertex, e.getModifyWeight());
                    minEdge.setMinEdgeTo(getVertex(v));
                }
                if (e != null && minZeroWeight > e.getModifyWeight() && e.getModifyWeight() == 0){
                    minZeroWeight = e.getModifyWeight();
                    minZeroEdge = new XEdge(getVertex(u), vertex, e.getModifyWeight());
                    minZeroEdge.setMinEdgeTo(getVertex(v));
                }
            }
            if (minEdge != null) {
                minEdge.enable();
                vertex.xrevAdj.add(minEdge);
                getVertex(u).xadj.add(minEdge);
            } else if (minZeroEdge != null) {
                minZeroEdge.enable();
                vertex.xrevAdj.add(minZeroEdge);
                getVertex(u).xadj.add(minZeroEdge);
            }
        }

        for (Vertex u : this) {
            int minWeight = INFINITY;
            int minZeroWeight = INFINITY;
            XEdge minEdge = null;
            XEdge minZeroEdge = null;
            for (Vertex v : vertex.collab) {
                XEdge e = getRevEdge(getVertex(u), getVertex(v)); //Inefficient TODO
                if (e != null && minWeight > e.getModifyWeight() && e.getModifyWeight() > 0) {
                    minWeight = e.getModifyWeight();
                    minEdge = new XEdge(vertex, getVertex(u), e.getModifyWeight());
                    minEdge.setMinEdgeFrom(getVertex(v));
                }
                if (e != null && minZeroWeight > e.getModifyWeight() && e.getModifyWeight() == 0){
                    minZeroWeight = e.getModifyWeight();
                    minZeroEdge = new XEdge(vertex, getVertex(u), e.getModifyWeight());
                    minZeroEdge.setMinEdgeFrom(getVertex(v));
                }
            }
            if (minEdge != null) {
                minEdge.enable();
                vertex.xadj.add(minEdge);
                getVertex(u).xrevAdj.add(minEdge);
            } else if (minZeroEdge != null){
                minZeroEdge.enable();
                vertex.xadj.add(minZeroEdge);
                getVertex(u).xrevAdj.add(minZeroEdge);
            }
        }
    }

    public void unRavel(XVertex u) {
        System.out.println("DISABLING: " + u);
        if (u.size() <= 1) {
            return;
        }
        for (Vertex v : u.collab) {
            XVertex xv = getVertex(v);
            xv.enable();
        }

//        u.disable();
//        decrementSize();
    }

    void disableAll(List<Vertex> cycle, XVertex parent) {
        for (Vertex v : cycle) {
            XGraph.XVertex k = this.getVertex(v);
            k.disable();
            k.setVertex(parent);
        }
    }

    @Override
    public Vertex getVertex(int n) {
        return xv[n - 1];
    }

    XVertex getVertex(Vertex u) {
        return Vertex.getVertex(xv, u);
    }

    /**
     * Method to reverse the edges of a graph.  Applicable to directed graphs only.
     */
    public void reverseGraph() {
        if (directed) {
            for (Graph.Vertex u : this) {
                List<XGraph.XEdge> tmp = getVertex(u).getAdj();
                getVertex(u).xadj = getVertex(u).getRevAdj();
                getVertex(u).xrevAdj = tmp;
            }
        }
    }

    public static void main(String[] args) {
        Graph g = Graph.readGraph(new Scanner(System.in));
        XGraph xg = new XGraph(g);
        Vertex src = xg.getVertex(1);
    }

    void printGraph(BFS b) {
        for (Vertex u : this) {
            System.out.print("  " + u + "  :  " + (b.distance(u) == INFINITY ? "INF" : b.distance(u)) + "  :  ");
            for (Edge e : u) {
                System.out.print(e);
            }
            System.out.println();
        }

    }

    public int size() {
        return graphSize;
    }

    void incrementSize() {
        graphSize++;
    }

    void decrementSize() {
        graphSize--;
    }

    XEdge getEdge(XVertex u, XVertex v) {
        for (XEdge e : u.getAdj()) {
            XVertex x = getVertex(e.otherEnd(u));
            if (x == v) {
                return e;
            }
        }
        return null;
    }

    XEdge getRevEdge(XVertex u, XVertex v) {
        for (XEdge e : u.getRevAdj()) {
            XVertex x = getVertex(e.otherEnd(u));
            if (x == v) {
                return e;
            }
        }
        return null;
    }

    int originalSize(){
        return this.original.size();
    }

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