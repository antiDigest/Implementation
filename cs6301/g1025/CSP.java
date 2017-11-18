package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.TopologicalOrders.TOPVertex;

public class CSP extends GraphAlgorithm<CSP.CSPVertex> {

	
	/**
	 * Constructor for CSP  
	 * @parameters Graphg and and the constraint k
	 */
	public CSP(Graph g, int k) {
		super(g);
		node = new CSPVertex[g.size()];
		for (Graph.Vertex u : g) {
			node[u.getName()] = new CSPVertex(u, k);
		}
	}
	
	public CSPVertex getCSPVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}

	/**
     * class CSPVertex
     *
     * @param parent : Vertex - parent
     * @param distance : distance-Shortest distance
     * @param d : Integer array -d 
     * 
     */
	static class CSPVertex {
		Vertex parent;
		Integer d[];
		Integer distance;

		CSPVertex(Vertex u, int k) {
			d = new Integer[k + 1];
			for (int i = 0; i <= k; i++)
				d[i] = null;
			parent = null;
			distance = null;
		}
	}

	/**
     * @param s : Vertex - Source s
     * @param t : Vertex - Source t
     * @param g :  Graph g
     * Intger.Max_Value is like infinity if it is not possible then returns Integer.MaxValue
     * returns distance
     */
	Integer ShortestPathK(Vertex s, Vertex t, int k, Graph g) {
		boolean result = CSPAtmostK(s, g);
		if (getCSPVertex(t).distance == null) {
			System.out.println("cannot be reached with given constraints");
			return Integer.MAX_VALUE;
		} else if (!result) {
			System.out.println("Graph has a negative cycle");
			return -1;
		}
		return getCSPVertex(t).distance;

	}

	/**
     * @param s : Vertex - Source s
     * @param g : Graph g
     * returns boolean -whether graph has a negative cycle or it satisfies the constraints.
     * 
     */
	boolean CSPAtmostK(Vertex s, Graph g) {
		getCSPVertex(s).d[0] = 0;
		boolean noChange = false; 
		for (int k = 1; k < getCSPVertex(s).d.length; k++) {
			noChange = true;
			for (Vertex u : g) {
				getCSPVertex(u).d[k] = getCSPVertex(u).d[k - 1];
				for (Edge e : u.revAdj) {
					Vertex p = e.fromVertex();

					if ((getCSPVertex(u).d[k] != null && getCSPVertex(p).d[k - 1] != null
							&& getCSPVertex(u).d[k] > getCSPVertex(p).d[k - 1] + e.getWeight())
							|| (getCSPVertex(p).d[k - 1] != null && getCSPVertex(u).d[k] == null)) {
						getCSPVertex(u).d[k] = getCSPVertex(p).d[k - 1] + e.getWeight();
						getCSPVertex(u).parent = p;
						noChange = false;

					}

				}

			}

			if (noChange || k == getCSPVertex(s).d.length - 1) {
				for (Vertex u : g) {
					getCSPVertex(u).distance = getCSPVertex(u).d[k];
				}
				return true;
			}

		}

		return false;

	}
}
