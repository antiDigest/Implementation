package cs6301.g1025;

import java.util.ArrayDeque;
import java.util.ArrayList;

import java.util.List;
import java.util.Queue;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

public class TopologicalOrders extends GraphAlgorithm<TopologicalOrders.TOPVertex> {

	
	
	
	
	/**
     * Constructor for Topological Orders
     * @param g :  Graph g
     * 
     */
	public TopologicalOrders(Graph g) {
		super(g);
		node = new TOPVertex[g.size()];
		for (Vertex u : g) {
			node[u.getName()] = new TOPVertex(u);

		}
	}

	public TOPVertex getTopVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}

	/**
     * 
     *TOPVertex class to store information to solve  this algorithm.
     * 
     */
	static class TOPVertex {
		int inDegree;
		boolean seen;

		public boolean isSeen() {
			return seen;
		}

		public void setSeen(boolean seen) {
			this.seen = seen;
		}

		public int getInDegree() {
			return inDegree;
		}

		public void setInDegree(int inDegree) {
			this.inDegree = inDegree;
		}

		TOPVertex(Graph.Vertex u) {
			this.inDegree = u.getRevAdjSize();
			this.seen = false;
		}
	}

	
	/**
     * 
     * @param g :  Graph g
     * @param toPrint : boolean  Whether to print the topological orders
     * returns the number of topological orders
     */
	long enumerateTopOrders(Graph g, boolean toPrint) {
		List<Graph.Vertex> topList = new ArrayList<Graph.Vertex>();
		long[] count=new long[1];
		Enumerate(g, topList, toPrint,count);
		return count[0];
	}

	
	/**
     * 
     * @param g :  Graph g
     * returns the number of topological orders
     */
	public long countToporders(Graph g) {
		return enumerateTopOrders(g, false);
	}

	/**
     * 
     * @param g :  Graph g
     * @param List:topList - list to print topological orders
     * @param toPrint-Whether toPrint the order or not
     * @param-count[] array stores the counts.
     */
	void  Enumerate(Graph g, List<Graph.Vertex> topList, boolean toPrint,long[] count) {
		
		if (topList.size() == g.size()) {
			count[0]++;
			if (toPrint)
				printResult(topList);
		}
		for (Vertex u : g) {
			if (!getTopVertex(u).isSeen() && getTopVertex(u).getInDegree() == 0) {
				topList.add(u);
				getTopVertex(u).setSeen(true);
				changeIndegree(u, -1);
				Enumerate(g, topList, toPrint,count);
				changeIndegree(u, +1);
				getTopVertex(u).setSeen(false);
				topList.remove(topList.size() - 1);
			}
		}
		return;

	}

	/**
     *@param List:topList - prints the path
     * 
     */
	void printResult(List<Graph.Vertex> topList) {

		for (Vertex v : topList) {
			if (v.equals(topList.get(topList.size() - 1))) {
				System.out.print(v.getName() + 1);
			} else {
				System.out.print(v.getName() + 1 + " ");
			}
		}
		System.out.println();
	}

	
	/**
     * Utility function to just change the indegree of the outgoing vertices of given vertex
     * @param Vertex:u
     * @param int:one
     */
	void changeIndegree(Vertex u, int one) {
		for (Edge e : u) {
			Vertex v = e.otherEnd(u);
			getTopVertex(v).setInDegree(getTopVertex(v).getInDegree() + one);
		}
	}
	
	/**
     * Generic function - Given a graph:adds the topological order to the argument list.
     * @param Graph:g
     * @param List:topList
     */

	public List<Graph.Vertex> toplogicalOrder(Graph g,List<Graph.Vertex> topList) {
		Queue<Graph.Vertex> q = new ArrayDeque<Graph.Vertex>();
		for (Graph.Vertex u : g) {
			if (getTopVertex(u).inDegree == 0)
				q.add(u);
		}
		while (!q.isEmpty()) {
			Graph.Vertex u = q.poll();
			topList.add(u);
			for (Graph.Edge e : u) {
				Graph.Vertex v = e.otherEnd(u);
				getTopVertex(v).setInDegree(getTopVertex(v).getInDegree() -1);
				if (getTopVertex(v).inDegree == 0)
					q.add(v);
			}
		}
		return topList.size() == 0 ? null : topList;

	}

}
