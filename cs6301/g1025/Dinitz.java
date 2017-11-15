/**
 * @author Antriksh, Gunjan, Saikumar, Swaroop
 * Version 1: Implementing
 */

package cs6301.g1025;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static cs6301.g1025.BFS.INFINITY;

public class Dinitz {

    Graph g;
    Graph.Vertex source;
    Graph.Vertex sink;

    Dinitz(Graph g, Graph.Vertex src, Graph.Vertex sink, HashMap<Graph.Edge, Integer> capacity) {
        this.g = new XGraph(g, capacity);
        this.source = this.g.getVertex(src.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    class MinCut {
        int maxFlow;
        // TODO add min cut {S, T}
    }

    int maxFlow() {
        BFS b = new BFS((XGraph) g, (XGraph.XVertex) source);
        while (true) {
            b.bfs();
            BFS.BFSVertex bsink = b.getVertex(sink);
            if (bsink.parent == null || bsink.distance == INFINITY) {
                break;
            }
            List<Graph.Edge> pathEdges = getPath(source, sink, b);
            int minCapacity = minCapacity(pathEdges);
            for (Graph.Edge e : pathEdges) {
                /** TODO:
                 if graph.edges contains e => e.flow = e.flow + minCapacity
                 else eReverse.flow = eReverse.flow - minCapacity
                 */
            }
        }
        // TODO: return flow
        return 0;
    }

    int minCapacity(List<Graph.Edge> path) {
        //TODO: from all capacities select the one having min capacity
        return 0;
    }

    List<Graph.Edge> getPath(Graph.Vertex src, Graph.Vertex snk, BFS b) {
        //TODO: get a path of edges from source to sink

        List<Graph.Edge> L = new LinkedList<>();
        BFS.BFSVertex bsink = b.getVertex(snk);
        BFS.BFSVertex bsource = b.getVertex(source);
        while (bsink.parent != source) {
            L.add(bsink.parentEdge);
            bsink = b.getVertex(bsink.parent);
        }

        L.add(bsink.parentEdge);

        System.out.println("PATH: " + L);

        return L;
    }

}
