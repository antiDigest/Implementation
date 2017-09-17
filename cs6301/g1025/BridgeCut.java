/**
 * Class to represent a graph
 *  @author antriksh
 *
 */

package cs6301.g1025;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BridgeCut extends GraphAlgorithm<BridgeCut.BCVertex> {

	class BCVertex{
		int name;
		public boolean seen;
		public int dis;
		public int fin;
		public int low;
		boolean cut;
		public Graph.Vertex parent;

		public BCVertex(Graph.Vertex u){
			name = u.getName();
			seen = false;
			dis = 0;
			fin = 0;
			parent = null;
			cut = false;
		}
	}

	private int time=0;

	BridgeCut(Graph g) {
		super(g);
		node = new BCVertex[g.size()];
		for(Graph.Vertex u: g){
			node[u.getName()] = new BCVertex(u);
		}
	}

	void discover(Graph.Vertex u){
		BCVertex bu = getVertex(u);
		bu.dis = ++time;
		bu.low = bu.dis;
	}

	void visit(Graph.Vertex u){
		BCVertex bu = getVertex(u);
		bu.seen = true;
	}

	boolean seen(Graph.Vertex u){
		return getVertex(u).seen;
	}

	void addParent(Graph.Vertex v, Graph.Vertex u){
		BCVertex bv = getVertex(v);
		bv.parent = u;
	}

	void finish(Graph.Vertex u){
		BCVertex bu = getVertex(u);
		bu.fin = ++time;
	}

	void setLow(Graph.Vertex u, Graph.Vertex v){
		BCVertex bu = getVertex(u);
		BCVertex bv = getVertex(v);
		bu.low = Math.min(bu.low, bv.low);
	}

	void setDis(Graph.Vertex u, Graph.Vertex v){
		BCVertex bu = getVertex(u);
		BCVertex bv = getVertex(v);
		bu.low = Math.min(bu.low, bv.dis);
	}

	List<Graph.Edge> findBridgeCut(Graph g){
		List<Graph.Edge> list = new LinkedList<>();
		for(Graph.Vertex u: g){
			if(!seen(u)){
				dfsVisit(u, list);
			}
		}
		return list;
	}

	public void dfsVisit(Graph.Vertex u, List<Graph.Edge> list){
		visit(u);
		discover(u);

		for(Graph.Edge e: u){
			Graph.Vertex v = e.otherEnd(u);
			BCVertex bv = getVertex(v);
			BCVertex bu = getVertex(u);
			if(!seen(v)){
				addParent(v, u);
				dfsVisit(v, list);
				setLow(u, v);
//				System.out.println(v + " " + bv.low);
				if(bv.low == bv.dis){
					list.add(e);
				}
			}
			else if(v!=bu.parent){
				setDis(u, v);
			}
			identifyVertex(list, u, v);

		}
		finish(u);
	}

	void identifyVertex(List<Graph.Edge> list, Graph.Vertex u, Graph.Vertex v){
		BCVertex bu = getVertex(u);
		BCVertex bv = getVertex(v);
		if(bu.parent!=null && bv.low >= bu.dis){
			bu.cut = true;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		Graph g = Graph.readGraph(in);

		BridgeCut bc = new BridgeCut(g);
		System.out.println(bc.findBridgeCut(g));
	}
}
