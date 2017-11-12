
// Starter code for LP4
// Do not rename this file or move it away from cs6301/g1025

// change following line to your group number
package cs6301.g1025;

import java.util.List;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

import java.util.HashMap;

public class LP4 {
	Graph g;
	Vertex s;

	// common constructor for all parts of LP4: g is graph, s is source vertex
	public LP4(Graph g, Vertex s) {
		this.g = g;
		this.s = s;
	}

	// Part a. Return number of topological orders of g
	public long countTopologicalOrders() {
		TopologicalOrders a = new TopologicalOrders(g);
		return a.countToporders(g);

	}

	// Part b. Print all topological orders of g, one per line, and
	// return number of topological orders of g
	public long enumerateTopologicalOrders() {
		TopologicalOrders b = new TopologicalOrders(g);
		return b.enumerateTopOrders(g, true);
	}

	// Part c. Return the number of shortest paths from s to t
	// Return -1 if the graph has a negative or zero cycle
	public long countShortestPaths(Vertex t) {

		EnumerateSP c = new EnumerateSP();
		return c.Count(g, s, t);
	}

	// Part d. Print all shortest paths from s to t, one per line, and
	// return number of shortest paths from s to t.
	// Return -1 if the graph has a negative or zero cycle.
	public long enumerateShortestPaths(Vertex t) {
		// To do
		EnumerateSP d = new EnumerateSP();
		return d.Enumerate(g, s, t);

	}

	// Part e. Return weight of shortest path from s to t using at most k edges
	public int constrainedShortestPath(Vertex t, int k) {
		// To do
		CSP e = new CSP(g, k);
		return e.ShortestPathK(s, t, k, g);

	}

	// Part f. Reward collection problem
	// Reward for vertices is passed as a parameter in a hash map
	// tour is empty list passed as a parameter, for output tour
	// Return total reward for tour
	public int reward(HashMap<Vertex, Integer> vertexRewardMap, List<Vertex> tour) {
		// To do
		RewardCollection f=new RewardCollection(g,s,vertexRewardMap);
		return f.rewardProblem(tour);
		
	}

	// Do not modify this function
	static void printGraph(Graph g, HashMap<Vertex, Integer> map, Vertex s, Vertex t, int limit) {
		System.out.println("Input graph:");
		for (Vertex u : g) {
			if (map != null) {
				System.out.print(u + "($" + map.get(u) + ")\t: ");
			} else {
				System.out.print(u + "\t: ");
			}
			for (Edge e : u) {
				System.out.print(e + "[" + e.getWeight() + "] ");
			}
			System.out.println();
		}
		if (s != null) {
			System.out.println("Source: " + s);
		}
		if (t != null) {
			System.out.println("Target: " + t);
		}
		if (limit > 0) {
			System.out.println("Limit: " + limit + " edges");
		}
		System.out.println("___________________________________");
	}
}
