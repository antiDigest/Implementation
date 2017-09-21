package shortProject1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import shortProject1.Graph.Edge;
import shortProject1.Graph.Vertex;

/* Logic - Run BFS, starting at an arbitrary node as root.  Let u be a node
at maximum distance from the root.  Run BFS again, with u as the root.
Output diameter: path from u to a node at maximum distance from u.*/


public class DiameterOfTree {

	static LinkedList<Graph.Vertex> diameter(Graph g) {

		if (g == null || g.size() == 0) {
			return new LinkedList<Graph.Vertex>();
		}

		// Start with a random node
		Random random = new Random();
		int index = random.nextInt(g.size());
		Vertex root = g.getVertex(index);

		
		Vertex source = Bfs(root);

		
		Vertex[] v = g.v;
		for (int i = 0; i < v.length; i++) {
			Vertex p = v[i];
			p.isSeen = false;
			p.parent = null;
		}

		// Here destination is the last/end node visited by BFS
		Vertex destination = Bfs(source);
		LinkedList<Graph.Vertex> result = findPath(destination);
		return result;

	}

	static Vertex Bfs(Vertex root) {

		if (root == null) {
			return null;
		}

		Queue<Graph.Vertex> frontier = new LinkedList<Graph.Vertex>();

		frontier.add(root);
		root.parent = null;
		Vertex lastNode = root;
		// Normal BFS logic
		while (frontier.size() > 0) {
			Vertex node = frontier.poll();

			if (node.isSeen == false) {
				lastNode = node;
				node.isSeen = true;
				for (int i = 0; i < node.adj.size(); i++) {
					Edge e = node.adj.get(i);
					Vertex child = e.otherEnd(node);
					if (child.isSeen == false) {
						child.parent = node;
						frontier.add(child);
					}

				}
			}
		}
		return lastNode;

	}

	static LinkedList<Graph.Vertex> findPath(Vertex n) {

		if (n == null) {
			return new LinkedList<Graph.Vertex>();
		}
        
		LinkedList<Graph.Vertex> result = new LinkedList<Graph.Vertex>();
		result.add(n);
		// iterative-finding the parents till we reach null.
		while (n.parent != null) {
			result.add(n.parent);
			n = n.parent;
		}

		return result;

	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		Graph g = Graph.readGraph(in);

		LinkedList<Graph.Vertex> result = diameter(g);
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i));
		}

	}
}