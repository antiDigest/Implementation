/**
 * @author Antriksh, Gunjan, Saikumar, Swaroop
 * Version 1: Implementing
 */

package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XEdge;

import java.util.*;

public class Dinitz {
    public static final int INFINITY = Integer.MAX_VALUE;
    BFS bfshandle;
    Graph g;
    Vertex source;
    Vertex sink;

    Dinitz(Graph g, Vertex s, Vertex t) {
        this.g = g;
        this.source = s;
        this.sink = t;
        bfshandle = new BFS(g, s);
    }

    /*Approach:
     * updating minflow for a path while enumerating augmenting paths- (optimizing like reward problem  in lp4 without storing all paths) and
     * skipping over edges with the help of iterator
    */
    int EnumBFSTree(Vertex s, int flow, Iterator<Edge> itr) {
        if (s.equals(sink)) {
            return flow;
        }
        while (itr.hasNext()) {
            XEdge xe = (XEdge) itr.next();
            if (bfshandle.distance(xe.otherEnd(s)) == bfshandle.distance(s) + 1) {
                if(flow>xe.capacity-xe.flow){
                    flow=xe.capacity-xe.flow;
                }
                int minedgeflow = EnumBFSTree(xe.otherEnd(s), flow,
                        xe.otherEnd(s).iterator());
                if (minedgeflow > 0) {
                    xe.flow = xe.flow + minedgeflow;
                    xe.reverseEdge.flow =xe.reverseEdge.flow-minedgeflow;
                    return minedgeflow;
                }
            }
        }

        return -1;//meaning didnt find the augmenting path
    }

    /**
     * Caclulating maximum flow
     * @return
     */
    int maxFlow() {
        int flow = 0;
        while (true) {
            bfshandle.bfs();
            if (!bfshandle.getVertex(sink).seen)
                break;
            while (true) {
                int minflow = EnumBFSTree(source, INFINITY, source.iterator());
                if (minflow == -1) {
                    break;
                } else {
                    flow = flow + minflow;
                }
            }
            bfshandle.reinitialize(source);
        }
        return flow;
    }

}
