package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XEdge;
import cs6301.g1025.XGraph.*;

import static java.lang.Math.abs;

import java.util.HashMap;
import java.util.Set;

public class Flow {

	HashMap<Edge, Integer> capacity;
	Graph g;
	Vertex s;
	Vertex t;

	public Flow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity) {
		this.g = new XGraph(g, capacity);
		this.s = s;
		this.t = t;
		this.capacity = capacity;
	}

	// Return max flow found by Dinitz's algorithm
	public int dinitzMaxFlow() {
		XGraph xg = (XGraph) g;
		Dinitz d = new Dinitz(xg, xg.getVertex(s), xg.getVertex(t));
		int flow = d.maxFlow();
		verify();
		return flow;

	}

	// Return max flow found by relabelToFront algorithm
	public int relabelToFront() {
		XGraph xg = (XGraph) g;
		RelabelToFront rf = new RelabelToFront(xg, xg.getVertex(s), xg.getVertex(t));
		return rf.relabelToFront();
		
	}

	// flow going through edge e
	public int flow(Edge e) {
		XGraph xg = (XGraph) g;
		return xg.getEdge(e).flow;

	}

	// capacity of edge e
	public int capacity(Edge e) {
		XGraph xg = (XGraph) g;
		return xg.getEdge(e).capacity;
	}

	boolean verify() {
		int outFlow = 0;
		XGraph xg = (XGraph) g;
		XVertex xsource = xg.getVertex(s);
		for (Edge e : xsource.adj) {
			
			outFlow += flow(e);
		}

		int inFlow = 0;

		XVertex xsink = xg.getVertex(t);
		for (Edge e : xsink.revAdj) {
			XVertex op = xg.getVertex(e.otherEnd(xsink));
			for (Edge xe1 : op.adj) {
				if (xe1.otherEnd(op).equals(xsink)) {
					inFlow += flow(xe1);
				}
			}
		}

		if (inFlow != outFlow) {
			System.out.println(
					"Invalid: total flow from the source (" + outFlow + ") != total flow to the sink (" + inFlow + ")");
			return false;
		}

		for (Vertex u : g) {
			if (!u.equals(s) && !u.equals(t)) {
				XVertex xu = xg.getVertex(u);
				int outVertexFlow = 0;
				for (Edge e : xu.adj) {
					outVertexFlow += flow(e);
				}

				int inVertexFlow = 0;

				for (Edge e : xu.revAdj) {
					XVertex op = xg.getVertex(e.otherEnd(xu));
					for (Edge xe1 : op.adj) {
						if (xe1.otherEnd(op).equals(xu)) {
							inVertexFlow += flow(xe1);
						}
					}

				}

				if (inVertexFlow != outVertexFlow) {
					System.out.println("Invalid: Total incoming flow != Total outgoing flow at " + u);
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * After maxflow has been computed, this method can be called to get the
	 * "S"-side of the min-cut found by the algorithm
	 */
	public Set<Vertex> minCutS() {
		
		return null;
	}

	/*
	 * After maxflow has been computed, this method can be called to get the
	 * "T"-side of the min-cut found by the algorithm
	 */
	public Set<Vertex> minCutT() {
		return null;
	}
}
