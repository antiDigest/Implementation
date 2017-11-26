package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XEdge;
import cs6301.g1025.XGraph.XVertex;
import java.util.*;
import static java.lang.Math.abs;

public class RelabelToFront {
	public static final int INFINITY = Integer.MAX_VALUE;
	XGraph xg;
	Vertex source;
	Vertex sink;

	RelabelToFront(XGraph g, Vertex source, Vertex sink) {
		this.xg = g;
		this.source = source;
		this.sink = sink;

	}

	/**
	 * Initializing Max-Flow Relabel to Front
	 */
	void initialize() {
		XVertex xsource = xg.getVertex(source);
		xsource.height = xg.size();
		for (Edge e : xsource) {
			XVertex xu = xg.getVertex(e.otherEnd(xsource));
			XEdge xe = (XEdge) e;
			xe.flow = xe.capacity;
			xe.reverseEdge.flow=xe.reverseEdge.flow-xe.flow;
			xsource.excess = xsource.excess - xe.capacity;
			xu.excess = xu.excess + xe.capacity;
		}
	}

	/**
	 * Push flow out of u using e edge (u, v) = e: is in Residual Graph (Gf)
	 * 
	 * @param u
	 *            From Vertex
	 * @param v
	 *            To Vertex
	 * @param e
	 *            Edge: (u, v)
	 */
	void push(Vertex u, Vertex v, Edge e) {
		XEdge xe = (XEdge) e;
		XVertex xu = (XVertex) u;
		XVertex xv = (XVertex) v;

		int delta = Math.min(xu.excess, xe.capacity - xe.flow);
		if (xe.fromVertex().equals(xu)) {
			xe.flow = xe.flow + delta;
			xe.reverseEdge.flow = xe.reverseEdge.flow - delta;
		} else {
			xe.flow = xe.flow - delta;
			xe.reverseEdge.flow = xe.reverseEdge.flow + delta;
		}

		xu.excess -= delta;
		xv.excess += delta;
	}

	/**
	 * Edge out of u in Residual Graph (Gf) because of e ?
	 * 
	 * @param u
	 *            From vertex
	 * @param e
	 *            Edge (u, ?)
	 * @return true if in residual graph, else false
	 */
	static boolean inResidualGraph(Vertex u, Edge e) {
		XEdge xe = (XEdge) e;
		XVertex xu = (XVertex) u;
		return xe.fromVertex() == xu ? xe.flow < xe.capacity : xe.flow > 0;
	}

	/**
	 * increase the height of u, to allow u to get rid of its excess
	 * Precondition: u.excess > 0, and for all ( u, v ) in Gf, u.height <=
	 * v.height
	 * 
	 * @param u
	 *            Vertex
	 */
	void relabel(Vertex u) {
		XVertex xu = (XVertex) u;

		int minHeight = INFINITY;
		for (Edge e : xu) {
			XVertex xv = (XVertex) e.otherEnd(xu);
			if (xv.height < minHeight) {
				minHeight = xv.height;
				xu.height = 1 + minHeight;
			}
		}

		
	}

	/**
	 * push all excess flow out of u, raising its height, as needed
	 * 
	 * @param u
	 *            Vertex
	 */
	void discharge(Vertex u) {
		XVertex xu = (XVertex) u;
		while (xu.excess > 0) {
			for (Edge e : u) {
				XEdge xe = (XEdge) e;
				XVertex xv = xg.getVertex(e.otherEnd(xu));
				if (inResidualGraph(xu, xe) && xu.height == 1 + xv.height) {
					push(xu, xv, xe);
					if (xu.excess == 0)
						return;
				}
			}

			relabel(xu);
		}
	}

	/**
	 * Algorithm to find max flow !
	 */
	int relabelToFront() {
		initialize();
		List<Vertex> L = new LinkedList<>();
		for (Vertex u : xg) {
           if (!xg.getVertex(u).equals(source) && !xg.getVertex(u).equals(sink)) {
				L.add(u);
			}
		}

		boolean done = false;

		while (!done) {
            Iterator<Vertex> it = L.iterator();
			done = true;
			XVertex xu = null;
			while (it.hasNext()) {
				xu = (XVertex) it.next();
				if (xu.excess == 0) {
					continue;
				}
				int oldHeight = xu.height;
				discharge(xu);

				if (xu.height != oldHeight) {
					done = false;
					break;
				}
			}

			if (!done) {
				it.remove();
				L.add(0, xu);
			}
		}
		return xg.getVertex(sink).excess ;
	}

	

}
