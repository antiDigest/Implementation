package cs6301.g1025;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cs6301.g00.Graph;

public class DAG {
	
	/**
	 * @param d DFS object after dfs is done.
	 *calculates back edges. can be reused to calculate the list of back edges in bridge - cut problem
	 *optimal: the loop can be stopped whenever we find a back edge. 
	 */
	boolean hasBackEdges(DFS d){
		
		List<Graph.Edge> lst = new ArrayList<Graph.Edge>();

		for(Graph.Vertex u : d.g){
			
			for(Graph.Edge e : u){
				Graph.Vertex v = e.otherEnd(u);
				//(v.discovery <= u.discovery < u.finish <= v.finish), then e is a back edge
				if(d.getVertex(v).dis <= d.getVertex(u).dis && d.getVertex(u).dis < d.getVertex(u).fin
						&& d.getVertex(u).fin <= d.getVertex(v).fin){
					lst.add(e);
					//return false;
				}
			}
		}
		//return true;
		return lst.size() != 0;
	}
	boolean isDAG(Graph g) {
		
		DFS d = new DFS(g, null);
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
		DAG dag = new DAG();
		System.out.println(dag.isDAG(graph));
		
	}

}
