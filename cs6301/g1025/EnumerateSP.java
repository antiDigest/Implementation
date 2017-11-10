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

	long Enumerate(Graph g, Vertex s, Vertex t) {
		XGraph xg = new XGraph(g);
		boolean isNegCycle = BellmanFord(xg, xg.getVertex(s), t);
		if (!isNegCycle) {
			System.out.println("Non-positive cycle in graph.  Unable to solve problem");
			return -1;
		} else {
			for (Vertex v : xg) {
				XVertex xv = (XVertex) v;
				if (xv.distance == null) {
					//System.out.println("Disabled Vertices are " + (xv.getName() + 1));
					xg.disable(xv.getName()+1);
				}
			}
			List<Graph.Vertex> SPath = new ArrayList<Graph.Vertex>();
			SPath.add(s);
	        return EnumerateDFS(xg, s, t, SPath);
	     }
	}

	long EnumerateDFS(Graph g, Vertex s, Vertex t, List<Graph.Vertex> SPath) {
		long count = 0;
		if (s.equals(t)) {
			printPath(SPath);
			if(SPath.size()<2) return 0;
			else{
			count++;
			return count;}
		}
		for (Edge e : s) {
			SPath.add(e.otherEnd(s));
			count = count + EnumerateDFS(g, e.otherEnd(s), t, SPath);
			SPath.remove(SPath.size() - 1);
		}
		
		return count;
	}

	boolean BellmanFord(Graph g, Vertex s, Vertex t) {

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
				if (xv.distance == null || xv.distance >= xu.distance + e.weight) {
					xv.distance = xu.distance + e.weight;
				} else {
					XEdge xe = (XEdge) e;
					//System.out.println(xe);
					xe.setDisabled(true);
				}
				if (!xv.seen)
					xv.seen = true;
				q.add(xv);

			}

		}
		return true;

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
