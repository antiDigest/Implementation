package cs6301.g1025.sp3;
import cs6301.g00.Graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SCC
		extends GraphAlgorithm<SCC.SCCVertex> {

	public SCC(Graph g) {
		super(g);
		node = new SCCVertex[g.size()];
		// Create array for storing vertex properties
		for (Graph.Vertex u : g) {
			node[u.getName()] = new SCCVertex(u);
		}
	}

	static class SCCVertex {
		boolean seen;
		int componentno;

		SCCVertex(Graph.Vertex u) {
			seen = false;
			componentno = 0;

		}
	}

	void assignComponentno(Graph.Vertex u, int componentno) {

		SCCVertex sc = getVertex(u);
		sc.componentno = componentno;
	}

	boolean seen(Graph.Vertex u) {
		return getVertex(u).seen;
	}

	// Visit a node v from u
	void visit(Graph.Vertex v) {
		SCCVertex sc = getVertex(v);
		sc.seen = true;

	}

	int dfsRev(List<Graph.Vertex> lst) {
		int components = 0;

		for (Graph.Vertex v : lst) {
			if (!seen(v)) {
				components = components + 1;
				dfsRevVisit(v, components);
			}
		}
		return components;

	}

	void dfsRevVisit(Graph.Vertex u, int componentno) {
		visit(u);
		assignComponentno(u, componentno);
		for (Graph.Edge e : u.getRevAdj()) {
			Graph.Vertex v = e.otherEnd(u);

			if (!seen(v)) {

				visit(v);
				dfsRevVisit(v, componentno);
			}
		}

		visit(u);
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
		SCC sc = new SCC(graph);
		System.out.println(sc.stronglyConnectedComponents(graph));

	}

	int stronglyConnectedComponents(Graph g) { 
		DFS d = new DFS(g, null);
		d.dfs();

		List<Graph.Vertex> lst1 = findOrderFinishTimes(d);

		return this.dfsRev(lst1);

	}

     List<Graph.Vertex> findOrderFinishTimes(DFS d) {
		List<Graph.Vertex> lst = new ArrayList<Graph.Vertex>();
        for(Graph.Vertex u :d.g){
        	lst.add(u);
        }
		Comparator<Graph.Vertex> cmp = new Comparator<Graph.Vertex>() {

			@Override
			public int compare(Graph.Vertex o1, Graph.Vertex o2) {

				if (d.getVertex(o1).fin < d.getVertex(o2).fin) {
					return 1;
				} else if (d.getVertex(o1).fin > d.getVertex(o2).fin) {
					return -1;
				} else {
					return 0;
				}

			}
		};
		Collections.sort(lst, cmp);
		return lst;

	}

}
