package sp3.cs6301.g1025;

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
	int topNum;//global topological number
	List<Graph.Vertex> decFinList;//LinkedList of vertices with decreasing fin times
	
	public TOPVertex top(Graph.Vertex u){
		return Graph.Vertex.getVertex(node, u);
	}
	
	/**
	 *Constructor 
	 */
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
			if (top(u).inDegree == 0)
				q.add(u);
		}

		while (!q.isEmpty()) {
			Graph.Vertex u = q.poll();
			top(u).top = ++topNum;
			topList.add(u);
			for (Graph.Edge e : u) {
				Graph.Vertex v = e.otherEnd(u);
				top(v).inDegree--;
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
		for (Graph.Vertex u : g) {
			top(u).seen = false;
		}
		while (it.hasNext()) {
			Graph.Vertex u = it.next();
			if (!top(u).seen) {
				cNo++;
				dfsVisit(u);
			}
		}
	}

	void dfsVisit(Graph.Vertex u) {
		top(u).seen = true;
		top(u).dis = ++time;
		top(u).cno = cNo;
		for (Graph.Edge e : u) {
			Graph.Vertex v = e.otherEnd(u);
			if (!top(v).seen) {
				top(v).parent = u;
				dfsVisit(v);
			}
		}
		top(u).fin = ++time;
		top(u).top = topNum--;
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
