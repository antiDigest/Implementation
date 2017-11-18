package cs6301.g1025;

import java.util.LinkedList;
import java.util.Queue;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;

public class BellmanFordFast {
	
	/**
	 * Input graph -outputs the shortest distances for all nodes
	 */
	static boolean BellmanFord(Graph g, Vertex s) {
	
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
					XVertex xv = (XVertex) e.otherEnd(xu);
					if (xv.distance == null || xv.distance >xu.distance + e.weight) {
						xv.distance = xu.distance + e.weight;
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

