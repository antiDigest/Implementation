package cs6301.g1025;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import cs6301.g1025.BFS.BFSVertex;
import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;

public class RewardCollection {

	Graph g;
	Vertex s;
	HashMap<Vertex, Integer> vertexRewardMap;
	BFS bfshandle;
	int maxProfit;
	List<Vertex> tour;

	/**
	 * Constructor for Reward Collection problem
	 * 
	 * @param g
	 *            : Graph g
	 * @param s
	 *            : Vertex
	 * 
	 * @param vertexRewardMap:HashMap
	 * @ param tour:List<Vertex>
	 *
	 */
	public RewardCollection(Graph g, Vertex s, HashMap<Vertex, Integer> vertexRewardMap, List<Vertex> tour) {
		this.g = g;
		this.s = s;
		this.vertexRewardMap = vertexRewardMap;
		bfshandle = new BFS(g, s);
		maxProfit = 0;
		this.tour = tour;
	}

	/**
	 * returns the maxprofit obtained through the
	 * tour.
	 */

	int rewardProblem() {
		XGraph xg = new XGraph(g);
		BellmanFordFast.BellmanFord(xg, xg.getVertex(s));
		xg.getVertex(s).seen = true;
		ArrayList<Vertex> curpath = new ArrayList<Vertex>();
		curpath.add(s);
		enumerate(xg, xg.getVertex(s), vertexRewardMap.get(s),curpath);
		return maxProfit;
	}

	/**
	 * By taking design in to consideration and Without changing anything
	 * in standard BFS class,i implemented this function. i can improve the time
	 * complexity a little bit if i can change the bfs() method in BFS class.
	 * 
	 */
	void callBFS(Graph g, Vertex v, List<Vertex> path, int profit) {
		bfshandle.reinitialize(v);
		reinitialize(path);
		bfshandle.bfs();
		Vertex end = null;
		//finding where bfs ends after calling bfs method-just checking the edges of the source
		for (Edge e : s) {
			if ((v.equals(e.otherEnd(s)) && path.size() > 2)
					|| (bfshandle.getVertex(e.otherEnd(s)).seen && bfshandle.getVertex(e.otherEnd(s)).parent != null)) {
				end = e.otherEnd(s);
				break;
			}
		}
		LinkedList<Vertex> temp = new LinkedList<Vertex>();// using linked list  to make add operation in o(1)  time
	       if (end != null) {
			tour.clear();
			temp.add(0, s);
			while (bfshandle.getVertex(end).parent != null) {
				temp.add(0, end);
				end = bfshandle.getVertex(end).parent;
			}
			temp.addAll(0, path);
			tour.addAll(temp);
			maxProfit = profit;
		}
	}

	/**@param path:List<Vertex> path
	 *  sets the bfshandle vertices-seen to true according to the path.   
	 */
	void reinitialize(List<Vertex> path) {
		for (Graph.Vertex u : path) {
			BFSVertex bu = bfshandle.getVertex(u);
			bu.seen = true;
		}
	}

	/**@param path:XGraph xg
	 * @param xu:XVertex
	 * @param int: pathcost
	 * @param curpath:ArrayList<Vertex> 
	 * checks the paths while enumerating
	 *    
	 */
	void enumerate(XGraph xg, XVertex xu, int pathcost, ArrayList<Vertex> curpath) {
		XVertex xu1 = xg.getVertex(xu);
		if (!xu1.equals(s) && pathcost > maxProfit) {
			callBFS(xg, xu, curpath, pathcost);
		}
		for (Edge e : xu1) {
			XVertex xv = (XVertex) e.otherEnd(xu1);
			if ((!xv.seen && xv.distance == xu1.distance + e.weight)) {
				curpath.add(xv);
				xv.seen = true;
				enumerate(xg, xv, vertexRewardMap.get(xv) + pathcost,curpath);
				curpath.remove(curpath.size() - 1);
				xv.seen = false;
			}

		}
	}
}