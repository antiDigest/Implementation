package cs6301.g1025.sp3;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cs6301.g00.Graph;

//public class DAG extends GraphAlgorithm<DAG.DAGVertex> {
public class DAG {
//	int time = 0;
//	
//	public DAG(Graph g) {
//		super(g);
//		node = new DAGVertex[g.size()];
//		// Create array for storing vertex properties
//		for (Graph.Vertex u : g) {
//			node[u.getName()] = new DAGVertex(u);
//		}
//	}
//	
//	static class DAGVertex {
//		boolean seen;
//		int dis;//discovered time
//		int fin;//finish time
//		Graph.Vertex parent;
//		
//		DAGVertex(Graph.Vertex u) {
//			seen = false;
//			dis = 0;
//			fin = 0;
//			parent = null;
//		}
//	}
//	
//
//	
//	void dfs(){
//		time = 0;
//		for(Graph.Vertex v : g){
//			if(!seen(v)){
//				dfsVisit(v);
//			}
//		}
//
//	}
//	
//	void dfsVisit(Graph.Vertex u){
//		visit(u);
//		discover(u);
//		for(Graph.Edge e : u){
//			Graph.Vertex v = e.otherEnd(u);
//			if(!seen(v)){
//				parent(u,v);
//				visit(v);
//				dfsVisit(v);
//			}
//		}
//		finish(u);
//		visit(u);
//	}
//	
//	Graph.Vertex getParent(Graph.Vertex u) {
//		return getVertex(u).parent;
//	}
//
//	boolean seen(Graph.Vertex u) {
//		return getVertex(u).seen;
//	}
//	
//	// Visit a node v from u
//	void visit(Graph.Vertex v) {
//		DAGVertex dv = getVertex(v);
//		dv.seen = true;
//		
//	}
//	
//	void parent(Graph.Vertex u, Graph.Vertex v){
//		DAGVertex dv = getVertex(v);
//		dv.parent = u;
//	}
//	void discover(Graph.Vertex v){
//		DAGVertex dv = getVertex(v);
//		dv.dis = ++time;
//		
//	}
//	
//	void finish(Graph.Vertex v){
//		DAGVertex dv = getVertex(v);
//		dv.fin = ++time;
//	}
	
	boolean hasBackEdges(DFS d){
		
		List<Graph.Edge> lst = new ArrayList<Graph.Edge>();
		
//		for(Graph.Vertex u : g){
//			getVertex(u).seen = false;
//		}
		for(Graph.Vertex u : d.g){
			
			for(Graph.Edge e : u){
				Graph.Vertex v = e.otherEnd(u);
//				int vDis = d.getVertex(v).dis;
//				int uDis = d.getVertex(u).dis;
//				int uFin = d.getVertex(u).fin;
//				int vFin = d.getVertex(v).fin;
//				if(vDis <= uDis && uDis < uFin && uFin <= vFin) 
//					lst.add(e);
				if(d.getVertex(v).dis <= d.getVertex(u).dis && d.getVertex(u).dis < d.getVertex(u).fin
						&& d.getVertex(u).fin <= d.getVertex(v).fin){
					lst.add(e);
				}
			}
			//getVertex(u).seen = true;
			
		}
		
		return lst.size() != 0;
	}
	boolean isDAG(Graph g1) {
		
		DFS d = new DFS(g1, null);
		d.dfs();
		return hasBackEdges(d);
		
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
		DAG d = new DAG();
//		d.dfs();
		System.out.println(d.isDAG(graph));
		
		
	
		
	}

}
