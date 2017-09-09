package cs6301.g25.sp1;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import cs6301.g00.Graph;

public class Diameter {

	/**
	 *New class to add additional fields to the Vertex class.
	 *
	 */
	class VertexBfs {
		boolean seen;
		int distance;
		Graph.Vertex element;
		Graph.Vertex parent;
		
		//Constructor
		VertexBfs(Graph.Vertex u) {
			element = u;
			seen = false;
			distance = 0;
			parent = null;
		}
	}
	
	VertexBfs[] vertexBfs;
	Graph g;

	
	public Diameter(Graph g){
		this.g = g;
		vertexBfs = new VertexBfs[g.size()];
		for (Graph.Vertex u : g) {
			vertexBfs[u.getName()] = new VertexBfs(u);
		}
	}
	
	/**
	 *Parameter to set seen status of a parameterized Vertex to true;
	 */
	void visit(Graph.Vertex u) {
		VertexBfs v = getVertexBfs(u);
		v.seen = true;
	}
	/**
	 *returns parent of a node in a graph or a tree
	 */
	void parent(Graph.Vertex u, Graph.Vertex v){
		VertexBfs bU = getVertexBfs(u);
		bU.parent = v;
	}
	
	/**
	 *returns new parameterized vertex corresponding to general vertex
	 */
	VertexBfs getVertexBfs(Graph.Vertex u) {
		return vertexBfs[u.getName()];
	}

	/**
	 *returns general Vertex corresponding to parameterized vertex
	 */
	Graph.Vertex getVertex(VertexBfs c) {
		return c.element;
	}
	
	/**
	 *returns the visited status of a vertex as true or false
	 */
	boolean seen(Graph.Vertex u) {
		VertexBfs ccu = getVertexBfs(u);
		return ccu.seen;
	}
	
	/**
	 *sets distance distance to u from the source of the tree
	 */
	void distance(Graph.Vertex u, int distance){
		VertexBfs bfsu = getVertexBfs(u);
		bfsu.distance = distance;
	}
	
	/**
	 *returns distance of a parameterized vertex 'u' from the source
	 */
	int getDistance(Graph.Vertex u){
		VertexBfs bfsu = getVertexBfs(u);
		return bfsu.distance;
	}
	/**
	 *BFS implementation which returns maximum distance node from the source 
	 */
	public Graph.Vertex bfs(Graph.Vertex source) {
		Queue<Graph.Vertex> q = new ArrayDeque<>();
		q.add(source);
		visit(source);
		distance(source, 0);
		Graph.Vertex max = source;
		while (!q.isEmpty()) {
			Graph.Vertex u = q.poll();
			for (Graph.Edge e : u.getAdj()) {
				Graph.Vertex v = e.otherEnd(u);
				if (!seen(v)) {
					q.add(v);
					parent(v, u);//sets u as parent of v
					visit(v);
					distance(v, 1 + getDistance(u));
					max = updateMax(v, max);
				}
			}
		}
		return max;
	}
	/**
	 *returns the max distance node among u and v from the source
	 */
	Graph.Vertex updateMax(Graph.Vertex u, Graph.Vertex v){
		VertexBfs uB = getVertexBfs(u);
		VertexBfs vB = getVertexBfs(v);
		if(vB.distance < uB.distance) return u;
		else return v;
	}
	
	/**
	 *initializes by resetting the vertices in the parameterized Vertex array 
	 */
	void resetVertices(){
		for(VertexBfs v : vertexBfs){
			v.distance=0;
			v.seen=false;
			v.parent=null;
		}	
	}

	public LinkedList<Graph.Vertex>  getDiameter(){	
		LinkedList<Graph.Vertex> path =  new LinkedList<Graph.Vertex>();
		if(g==null||g.size()==0){
			return path;
		}
		Random r = new Random();
		Graph.Vertex maxDistVertex = bfs( g.getVertex(r.nextInt(g.size()) + 1));//add 1 since n is exclusive in rand
		System.out.println(maxDistVertex);
		printList (maxDistVertex);
		resetVertices();
		
		maxDistVertex = bfs(maxDistVertex);
		System.out.println(maxDistVertex);
		path=printList (maxDistVertex);
		return path;
	}
	/**
	 *Testing purpose only, not part of the assignment
	 */
	LinkedList<Graph.Vertex>  printList (Graph.Vertex tmp){
		LinkedList<Graph.Vertex> path =  new LinkedList<Graph.Vertex>();
		while(tmp != null){
			path.add(tmp);
			tmp = getVertexBfs(tmp).parent;
		}
		System.out.println("Longest Path: " + path);
		return path;
		
	}
	
	


}
