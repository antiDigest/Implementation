package sp3.cs6301.g1025;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cs6301.g00.Graph;


public class DFS extends GraphAlgorithm<DFS.DFSVertex>{
	private int time = 0;
	public DFS(Graph g, Graph.Vertex src) {
		super(g);
		this.src = src;
		node = new DFSVertex[g.size()];
		// Create array for storing vertex properties
		for (Graph.Vertex u : g) {
			node[u.getName()] = new DFSVertex(u);
		}
	}
	
	Graph.Vertex src;
	
	static class DFSVertex {
		boolean seen;
		int dis;//discovered time
		int fin;//finish time
		Graph.Vertex parent;
		
		DFSVertex(Graph.Vertex u) {
			seen = false;
			dis = 0;
			fin = 0;
			parent = null;
		}
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
		DFSVertex dv = getVertex(v);
		dv.seen = true;
		
	}
	
	void parent(Graph.Vertex u, Graph.Vertex v){
		DFSVertex dv = getVertex(v);
		dv.parent = u;
	}
	void discover(Graph.Vertex v){
		DFSVertex dv = getVertex(v);
		dv.dis = ++time;
		
	}
	
	void finish(Graph.Vertex v){
		DFSVertex dv = getVertex(v);
		dv.fin = ++time;
	}
	
	int getFinishTime(Graph.Vertex v){
		DFSVertex dv = getVertex(v);
		return dv.fin;
	}
	
	public static void main(String args[]) throws FileNotFoundException{
		Scanner scanner;
		
		if (args.length > 0) {
			File in = new File(args[0]);
			scanner = new Scanner(in);
		} else {
			System.out.println("Enter the graph: ");
			scanner = new Scanner(System.in);
		}
		
		Graph graph = Graph.readGraph(scanner, false);
		DFS dfs = new DFS(graph, graph.getVertex(6));
		dfs.dfs();
		for(Graph.Vertex v : dfs.g){
			System.out.print(dfs.getFinishTime(v)+ " ");
		}
	}

}
