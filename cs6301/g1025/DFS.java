/**
 * Class to represent a graph
 *  @author swaroop, saikumar, antriksh
 *
 */

package cs6301.g1025;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


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

	/**
	 * Wrapper class for Graph.Vertex
	 */
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

	/**
	 * DFS Visit Procedure
	 * @param u: start vertex
	 */
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

	/**
	 * get u's parent
	 * @param u: vertex
	 * @return vertex
	 */
	Graph.Vertex getParent(Graph.Vertex u) {
		return getVertex(u).parent;
	}

	/**
	 * u seen or not
	 * @param u: vertex
	 * @return boolean
	 */
	boolean seen(Graph.Vertex u) {
		return getVertex(u).seen;
	}

	/**
	 * Visit node v from u
	 * @param v: vertex
	 */
	void visit(Graph.Vertex v) {
		DFSVertex dv = getVertex(v);
		dv.seen = true;
		
	}

	/**
	 * set parent u of v
	 * @param u: Vertex
	 * @param v: Vertex
	 */
	void parent(Graph.Vertex u, Graph.Vertex v){
		DFSVertex dv = getVertex(v);
		dv.parent = u;
	}

	/**
	 * Set discovered
	 * @param v
	 */
	void discover(Graph.Vertex v){
		DFSVertex dv = getVertex(v);
		dv.dis = ++time;
		
	}

	/**
	 * set finish time
	 * @param v
	 */
	void finish(Graph.Vertex v){
		DFSVertex dv = getVertex(v);
		dv.fin = ++time;
	}

	/**
	 * Get finish time
	 * @param v
	 * @return integer
	 */
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
