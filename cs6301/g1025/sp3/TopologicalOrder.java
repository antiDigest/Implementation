package cs6301.g1025.sp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import cs6301.g00.Graph;
import cs6301.g1025.sp3.DFS.DFSVertex;

public class TopologicalOrder extends GraphAlgorithm<TopologicalOrder.TOPVertex>{
	
	Graph.Vertex src;
	
	static class TOPVertex {
		boolean seen;
		int dis;//discovered time
		int fin;//finish time
		Graph.Vertex parent;
		int inDegree;
		int top;
		TOPVertex(Graph.Vertex u) {
			seen = false;
			dis = 0;
			fin = 0;
			parent = null;
			inDegree = 0;
			top = 0;
		}
	}
	
	public TopologicalOrder(Graph g, Graph.Vertex src) {
		super(g);
		this.src = src;
		node = new TOPVertex[g.size()];
		// Create array for storing vertex properties
		for (Graph.Vertex u : g) {
			node[u.getName()] = new TOPVertex(u);
		}
	}

	/**
	 * Algorithm 1. Remove vertices with no incoming edges, one at a time, along
	 * with their incident edges, and add them to a list.
	 */
	int time = 0;
	int getInDegree(Graph.Vertex v){
		return getVertex(v).inDegree;
	}
	
	void setInDegree(Graph.Vertex v, int val){
		TOPVertex tv = getVertex(v);
		tv.inDegree = val;
	}

	void dfs(){
		time = 0;
		for(Graph.Vertex v : g){
			if(!seen(v)){
				dfsVisit(v);
			}
		}

	}
	
	void dfsVisit(Graph.Vertex u){
		visit(u);
		discover(u);
		for(Graph.Edge e : u){
			Graph.Vertex v = e.otherEnd(u);
			if(!seen(v)){
				parent(u,v);
				visit(v);
				dfsVisit(v);
			}
		}
		finish(u);
		visit(u);
	}
	
	Graph.Vertex getParent(Graph.Vertex u) {
		return getVertex(u).parent;
	}

	boolean seen(Graph.Vertex u) {
		return getVertex(u).seen;
	}
	
	// Visit a node v from u
	void visit(Graph.Vertex v) {
		TOPVertex dv = getVertex(v);
		dv.seen = true;
		
	}
	
	void parent(Graph.Vertex u, Graph.Vertex v){
		TOPVertex dv = getVertex(v);
		dv.parent = u;
	}
	void discover(Graph.Vertex v){
		TOPVertex dv = getVertex(v);
		dv.dis = ++time;
		
	}
	
	void finish(Graph.Vertex v){
		TOPVertex dv = getVertex(v);
		dv.fin = ++time;
	}
	
	int getFinishTime(Graph.Vertex v){
		TOPVertex dv = getVertex(v);
		return dv.fin;
	}
	
	public List<Graph.Vertex> toplogicalOrder1(Graph g) {
		int topNum = 0;
		Queue<Graph.Vertex> q = new ArrayDeque<Graph.Vertex>();
		List<Graph.Vertex> topList = new ArrayList<Graph.Vertex>();
		for(Graph.Vertex v: g){
			setInDegree(v, v.getRevAdj().size());
			if(getInDegree(v) == 0) q.add(v);
		}
		
		while(!q.isEmpty()){
			Graph.Vertex u = q.poll();
			top(u, ++topNum);
			topList.add(u);
			for(Graph.Edge e : u){
				Graph.Vertex v = e.otherEnd(u);
				setInDegree(v, getInDegree(v)-1);
				if(getInDegree(v) == 0) q.add(v);
			}
		}
		if(topNum != g.size()) return null;
		return topList.size() == 0 ? null : topList;
		

	}
	
	void top(Graph.Vertex u, int val){
		TOPVertex tv = getVertex(u);
		tv.top = val;
	}

	/**
	 * Algorithm 2. Run DFS on g and add nodes to the front of the output list,
	 * in the order in which they finish. Try to write code without using global
	 * variables.
	 */
	public List<Graph.Vertex> toplogicalOrder2(Graph g) {
		return null;

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
		TopologicalOrder tp = new TopologicalOrder(graph, graph.getVertex(4));
		List<Graph.Vertex> out1 = tp.toplogicalOrder1(graph);
		System.out.println(out1);
		List<Graph.Vertex> out2 = tp.toplogicalOrder2(graph);
		System.out.println(out2);
		return;
	}

}
