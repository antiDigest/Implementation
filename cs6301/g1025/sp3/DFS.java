package cs6301.g1025.sp3;


import cs6301.g00.Graph;


public class DFS extends GraphAlgorithm<DFS.DFSVertex>{
	
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
	
	static class DFSVertex {
		boolean seen;
		int start;
		int finish;
		
		DFSVertex(Graph.Vertex u) {
			seen = false;
			start = 0;
			finish = 0;
		}
	}

	boolean seen(Graph.Vertex u) {
		return getVertex(u).seen;
	}
	
	// Visit a node v from u
	void visit(Graph.Vertex u, Graph.Vertex v) {
		DFSVertex bv = getVertex(v);
		bv.seen = true;
	}

}
