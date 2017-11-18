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

	Graph g;
	Vertex s;
	Vertex t;

	/**
	 * Constructor for EnmerateSP
	 * 
	 * @param s
	 *            : Vertex - Source s
	 * @param t
	 *            : Vertex - Source t
	 * @param g:
	 *            Graph g
	 * 
	 */
	EnumerateSP(Graph g, Vertex s, Vertex t) {
		this.g = g;
		this.s = s;
		this.t = t;

	}

	/**
	 * returns the count of shortest paths between Source and destination for a
	 * given graph
	 * 
	 */
	long Count() {
		XGraph xg = new XGraph(g);
		if (!EnumOrCount(xg)) {
			return -1;
		} else {
			List<Graph.Vertex> topList = new ArrayList<Graph.Vertex>();
			TopologicalOrders c = new TopologicalOrders(xg);
			c.toplogicalOrder(xg, topList);
			long[] count = new long[topList.size()];
			count[s.getName()] = 1;
			for (Vertex u : topList) {
				for (Edge e : u) {
					count[e.otherEnd(u).getName()] += count[u.getName()];
				}
			}
			return count[t.getName()];
		}

	}

	/**
	 * prints the total paths and returns the count
	 * 
	 */
	long Enumerate() {
		XGraph xg = new XGraph(g);
		if (!EnumOrCount(xg)) {
			return -1;
		}
		List<Graph.Vertex> SPath = new ArrayList<Graph.Vertex>();
		SPath.add(t);
		long[] count = new long[1];
		EnumerateDFS(xg, xg.getVertex(t), xg.getVertex(s), SPath, count);
		return count[0];
	}

	/**
	 * 
	 * @param xg:
	 *            XGraph xg returns boolean-checks whether Problem can be solved
	 *            or not.
	 */
	boolean EnumOrCount(XGraph xg) {

		boolean isNegCycle = BellmanFordFast.BellmanFord(g, xg.getVertex(s));
		if (!isNegCycle) {
			System.out.println("Non-positive cycle in graph.  Unable to solve problem");
			return false;
		} else {
			for (Vertex u : xg) {
				XVertex xu = (XVertex) u;
				for (Edge e : u) {
					XVertex xv = (XVertex) e.otherEnd(u);
					if (xu.distance == null || xv.distance != e.weight + xu.distance) {
						XEdge xe = (XEdge) e;
						xe.setDisabled(true);
					}
				}
			}
			return true;
		}
	}

	/**
	 * 
	 * @param g
	 *            : Graph g
	 * @param s
	 *            : Vertex
	 * @param t
	 *            : Vertex
	 * @param count
	 *            : Count array
	 * @param SPath:path
	 *            to store all vertices and prints
	 * 
	 *            returns boolean-enumerates and counts all the paths
	 */

	void EnumerateDFS(XGraph g, XVertex t, XVertex s, List<Graph.Vertex> SPath, long[] count) {

		if (s.equals(t)) {
			printPath(SPath);
			if (SPath.size() < 2)
				return;
			else {
				count[0]++;
				return;
			}
		}
		for (Edge e : t.xrevadj) {
			XEdge xe = (XEdge) e;
			if (!xe.disabled) {
				SPath.add(0, e.otherEnd(t));
				EnumerateDFS(g, g.getVertex(e.otherEnd(t)), s, SPath, count);
				SPath.remove(0);
			}
		}

		return;
	}

	void printPath(List<Graph.Vertex> SPath) {

		for (Vertex v : SPath) {
			if (v.equals(SPath.get(SPath.size() - 1))) {
				System.out.print(v.getName() + 1);
			} else {
				System.out.print(v.getName() + 1 +" ");
			}
		}
		System.out.println();
	}

}