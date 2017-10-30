/**
 * Long Project 3
 * Implementing Tarjan's algorithm for finding MST in directed graphs
 * @author Antriksh, Saikumar, Swaroop, Gunjan
 * Version 1.0 10/28/2017: Implemented
 */
// Starter code for LP3
// Do not rename this file or move it away from cs6301/g??

// change following line to your group number

package cs6301.g1025;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cs6301.g00.Timer;
import cs6301.g1025.BFS.BFSVertex;
import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;
import cs6301.g1025.XGraph.XEdge;

public class LP3 {
	public static final int INFINITY = Integer.MAX_VALUE;
	static int VERBOSE = 1;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		if (args.length > 1) {
			VERBOSE = Integer.parseInt(args[1]);
		}

		int start = in.nextInt(); // root node of the MST
		Graph g = Graph.readDirectedGraph(in);
		Vertex startVertex = g.getVertex(start);
		List<Edge> dmst = new ArrayList<>();

		Timer timer = new Timer();
		int wmst = directedMST(g, startVertex, dmst);
		timer.end();

		System.out.println(wmst);
		if (VERBOSE > 0) {
			System.out.println("_________________________");
			for (Edge e : dmst) {
				System.out.print(e);
			}
			System.out.println();
			System.out.println("_________________________");
		}
		System.out.println(timer);
	}

	/**
	 * TO DO: List dmst is an empty list. When your algorithm finishes, it
	 * should have the edges of the directed MST of g rooted at the start
	 * vertex. Edges must be ordered based on the vertex into which it goes,
	 * e.g., {(7,1),(7,2),null,(2,4),(3,5),(5,6),(3,7)}. In this example, 3 is
	 * the start vertex and has no incoming edges. So, the list has a null
	 * corresponding to Vertex 3. The function should return the total weight of
	 * the MST it found.
	 */

	static void checkGraph(XGraph xg) {

		for (int i = 0; i < xg.xv.length; i++) {
			Vertex v = xg.xv[i];
			if (v == null) {
				break;
			}
			for (Edge e : xg.getVertex(v).getXAdj()) {
				XEdge e1 = (XEdge) e;
			
				//System.out.println(e1+" "+e1.disabled);
			}

		}

	}

	public static int directedMST(Graph g, Vertex start, List<Edge> dmst) {

		XGraph xg = new XGraph(g);
       int mst = -1;
		while (mst == -1) {
			//System.out.println(xg);
			ArrayList<Edge> disabledEdges = zeroGraph(xg,start);
			//System.out.println(xg);
			
			mst = doBFS(xg, xg.getVertex(start), dmst);
			//System.out.println(xg);
			
			if(mst!=-1){
				//System.out.println("found");
			}
			if (mst == -1) {
				IterativeTarjan(xg, disabledEdges, start);
				//checkGraph(xg);
			}
		}
		mst = 0;
	    Edge[] out=new Edge[xg.originalSize];
		for (Vertex u : xg) {
			// System.out.println("AjacencyList for Vertex " + u + " :");
			for (Edge e : u) {
				out[e.to.getName()]=e;
				//dmst.add(e.to.getName(),e);
			//System.out.println(e+""+e.getWeight());
				mst = mst + ((XEdge) e).getWeight();

			}
			
		}
	    Collections.addAll(dmst, out);
		return mst;
	}

	/**
	 * enable disbaled edges
	 */

	static void enableEdges(ArrayList<Edge> disabledEdges) {
		for (Edge e : disabledEdges) {
			((XGraph.XEdge) e).enable();
		}

	}

	static class Component {
		ArrayList<Vertex> v;
		Vertex xv;

		public Vertex getXv() {
			return xv;
		}

		public void setXv(Vertex xv) {
			this.xv = xv;
		}

		Component() {
			v = new ArrayList<Vertex>();

			xv = null;
		}

		ArrayList<Vertex> getList() {
			return v;
		}

		@Override
		public String toString() {
			for (Vertex xv : v) {
				//System.out.print(xv + ", ");
			}
			// System.out.println();
			return "";

		}
	}

	static void IterativeTarjan(XGraph xg, ArrayList<Edge> disabledEdges, Vertex start) {
		SCC sc = new SCC(xg);
		int no = sc.stronglyConnectedComponents(xg);
		Component[] compList = new Component[sc.components];

		enableEdges(disabledEdges);
	

		for (Vertex v : xg) {
			if (compList[sc.getComponentno(v)] == null) {
				compList[sc.getComponentno(v)] = new Component();
			}
			compList[sc.getComponentno(v)].getList().add(v);
		}

		HashMap<String,Edge> minEdgeMap=new HashMap<String,Edge>();
		//Edge minEdges[][] = new Edge[compList.length][compList.length];
		for (int i = 0; i < compList.length; i++) {
			for (int j = 0; j < compList.length; j++) {
				if (j != i) {
					Integer minWeight = INFINITY;
					Edge minEdge = null;
					for (Vertex u : compList[j].getList()) {
						XVertex xv = xg.getVertex(u);
						if (xv.isDisabled())
							continue;
						for (Edge e : xv.getXRevAdj()) {
							XEdge xe = (XEdge) e;
							if (xe.isDisabled())
								continue;
							if(sc.getComponentno(e.otherEnd(xv)) == j){
								if(xe.getModifyWeight()!=0){
									xe.disable();
								}
							}
							else if (sc.getComponentno(e.otherEnd(xv)) == i) {
								xe.disable();
								if (minWeight > xe.getModifyWeight()) {
									minWeight = xe.getModifyWeight();
									minEdge = xe;

								}
							}
						}
					}
					if (minEdge != null) {
						//minEdges[i][j] = minEdge;
						minEdgeMap.put(i+" "+j,minEdge);
						XEdge xe = (XEdge) minEdge;

						// System.out.println("BetweenComponent" + i + " " + j +
						// " " + minEdge);
					}

				}
			}

		}

		for (int i = 0; i < compList.length; i++) {
			Vertex from;
			Vertex to;
			if (compList[i].getXv() != null) {
				from = compList[i].getXv();
			} else {
				if (compList[i].getList().size() <= 1) {
					from = compList[i].getList().get(0);
				} else {
					xg.incrementSize();
					XGraph.XVertex xvertex = new XGraph.XVertex(xg.size() - 1, compList[i].getList());
					xg.xv[xg.size() - 1] = xvertex;
					from = xvertex;
					compList[i].setXv(from);
					xg.disableCycle(compList[i].getList());

				}

			}
			for (int j = 0; j < compList.length; j++) {
				if (j != i) {
					if (minEdgeMap.get(i+" "+j) != null) {
						if (compList[j].getXv() != null) {
							to = compList[j].getXv();
						} else {
							if (compList[j].getList().size() <= 1) {
								to = compList[j].getList().get(0);
							} else {
								xg.incrementSize();
								XGraph.XVertex xvertex = new XGraph.XVertex(xg.size() - 1, compList[j].getList());
								xg.xv[xg.size() - 1] = xvertex;
								to = xvertex;
								compList[j].setXv(to);
								xg.disableCycle(compList[j].getList());

							}
						}

						if ((compList[i].getList().size() > 1 || compList[j].getList().size() > 1)) {
							XEdge minE = new XEdge(xg.getVertex(from), xg.getVertex(to), minEdgeMap.get(i+" "+j).getWeight(),
									((XEdge) minEdgeMap.get(i+" "+j)).getModifyWeight());
							minE.minEdge = (XEdge) minEdgeMap.get(i+" "+j);
							minE.enable();
							xg.getVertex(from).xadj.add(minE);
							xg.getVertex(to).xrevadj.add(minE);
						} else {
							XEdge xe = (XEdge) minEdgeMap.get(i+" "+j);
							xe.enable();
						}
					}

				}
			}
		}

		// System.out.println(xg);

	}

	static int doBFS(XGraph xg, Vertex start, List<Edge> dmst) {
		// System.out.println(xg);
		BFS b = new BFS(xg, start);

		b.bfs();
		boolean isAllSeen = isAllVerticesSeen(xg, b, start);
		if (isAllSeen) {
			while (xg.graphSize > xg.originalSize) {
				//ArrayList<Vertex> disableVertices = new ArrayList<Vertex>();
				Vertex u=xg.xv[xg.graphSize-1];
				
					if (xg.getVertex(u).collab != null && xg.getVertex(u).collab.size() > 0) {

						xg.EnableCycle(xg.getVertex(u).collab);
		
						//disableVertices.add(u);
						// System.out.println(xg);
						for (Edge xe : xg.getVertex(u).xrevadj) {
							XEdge e1 = (XEdge) xe;
							if (!e1.isDisabled()) {
								if (e1.minEdge != null) {
									findST(xg, e1.minEdge.to, xg.getVertex(u).collab);
									e1.minEdge.enable();
									e1.disable();
									//System.out.println(xg);
									break;
								}
							}
						}
					}
					for(Edge e2:xg.getVertex(u).getXAdj()){
                        for (Edge e : xg.getVertex(e2.to).getXRevAdj()) {
							XEdge e1 = (XEdge) e;
							if (!e1.isDisabled()) {
								if (e1.minEdge != null) {
									e1.minEdge.enable();
									e1.disable();
									// System.out.println(xg);
								}
							}

						}
					}
				//}

				
					xg.getVertex(u).disable();
					xg.graphSize--;
				}
			return 1;
			}
			


			return -1;

	}

	static void findST(XGraph xg, Vertex start, List<Vertex> cycle) {
		BFS b = new BFS(xg, start);
		// System.out.println(xg);
		b.bfs();
		if (isCycleSeen(xg, b, start, cycle)) {
          
			for (Edge xe : xg.getVertex(start).getXRevAdj()) {
				XEdge e1 = (XEdge) xe;
				if (!e1.isDisabled()) {
					e1.disable();
				}
			}
		}
	}

	

	

	public static void getMST(BFS b, XGraph xg, Vertex start, List<Edge> dmst) {
		for (Vertex u : xg) {
			Vertex v = b.getVertex(u).getParent();
			if (v != null) {
				for (Edge e : u) {
					if (e.to != v) {
						XEdge e1 = (XEdge) e;
						e1.disable();
					}
				}
			}
		}

	}

	static boolean isAllVerticesSeen(XGraph xg, BFS b, Vertex start) {

		for (Vertex u : xg) {
			BFSVertex bv = b.getVertex(u);
			if (!bv.seen && start != u) {
				return false;
			}
		}
		return true;
	}

	static boolean isCycleSeen(XGraph xg, BFS b, Vertex start, List<Vertex> cycle) {

		for (Vertex u : cycle) {
			BFSVertex bv = b.getVertex(u);
			if (!bv.seen && start != u) {
				return false;
			}
		}
		return true;
	}

	static ArrayList<Edge> zeroGraph(XGraph xg,Vertex start) {
		Integer minWeight = null;
		ArrayList<Edge> disabledEdges = new ArrayList<Edge>();
		for (Vertex v : xg) {
		if(!v.equals(start)){
			XGraph.XVertex xv = xg.getVertex(v);
			minWeight = null;
			for (Edge xe : xv.getXRevAdj()) {
				if (((XGraph.XEdge) xe).isDisabled()) {
					continue;
				}
				if (minWeight == null) {
					minWeight = ((XEdge) xe).getModifyWeight();

				} else if (minWeight > ((XEdge) xe).getModifyWeight()) {
					minWeight = ((XEdge) xe).getModifyWeight();
				}
			}
			for (Edge xe : xv.getXRevAdj()) {
				if (((XGraph.XEdge) xe).isDisabled()) {
					continue;
				}

				((XEdge) xe).setModifyWeight(((XEdge) xe).getModifyWeight() - minWeight);
				// xe.setWeight(xe.getWeight() - minWeight);
				if (((XEdge) xe).getModifyWeight() != 0) {

					((XGraph.XEdge) xe).disable();
					disabledEdges.add((XGraph.XEdge) xe);
				}

			}
		}
		}
		return disabledEdges;
	}

}
