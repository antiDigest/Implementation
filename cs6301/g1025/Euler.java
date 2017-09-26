/**  Find Euler Tours in Graph if it is Eulerian
 *   @author antriksh, swaroop, saikumar, gunjan
 *  Ver 1.0: 2017/09/24
 */

// change following line to your group number
package cs6301.g1025;

import java.util.List;

import cs6301.g00.Eulerian;
import cs6301.g00.Graph;
import cs6301.g00.Graph.Edge;
import cs6301.g00.Graph.Vertex;


import java.util.ArrayList;
import java.util.Iterator;

public class Euler {
	int VERBOSE;
	List<Graph.Edge> tour;
	Graph.Vertex start;
	Graph g;

	// Constructor
	Euler(Graph g, Graph.Vertex start) {
		VERBOSE = 1;
		this.g = g;
		this.start = start;
		this.tour = new ArrayList<Graph.Edge>();
		eulerVertex = new EulerVertex[g.size()];
		for (Graph.Vertex u : g) {
			eulerVertex[u.getName()] = new EulerVertex(u);

		}

	}

	// Algorithm uses a parallel array for storing information about vertices
	static EulerVertex[] eulerVertex;

	// Class to store information about a vertex in this algorithm
	class EulerVertex {
		Graph.Vertex element;
		Iterator<Graph.Edge> it;
		List<Graph.Edge> subTour;

		EulerVertex(Graph.Vertex u) {
			element = u;
			this.subTour = new ArrayList<Graph.Edge>();
			this.it = u.getAdj().iterator();
		}
	}

	// To do: function to find an Euler tour
	public List<Graph.Edge> findEulerTour() {

		findTours();
		if (VERBOSE > 9) {
			printTours();
		}
		stitchTours();
		return tour;
	}

	/*
	 * To do: test if the graph is Eulerian. If the graph is not Eulerian, it
	 * prints the message: "Graph is not Eulerian" and one reason why, such as
	 * "inDegree = 5, outDegree = 3 at Vertex 37" or
	 * "Graph is not strongly connected"
	 */
	boolean isEulerian() {
		Eulerian e = new Eulerian();
		return e.testEulerian(g);

	}

	// Find tours starting at vertices with unexplored edges
	void findTours() {

		findTour(this.start, getEulerVertex(start).subTour);
		for (Graph.Vertex u : g) {
			while (getEulerVertex(u).it.hasNext()) {

				findTour(u, getEulerVertex(u).subTour);

			}

		}
	}

	static void findTour(Graph.Vertex start, List<Graph.Edge> tour) {

		Graph.Vertex u = start;
		while (getEulerVertex(u).it.hasNext()) {
			Graph.Edge e = getEulerVertex(u).it.next();
			tour.add(e);
			Vertex v = e.otherEnd(u);
			u = v;
		}

	}

	static EulerVertex getEulerVertex(Graph.Vertex u) {
		return eulerVertex[u.getName()];
	}

	/*
	 * Print tours found by findTours() using following format: Start vertex of
	 * tour: list of edges with no separators Example: lp2-in1.txt, with start
	 * vertex 3, following tours may be found. 3:
	 * (3,1)(1,2)(2,3)(3,4)(4,5)(5,6)(6,3) 4: (4,7)(7,8)(8,4) 5: (5,7)(7,9)(9,5)
	 *
	 * Just use System.out.print(u) and System.out.print(e)
	 */
	void printTours() {

		for (Graph.Vertex v : g) {
			if (getEulerVertex(v).subTour.size() > 0) {
				System.out.println(v + ":");
				for (Edge e : getEulerVertex(v).subTour) {
					System.out.println("(" + v.getName() + "," + e.otherEnd(v).getName() + ")");
				}
			}
		}
	}

	// Stitch tours into a single tour using the algorithm discussed in class
	void stitchTours() {
		explore(this.start);
	}

	void explore(Graph.Vertex u) {
		Graph.Vertex temp = u;
		for (Edge e : getEulerVertex(temp).subTour) {
			this.tour.add(e);
			temp = e.otherEnd(temp);
			if (temp != u && getEulerVertex(temp).subTour.size() > 0) {
				explore(temp);
			}

		}
	}

	void setVerbose(int v) {
		VERBOSE = v;
	}
}
