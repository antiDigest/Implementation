package cs6301.g1025;

import java.util.LinkedList;
import java.util.Queue;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;

import static cs6301.g1025.XGraph.INFINITY;

public class BellmanFord {

    /**
     * Input graph -outputs the shortest distances for all nodes
     */
    static boolean bellmanFord(Graph g, Vertex s) {

        Queue<Vertex> q = new LinkedList<Vertex>();
        XVertex xs = (XVertex) s;
        xs.distance = 0;
        xs.seen = true;
        q.add(xs);
        while (!q.isEmpty()) {
            XVertex xu = (XVertex) q.remove();
            xu.seen = false;
            xu.count = xu.count + 1;
            if (xu.count >= g.size()) {
                return false;
            }
            for (Edge e : xu) {
                XGraph.XEdge xe = (XGraph.XEdge) e;
                XVertex xv = (XVertex) e.otherEnd(xu);
                if (xv.distance == INFINITY || xv.distance > xu.distance + xe.weight) {
                    xv.distance = xu.distance + xe.weight;
                    xv.parent = xu;
                    xv.parentEdge = xe;
                    if (!xv.seen) {
                        xv.seen = true;
                        q.add(xv);
                    }
                }

            }

        }
        return true;

    }
}
