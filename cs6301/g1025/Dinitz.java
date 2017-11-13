/**
 * @author Antriksh, Gunjan, Saikumar, Swaroop
 * Version 1: Implementing
 */

package cs6301.g1025;


import java.util.List;

public class Dinitz {

    Graph g;
    Graph.Vertex source;
    Graph.Vertex sink;

    Dinitz(Graph g, Graph.Vertex src, Graph.Vertex sink){
        this.g = new XGraph(g);
        this.source = this.g.getVertex(src.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    class MinCut{
        int maxFlow;
        // TODO add min cut {S, T}
    }

    int maxFlow(){
        BFS b = new BFS((XGraph) g, (XGraph.XVertex) source);
        while(true){
            b.bfs();
            BFS.BFSVertex bsink = b.getVertex(sink);
            if(bsink.parent == null){
                break;
            }
            List<Graph.Edge> pathEdges = getPath(Graph.Vertex source, Graph.Vertex sink); // TODO
            int minCapacity = minCapacity(pathEdges);
            for(Graph.Edge e:pathEdges){
                /** TODO:
                if graph.edges contains e => e.flow = e.flow + minCapacity
                else eReverse.flow = eReverse.flow - minCapacity
                 */
            }
        }
        // TODO: return flow
        return 0;
    }

    int minCapacity(List<Graph.Edge> path){
        //TODO
        return 0;
    }

}
