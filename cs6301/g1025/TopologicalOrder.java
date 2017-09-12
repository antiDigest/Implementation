package cs6301.g1025.sp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import cs6301.g00.Graph;

public class TopologicalOrder extends GraphAlgorithm<TopologicalOrder.TOPVertex> {

	Graph.Vertex src;
	int time;
	int cNo;
	int topNum;
	List<Graph.Vertex> decFinList;
	
	public TOPVertex top(Graph.Vertex u){
		return Graph.Vertex.getVertex(node, u);
	}

	public TopologicalOrder(Graph g) {
		super(g);
		node = new TOPVertex[g.size()];
		// Create array for storing vertex properties
		for (Graph.Vertex u : g) {
			node[u.getName()] = new TOPVertex(u);
		}
	}

	static class TOPVertex {
		boolean seen;
		int dis;// discovered time
		int fin;// finish time
		Graph.Vertex parent;
		int inDegree;
		int top;
		int cno;

		TOPVertex(Graph.Vertex u) {
			seen = false;
			dis = 0;
			fin = 0;
			parent = null;
			inDegree = 0;
			top = 0;
			cno = 0;
		}
	}

	int getInDegree(Graph.Vertex v) {
		return getVertex(v).inDegree;
	}
//
	void setInDegree(Graph.Vertex v, int val) {
		TOPVertex tv = getVertex(v);
		tv.inDegree = val;
	}

//	Graph.Vertex getParent(Graph.Vertex u) {
//		return getVertex(u).parent;
//	}

//	boolean seen(Graph.Vertex u) {
//		return getVertex(u).seen;
//	}

//	void visit(Graph.Vertex v) {
//		TOPVertex dv = getVertex(v);
//		dv.seen = true;
//
//	}

//	void parent(Graph.Vertex u, Graph.Vertex v) {
//		TOPVertex dv = getVertex(v);
//		dv.parent = u;
//	}

//	void discover(Graph.Vertex v) {
//		TOPVertex dv = getVertex(v);
//		dv.dis = ++time;
//
//	}

//	void finish(Graph.Vertex v) {
//		TOPVertex dv = getVertex(v);
//		dv.fin = ++time;
//	}

//	void top(Graph.Vertex u, int val) {
//		TOPVertex tv = getVertex(u);
//		tv.top = val;
//	}

	/**
	 * Algorithm 1. Remove vertices with no incoming edges, one at a time, along
	 * with their incident edges, and add them to a list.
	 */
	public List<Graph.Vertex> toplogicalOrder1(Graph g) {
		int topNum = 0;
		Queue<Graph.Vertex> q = new ArrayDeque<Graph.Vertex>();
		List<Graph.Vertex> topList = new ArrayList<Graph.Vertex>();
		for (Graph.Vertex u : g) {
			top(u).inDegree = u.getRevAdj().size();
			//setInDegree(v, v.getRevAdj().size());
			if (top(u).inDegree == 0)
				q.add(u);
		}

		while (!q.isEmpty()) {
			Graph.Vertex u = q.poll();
			top(u).top = ++topNum;
			// top(u, ++topNum);
			topList.add(u);
			for (Graph.Edge e : u) {
				Graph.Vertex v = e.otherEnd(u);
				top(v).inDegree--;
				//setInDegree(v, getInDegree(v) - 1);
				if (top(v).inDegree == 0)
					q.add(v);
			}
		}
		if (topNum != g.size())
			return null;
		return topList.size() == 0 ? null : topList;

	}

	/**
	 * Algorithm 2. Run DFS on g and add nodes to the front of the output list,
	 * in the order in which they finish. Try to write code without using global
	 * variables.
	 */
	public List<Graph.Vertex> toplogicalOrder2(Graph g) {
		Iterator<Graph.Vertex> it = g.iterator();
		dfs(it);
		return decFinList;

	}

	void dfs(Iterator<Graph.Vertex> it) {
		topNum = g.size();
		time = 0;
		cNo = 0;
		decFinList = new LinkedList<Graph.Vertex>();
		// for u in V do u.seen = false
		for (Graph.Vertex u : g) {
			getVertex(u).seen = false;
		}
		while (it.hasNext()) {
			Graph.Vertex u = it.next();
			if (!getVertex(u).seen) {
				cNo++;
				dfsVisit(u);
			}
		}
	}

	void dfsVisit(Graph.Vertex u) {
		getVertex(u).seen = true;
		getVertex(u).dis = ++time;
		getVertex(u).cno = cNo;
		for (Graph.Edge e : u) {
			Graph.Vertex v = e.otherEnd(u);
			if (!getVertex(v).seen) {
				getVertex(v).parent = u;
				dfsVisit(v);
			}
		}
		getVertex(u).fin = ++time;
		getVertex(u).top = topNum--;
		decFinList.add(0, u);
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner;
		if (args.length > 0) {
			File in = new File(args[0]);
			scanner = new Scanner(in);
		} else {
			System.out.println("Enter the graph: ");
			scanner = new Scanner(System.in);
		}

		Graph graph = Graph.readGraph(scanner, true);
		TopologicalOrder tp = new TopologicalOrder(graph);
		List<Graph.Vertex> out1 = tp.toplogicalOrder1(graph);
		System.out.println(out1);
		List<Graph.Vertex> out2 = tp.toplogicalOrder2(graph);
		System.out.println(out2);
		return;
	}

}
