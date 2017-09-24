/**
 * Class to represent a graph
 *  @author swaroop, saikumar
 *
 */

package cs6301.g00;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class Eulerian {

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
		Eulerian e = new Eulerian();
		System.out.println(e.testEulerian(graph));

	}

	public boolean testEulerian(Graph g) {

		for (Graph.Vertex u : g) {
			if (u.getAdj().size() != u.getRevAdj().size()) {
				System.out.println("Graph is not Eulerian");
				System.out.println("Reason: Indegree = " + u.getRevAdj() + " and Outdegree = " + u.getAdj().size()
						+ " at vertex " + u.getName());
				return false;
			}
		}
		SCC sc = new SCC(g);
		int components = sc.stronglyConnectedComponents(g);

		if (components > 1) {
			System.out.println("Graph is not Eulerian");
			System.out.println("Reason: Graph is not strongly connected");
			return false;
		} else
			return true;
	}
}