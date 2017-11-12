package cs6301.g1025;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XEdge;
import cs6301.g1025.XGraph.XVertex;

public class EnumerateSP {

	long Count(Graph g, Vertex s, Vertex t) {
		XGraph xg = new XGraph(g);
		if (!EnumOrCount(xg, xg.getVertex(s))) {
			return -1;
		} else {
			List<Graph.Vertex> topList = new ArrayList<Graph.Vertex>();
			TopologicalOrders c = new TopologicalOrders(xg);
            c.toplogicalOrder(xg, topList);
			long[] count = new long[topList.size()];
			count[s.getName()]=1;
			for(Vertex u:topList){
				for(Edge e:u){
					count[e.otherEnd(u).getName()] += count[u.getName()];
				}
			}
			return count[t.getName()];
		}

	}
	
	

	long Enumerate(Graph g, Vertex s, Vertex t) {
		XGraph xg = new XGraph(g);
		if (!EnumOrCount(xg, xg.getVertex(s))) {
			return -1;
		}
		List<Graph.Vertex> SPath = new ArrayList<Graph.Vertex>();
		SPath.add(s);
		return EnumerateDFS(xg, xg.getVertex(s), xg.getVertex(t), SPath);
	}

	boolean EnumOrCount(Graph g, Vertex s) {
        boolean isNegCycle = BellmanFordFast.BellmanFord(g, s);
		if (!isNegCycle) {
			System.out.println("Non-positive cycle in graph.  Unable to solve problem");
			return false;
		} else {
			XGraph xg = (XGraph) g;
			for (Vertex u : xg) {
				XVertex xu = (XVertex) u;
			    for (Edge e : u) {
					XVertex xv = (XVertex) e.otherEnd(u);
					if (xu.distance == null||xv.distance != e.weight + xu.distance) {
						XEdge xe = (XEdge) e;
						xe.setDisabled(true);
					}
				}

			}
			return true;
		}
	}

	long EnumerateDFS(Graph g, Vertex s, Vertex t, List<Graph.Vertex> SPath) {
		long count = 0;
		if (s.equals(t)) {
			printPath(SPath);
			if (SPath.size() < 2)
				return 0;
			else {
				count++;
				return count;
			}
		}
		for (Edge e : s) {
            SPath.add(e.otherEnd(s));
			count = count + EnumerateDFS(g, e.otherEnd(s), t, SPath);
			SPath.remove(SPath.size() - 1);
		}

		return count;
	}

	

	void printPath(List<Graph.Vertex> SPath) {

		for (Vertex v : SPath) {
			if (v.equals(SPath.get(SPath.size() - 1))) {
				System.out.print(v.getName() + 1);
			} else {
				System.out.print(v.getName() + 1 + ",");
			}
		}
		System.out.println();
	}

}