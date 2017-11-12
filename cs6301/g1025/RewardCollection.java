package cs6301.g1025;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;

public class RewardCollection {

	Graph g;
	Vertex s;
	HashMap<Vertex, Integer> vertexRewardMap;
	List<Path> SPaths;

	public RewardCollection(Graph g, Vertex s, HashMap<Vertex, Integer> vertexRewardMap) {
		this.g = g;
		this.s = s;
		this.vertexRewardMap = vertexRewardMap;

	}

	int rewardProblem(List<Vertex> tour) {
		XGraph xg = new XGraph(g);
		BellmanFordFast.BellmanFord(xg, xg.getVertex(s));
		// System.out.println(xg.toString());
		// for (Vertex v : xg) {
		// //System.out.println("for vertex " + (v.getName() + 1) + " " +
		// xg.getVertex(v).distance);
		// }

		for (Vertex u : xg) {
			XVertex xu = (XVertex) u;
			xu.seen = false;
			xu.count = 0;
			if (xu.distance == null) {
				xg.disable(xu.getName() + 1);
			}

		}

		List<Graph.Vertex> SPath = new ArrayList<Graph.Vertex>();
		xg.getVertex(s).seen = true;
		SPath.add(s);
		SPaths = new ArrayList<Path>();
		enumerate(xg, xg.getVertex(s), SPath);
		Collections.sort(SPaths);
		for (Path prev : SPaths) {
			for (Path p : SPaths) {
				if (checkDisjoint(prev, p, s)) {
					addTour(prev.path, p.path, tour);
					return prev.pathreward;
				}
			}

		}
		return 0;

	}

	void addTour(ArrayList<Vertex> list1, ArrayList<Vertex> list2, List<Vertex> tour) {
		for (Vertex v : list1) {
			tour.add(v);
		}
		ListIterator<Vertex> li = list2.listIterator(list2.size());
		while (li.hasPrevious()) {
			tour.add(li.previous());
		}

	}

	void enumerate(XGraph xg, XVertex s, List<Graph.Vertex> SPath) {
		for (Edge e : s) {
			XVertex xv = xg.getVertex(e.otherEnd(s));
			if (!xv.seen) {
				if (xv.distance == s.distance + e.weight) {
					if (xv.count != 0) {
						storePath(SPath, e);
					}
					SPath.add(e.otherEnd(s));
					xv.seen = true;
					xv.count = xv.count + 1;
					enumerate(xg, xv, SPath);
					xv.seen = false;
					SPath.remove(SPath.size() - 1);
				} else {
					storePath(SPath, e);
				}
			}

		}

		return;

	}

	class Path implements Comparable<Path> {
		ArrayList<Vertex> path;
		Integer pathreward;
		Edge crossEdge;

		Path(ArrayList<Vertex> path, int totalreward, Edge crossEdge) {
			this.path = path;
			this.pathreward = totalreward;
			this.crossEdge = crossEdge;
		}

		@Override
		public int compareTo(Path p) {
			return p.pathreward.compareTo(this.pathreward);
		}

		@Override
		public String toString() {
			System.out.println(path.toString() + " " + crossEdge + " " + pathreward);
			return "";
		}

	}

	boolean checkDisjoint(Path p1, Path p2, Vertex s) {

		if (p1.path.size() > 1 && p2.path.size() > 1 && p1.path.get(1).equals(p2.path.get(1))) {
			return false;
		}
		if (!(p1.crossEdge.from.equals(p2.crossEdge.to) && p1.crossEdge.to.equals(p2.crossEdge.from))) {
			return false;
		}
		ArrayList<Vertex> list1 = p1.path;
		ArrayList<Vertex> list2 = p2.path;
		HashSet<Vertex> h1 = new HashSet<Vertex>();
		if (list1.size() > list2.size()) {
			list1 = p2.path;
			list2 = p1.path;
		}

		for (Vertex v : list1) {
			h1.add(v);
		}
		for (Vertex u : list2) {
			if ((!u.equals(s)) && h1.contains(u)) {
				return false;
			}
		}
		return true;

	}

	void storePath(List<Graph.Vertex> SPath, Edge e) {

		ArrayList<Graph.Vertex> list = new ArrayList<Graph.Vertex>();
		int totalreward = 0;
		for (Vertex v : SPath) {
			totalreward += vertexRewardMap.get(v);
			list.add(v);
		}
		Path p = new Path(list, totalreward, e);
		SPaths.add(p);

	}

}
