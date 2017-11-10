package cs6301.g1025;


import java.util.ArrayList;

import java.util.List;


import cs6301.g00.Graph;
import cs6301.g00.Graph.Edge;
import cs6301.g00.Graph.Vertex;
import cs6301.g00.GraphAlgorithm;

public class TopologicalOrders extends GraphAlgorithm<TopologicalOrders.TOPVertex> {

	public TopologicalOrders(Graph g) {
		super(g);
		node = new TOPVertex[g.size()];
		for (Graph.Vertex u : g) {
			node[u.getName()] = new TOPVertex(u);

		}
	}

	public TOPVertex getTopVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}

	static class TOPVertex {
		int inDegree;
		boolean seen;

		public boolean isSeen() {
			return seen;
		}

		public void setSeen(boolean seen) {
			this.seen = seen;
		}

		public int getInDegree() {
			return inDegree;
		}

		public void setInDegree(int inDegree) {
			this.inDegree = inDegree;
		}

		TOPVertex(Graph.Vertex u) {
			this.inDegree = u.revAdj.size();
			this.seen = false;
		}
	}

	long enumerateTopOrders(Graph g,boolean toPrint) {
		List<Graph.Vertex> topList = new ArrayList<Graph.Vertex>();
		return Enumerate(g, topList,toPrint);
	}

	public long countToporders(Graph g) {
		return enumerateTopOrders(g,false);
	}

	long Enumerate(Graph g, List<Graph.Vertex> topList,boolean toPrint) {
		long count = 0;
		if (topList.size() == g.size()) {
			count = count + 1;
			if(toPrint)
			printResult(topList);
		}
        for (Vertex u : g) {
			if (!getTopVertex(u).isSeen() && getTopVertex(u).getInDegree() == 0) {
				topList.add(u);
				getTopVertex(u).setSeen(true);
				changeIndegree(u, -1);
				count = count + Enumerate(g, topList,toPrint);
				changeIndegree(u, +1);
				getTopVertex(u).setSeen(false);
				topList.remove(topList.size() - 1);
			}
		}
		return count;

	}

	void printResult(List<Graph.Vertex> topList) {
	 
		for (Vertex v : topList) {
            if(v.equals(topList.get(topList.size()-1))){            		
			System.out.print(v.getName()+1);}
            else{
            	System.out.print(v.getName()+1+",");
            }
		}
		System.out.println();
	}

	void changeIndegree(Vertex u, int one) {
		for (Edge e : u) {
			Vertex v = e.otherEnd(u);
			getTopVertex(v).setInDegree(getTopVertex(v).getInDegree() + one);
		}
	}
}