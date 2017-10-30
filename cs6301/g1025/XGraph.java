/** @author rbk
 *  Ver 1.0: 2017/09/29
 *  Example to extend Graph/Vertex/Edge classes to implement algorithms in which nodes and edges
 *  need to be disabled during execution.  Design goal: be able to call other graph algorithms
 *  without changing their codes to account for disabled elements.
 *
 *  Ver 1.1: 2017/10/09
 *  Updated iterator with boolean field ready. Previously, if hasNext() is called multiple
 *  times, then cursor keeps moving forward, even though the elements were not accessed
 *  by next().  Also, if program calls next() multiple times, without calling hasNext()
 *  in between, same element is returned.  Added UnsupportedOperationException to remove.
 **/

package cs6301.g1025;

import static cs6301.g1025.LP3.INFINITY;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class XGraph extends Graph {

	int graphSize;
	int originalSize;

	public int size() {
		return graphSize;
	}

	void incrementSize() {
		graphSize++;
	}

	public static class XVertex extends Vertex {
		boolean disabled;
		List<XEdge> xadj;
		List<XEdge> xrevadj;

		List<Vertex> collab;
		int name;
		int size;
		

		XVertex(Vertex u) {
			super(u);
			disabled = false;
			xadj = new LinkedList<>();
			xrevadj = new LinkedList<>();
		}

		List<XEdge> getXRevAdj() {
			return this.xrevadj;
		}

		List<XEdge> getXAdj() {
			return this.xadj;
		}

		XVertex(int n, List<Vertex> component) {
			super(n);
			name = n;
			disabled = false;
			xadj = new LinkedList<>();
			xrevadj = new LinkedList<>();
			collab = new ArrayList<>();
			size = component.size();
			collab.addAll(component);

		}

		boolean isDisabled() {
			return disabled;
		}

		void disable() {
			disabled = true;
		}

		void enable() {
			disabled = false;
		}
		// int size() {
		// return size;
		// }

		@Override
		public Iterator<Edge> iterator() {
			return new XVertexIterator(this);
		}

		class XVertexIterator implements Iterator<Edge> {
			XEdge cur;
			Iterator<XEdge> it;
			boolean ready;

			XVertexIterator(XVertex u) {
				this.it = u.xadj.iterator();
				ready = false;
			}

			public boolean hasNext() {
				if (ready) {
					return true;
				}
				if (!it.hasNext()) {
					return false;
				}
				cur = it.next();
				while (cur.isDisabled() && it.hasNext()) {
					cur = it.next();
				}
				ready = true;
				return !cur.isDisabled();
			}

			public Edge next() {
				if (!ready) {
					if (!hasNext()) {
						throw new java.util.NoSuchElementException();
					}
				}
				ready = false;
				return cur;
			}

			public void remove() {
				throw new java.lang.UnsupportedOperationException();
			}
		}
	}

	static class XEdge extends Edge {
		boolean disabled;
        XEdge minEdge;
        public int getModifyWeight() {
			return modifyWeight;
		}
		public void setModifyWeight(int modifyWeight) {
			this.modifyWeight = modifyWeight;
		}

		int modifyWeight;
      
		XEdge(XVertex from, XVertex to, int weight) {
			super(from, to, weight);
			disabled = false;
			minEdge=null;
			modifyWeight=weight;
		}
		XEdge(XVertex from, XVertex to, int weight,int modifyWeight){
			super(from, to, weight);
			disabled = false;
			minEdge=null;
			this.modifyWeight=modifyWeight;
		}
		

		
		
		

		boolean isDisabled() {
			XVertex xfrom = (XVertex) from;
			XVertex xto = (XVertex) to;
			return disabled || xfrom.isDisabled() || xto.isDisabled();
		}

		void disable() {
			disabled = true;
		}

		void enable() {
			disabled = false;
		}

		@Override
		public String toString() {
			 return "(" + from + "," + to + ")";
		}
	}

	XVertex[] xv; // vertices of graph

	public XGraph(Graph g) {
		super(g);
		originalSize= g.size();
		graphSize = g.size();
		xv = new XVertex[2 * g.size()]; // Extra space is allocated in array for
										// nodes to be added later
		for (Vertex u : g) {
			xv[u.getName()] = new XVertex(u);
		}

		// Make copy of edges
		for (Vertex u : g) {
			for (Edge e : u) {
				Vertex v = e.otherEnd(u);
				XVertex x1 = getVertex(u);
				XVertex x2 = getVertex(v);
				XEdge edge = new XEdge(x1, x2, e.getWeight());
				x1.xadj.add(edge);
				x2.xrevadj.add(edge);
			}
		}
	}

	@Override
	public Iterator<Vertex> iterator() {
		return new XGraphIterator(this);
	}

	class XGraphIterator implements Iterator<Vertex> {
		Iterator<XVertex> it;
		XVertex xcur;

		XGraphIterator(XGraph xg) {
			this.it = new ArrayIterator<XVertex>(xg.xv, 0, xg.size() - 1); // Iterate
																			// over
																			// existing
																			// elements
																			// only
		}

		public boolean hasNext() {
			if (!it.hasNext()) {
				return false;
			}
			xcur = it.next();
			while (xcur.isDisabled() && it.hasNext()) {
				xcur = it.next();
			}
			return !xcur.isDisabled();
		}

		public Vertex next() {
			return xcur;
		}

		public void remove() {
		}

	}

	/**
	 * Method to reverse the edges of a graph. Applicable to directed graphs
	 * only.
	 */
	public void reverseGraph() {
		if (directed) {
			for (Graph.Vertex u : this) {
				List<XGraph.XEdge> tmp = getVertex(u).getXAdj();
				getVertex(u).xadj = getVertex(u).getXRevAdj();

				getVertex(u).xrevadj = tmp;
				// for(Edge e:getVertex(u).xadj){
				// e.from
				// }

			}
		}
	}

	@Override
	public Vertex getVertex(int n) {
		return xv[n - 1];
	}

	XVertex getVertex(Vertex u) {
		return Vertex.getVertex(xv, u);
	}

	void disable(int i) {
		XVertex u = (XVertex) getVertex(i);
		u.disable();
	}

	@Override
	public String toString() {
		for (Vertex u : this) {
			System.out.println("AjacencyList for Vertex " + u + " :");
			for (Edge e : u) {
				System.out.print(e);

			}
			System.out.println();
		}
		return "";

	}

	void printGraph(BFS b) {
		for (Vertex u : this) {
			System.out.print("  " + u + "  :   " + b.distance(u) + "  : ");
			for (Edge e : u) {
				System.out.print(e);
			}
			System.out.println();
		}

	}

	// public void addVertex(Component[] compList,Vertex start) {
	//
	//
	// for (Component c: compList) {
	// List<Vertex> cycle=c.getList();
	// int minWeight = INFINITY;
	// XEdge minEdge = null;
	// for (Vertex v : cycle) {
	// XEdge e = getEdge(getVertex(start), getVertex(v));
	// if (e != null && e.getWeight() > 0 && minWeight > e.getWeight()) {
	// minWeight = e.getWeight();
	// minEdge=e;
	// }
	// }
	// if(minEdge!=null){
	// minEdge = new XEdge(getVertex(start), , e.getWeight());
	// incrementSize();
	// XGraph.XVertex vertex = new XGraph.XVertex(size() - 1, cycle);
	// this.xv[size() - 1] = vertex;
	//
	// disableAll(cycle);
	// }
	//
	// }
	//
	//
	//
	//
	// for(Vertex u:c.getList()){
	// if(getVertex(u).isDisabled()){
	// break;
	// }
	//
	// if (minEdge != null) {
	// vertex.xrevadj.add(minEdge);
	// getVertex(u).xadj.add(minEdge);
	// }
	// }
	// }
	// incrementSize();
	// XGraph.XVertex vertex = new XGraph.XVertex(size() - 1, cycle);
	// this.xv[size() - 1] = vertex;
	// disableAll(cycle);
	//
	// for (Vertex u : this) {
	// int minWeight = INFINITY;
	// XEdge minEdge = null;
	// for (Vertex v : vertex.collab) {
	// XEdge e = getRevEdge(getVertex(u), getVertex(v));
	// if (e != null && e.getWeight() > 0 && minWeight > e.getWeight()) {
	// minWeight = e.getWeight();
	// minEdge = new XEdge(vertex, getVertex(u), e.getWeight());
	// }
	// }
	// if (minEdge != null) {
	// vertex.xadj.add(minEdge);
	// getVertex(u).xrevadj.add(minEdge);
	// }
	// }
	// }

	public void unRavel(Vertex u) {
		XVertex k = getVertex(u);
		if (k.size <= 1) {
			return;
		}
		for (Vertex v : k.collab) {
			System.out.println(v);
		}
	}

	void disableCycle(List<Vertex> cycle) {
		for (Vertex v : cycle) {
			XGraph.XVertex k = this.getVertex(v);
//			for(Edge e:v){
//				XGraph.XEdge xe=(XEdge) e;
//				if(xe.getModifyWeight()!=0){
//					
//				}
//			}
			k.disable();
		}
	}

	void EnableCycle(List<Vertex> cycle) {
		for (Vertex v : cycle) {
			XGraph.XVertex k = this.getVertex(v);
			k.enable();
		}
	}

	


	

	XEdge getEdge(XVertex u, XVertex v) {
		for (XEdge e : u.getXAdj()) {
			XVertex x = getVertex(e.otherEnd(u));
			if (x == v) {
				return e;
			}
		}
		return null;
	}

	XEdge getRevEdge(XVertex u, XVertex v) {
		for (XEdge e : u.getXRevAdj()) {
			XVertex x = getVertex(e.otherEnd(u));
			if (x == v) {
				return e;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		Graph g = Graph.readGraph(new Scanner(System.in), true);
		System.out.println(g);
		XGraph xg = new XGraph(g);
		Vertex src = xg.getVertex(1);
		System.out.println(xg);

	}

}

/*
 * Sample output:
 * 
 * Node : Dist : Edges 1 : 0 : (1,2)(1,3) 2 : 1 : (2,1)(2,4)(2,5) 3 : 1 :
 * (3,1)(3,6)(3,7) 4 : 2 : (4,2)(4,8) 5 : 2 : (5,2) 6 : 2 : (6,3) 7 : 2 :
 * (7,3)(7,9) 8 : 3 : (8,4) 9 : 3 : (9,7) Source: 1 Farthest: 8 Distance: 3
 * 
 * Disabling vertices 8 and 9 1 : 0 : (1,2)(1,3) 2 : 1 : (2,1)(2,4)(2,5) 3 : 1 :
 * (3,1)(3,6)(3,7) 4 : 2 : (4,2) 5 : 2 : (5,2) 6 : 2 : (6,3) 7 : 2 : (7,3)
 * Source: 1 Farthest: 4 Distance: 2
 * 
 */
