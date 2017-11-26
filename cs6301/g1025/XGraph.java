package cs6301.g1025;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import cs6301.g00.ArrayIterator;

public class XGraph extends Graph {
	public static class XVertex extends Vertex {
       List<XEdge> xadj;
       int height;
       int excess;
       XVertex(Vertex u) {
			super(u);
			xadj = new LinkedList<>();
		}

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
				while (cur.flow >= cur.capacity && it.hasNext()) {
					cur = it.next();
				}
				if (cur.flow < cur.capacity) {
					ready = true;

				} else {
					ready = false;

				}
				return ready;
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
		int capacity;
		int flow;
		XEdge reverseEdge;

		XEdge(XVertex from, XVertex to, int flow, int capacity, int name) {
			super(from, to, 0, name);
			this.capacity = capacity;
			this.flow = flow;
			this.from = from;
			this.to = to;
		}
	}

	XVertex[] xv; // vertices of graph
	XEdge[] edges;

	public XGraph(Graph g, HashMap<Edge, Integer> capacity) {
		super(g);
		xv = new XVertex[g.size()]; // Extra space is allocated in array for
									// nodes to be added later
		for (Vertex u : g) {
			xv[u.getName()] = new XVertex(u);
			xv[u.getName()].height=0;
			xv[u.getName()].excess=0;
			
		}
		edges = new XEdge[g.m + 1];

		// Make copy of edges
		for (Vertex u : g) {
			for (Edge e : u) {
                Vertex v = e.otherEnd(u);
				XVertex x1 = getVertex(u);
				XVertex x2 = getVertex(v);
				XEdge xe1 = new XEdge(x1, x2, 0, capacity.get(e), e.getName());
				XEdge xe2 = new XEdge(x2, x1, 0, 0, -e.getName());
				xe1.reverseEdge = xe2;// adding the corresponding reverse edge
										// to these edges
				xe2.reverseEdge = xe1;// adding the corresponding reverse edge

				edges[e.getName()] = xe1;
				// to these edges
				x1.xadj.add(xe1);
				x2.xadj.add(xe2);

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
		boolean ready;

		XGraphIterator(XGraph xg) {
			this.it = new ArrayIterator<XVertex>(xg.xv, 0, xg.size() - 1);
		}

		public boolean hasNext() {
			if (ready) {
				return true;
			}
			if (!it.hasNext()) {
				return false;
			}
			xcur = it.next();
			ready = true;
			return ready;
		}

		public Vertex next() {
			if (!ready) {
				if (!hasNext()) {
					throw new java.util.NoSuchElementException();
				}
			}
			ready = false;
			return xcur;
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}

	}

	XEdge getEdge(Edge e) {
		return edges[e.getName()];
	}

	@Override
	public String toString() {
		for (Vertex u : this) {
			XVertex xu = this.getVertex(u);
			System.out.println("AjacencyList for Vertex " + u + " :"+ "and its excess  "+xu.excess+" height  "+xu.height);
			for (Edge e : xu.xadj) {
				XEdge xe = (XEdge) e;
				XVertex xv=this.getVertex(xe.otherEnd(xu));
				System.out.print(e + " flow " + xe.flow + " capacity " + xe.capacity+",");
			}
			System.out.println();
		}

		return "";
	}

	@Override
	public Vertex getVertex(int n) {
		return xv[n - 1];
	}

	XVertex getVertex(Vertex u) {
		return Vertex.getVertex(xv, u);
	}

	public static void main(String[] args) {

	}

}
