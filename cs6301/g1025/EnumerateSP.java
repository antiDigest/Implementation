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
			Queue<Vertex> q = new LinkedList<Vertex>();
			long[] count = new long[xg.size()];
			count[s.name] = 1;
			q.add(s);
			while (!q.isEmpty()) {
				Vertex u = q.poll();
				XVertex xu = xg.getVertex(u);
				for (Edge e : xu) {
					if (count[e.to.name]<=0)
						q.add(e.to);
					count[e.to.name] += count[e.from.name];

				}
			}
			return count[t.name];
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

		boolean isNegCycle = BellmanFord(g, s);
		if (!isNegCycle) {
			System.out.println("Non-positive cycle in graph.  Unable to solve problem");
			return false;
		} else {
			XGraph xg = (XGraph) g;
			for (Vertex u : g) {
				XVertex xu = (XVertex) u;
				if (xu.distance == null) {
					xg.disable(xu.getName() + 1);
				}
				for (Edge e : u) {
					XVertex xv = (XVertex) e.otherEnd(u);
					if (xv.distance != e.weight + xu.distance) {
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

	boolean BellmanFord(Graph g, Vertex s) {

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
				}
				if (!xv.seen) {
					xv.seen = true;
					q.add(xv);
				}

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
