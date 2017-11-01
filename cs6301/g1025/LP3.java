/**
 * Long Project 3
 * Implementing Tarjan's algorithm for finding MST in directed graphs
 *
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
import cs6301.g1025.XGraph.XEdge;
import cs6301.g1025.XGraph.XVertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Integer.min;

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
            }

        }

    }

    public static int directedMST(Graph g, Vertex start, List<Edge> dmst) {

        XGraph xg = new XGraph(g);
        int mst = dMST(xg, start, dmst);
        return mst;
    }

    public static int dMST(XGraph g, Vertex start, List<Edge> dmst) {

        XGraph zeroG = (XGraph) zeroGraph(g);
        boolean bfs = checkBFS(zeroG, zeroG.getVertex(start));
        SCC cc = new SCC(g);
        int components = cc.stronglyConnectedComponents(g);

        while (!bfs) {
            // Transform Weights
            zeroG = (XGraph) zeroGraph(g);

            cc = new SCC(zeroG);
            components = cc.stronglyConnectedComponents(zeroG);
            System.out.println("COMPONENTS: " + components);

            // HashMap to store the list of vertices for each cno !
            HashMap<Integer, List<Vertex>> hash = new HashMap<>();

            Iterator<Vertex> it = zeroG.iterator();
            Vertex u = next(it);
            while (u != null) {
                int cno = cc.getVertex(u).getCno();
                List<Vertex> list = hash.get(cno);
                if (list == null) {
                    list = new LinkedList<>();
                }
//                for (XGraph.XEdge e : zeroG.getVertex(u).getRevAdj()) {
//                    if (e.getWeight() > 0) {
//                        e.enable();
//                    } else {
//                        e.disable();
//                    }
//                }
                list.add(u);
                hash.put(cno, list);
                u = next(it);
            }

            for (HashMap.Entry<Integer, List<Vertex>> map : hash.entrySet()) {
                List<Vertex> list = map.getValue();
                if (list.size() > 1) {
                    zeroG.addVertex(list);
//                    zeroG.disableAll(list);
                }
            }
            cc = new SCC(zeroG);
            components = cc.stronglyConnectedComponents(zeroG);
            bfs = checkBFS(zeroG, zeroG.getVertex(start));
        }

        unRavel(zeroG.getVertex(start), zeroG, dmst);

        if(dmst.size() > 0)
            return 1;
        return -1;
    }

    static void unRavel(Vertex root, Graph g, List<Edge> dmst){
        XGraph xg = (XGraph) g;
        XVertex xroot = xg.getVertex(root);
        xroot.seen = true;

        for(Edge e: xroot){
            XVertex xv = xg.getVertex(e.otherEnd(xroot));
            XEdge xe = (XEdge) e;
//            if()
            dmst.add(e);
            if (xv.size() > 1 && !xv.seen()){
                MST(xe.minEdgeTo, xv, xg, dmst);
                unRavel(xv, xg, dmst);
            } else if(xv.isDisabled() && !xv.seen()){
                MST(xv, xv.getParent(), xg, dmst);
            } else if (xroot.size() > 1 && !xv.seen()){
                unRavel(xe.minEdgeFrom, xg, dmst);
            } else if (!xv.seen()) {
                unRavel(xv, xg, dmst);
            }
        }
    }

    static void MST(XVertex xu, XVertex parent, XGraph g, List<Edge> dmst){
        if(parent == null) return;
        g.unRavel(parent);
        xu.seen = true;
        for(Edge e: xu.getAdj()){
            XVertex xv = g.getVertex(e.otherEnd(xu));
            dmst.add(e);
            if(xv.getParent() == xu.getParent() && !xv.seen()){
                MST(xv, xv.getParent(), g, dmst);
            }
        }
    }

    /**
     * enable disbaled edges
     */

    static void enableEdges(ArrayList<Edge> disabledEdges) {
        for (Edge e : disabledEdges) {
            ((XGraph.XEdge) e).enable();
        }
    }

    static boolean checkBFS(XGraph xg, Vertex start) {
        BFS b = new BFS(xg, start);
        b.bfs();
        xg.printGraph(b);

        for(Vertex u : xg){
            BFSVertex bu = b.getVertex(u);
            if (!bu.seen && start != u)
                return false;
        }
        return true;
    }

    static Graph zeroGraph(XGraph xg) {
        Integer minWeight;
        ArrayList<Edge> disabledEdges = new ArrayList<>();
        for (Vertex v : xg) {
            XGraph.XVertex xv = xg.getVertex(v);
            minWeight = INFINITY;
            Iterator<Edge> eit = xv.reverseIterator();
            while (eit.hasNext()) {
                Edge xe = eit.next();
                if (minWeight > ((XEdge) xe).getModifyWeight()) {
                    minWeight = ((XEdge) xe).getModifyWeight();
                }
            }

            eit = xv.reverseIterator();
            while (eit.hasNext()) {
                Edge xe = eit.next();

                ((XEdge) xe).setModifyWeight(((XEdge) xe).getModifyWeight() - minWeight);
                // xe.setWeight(xe.getWeight() - minWeight);
                if (((XEdge) xe).getModifyWeight() != 0) {

                    ((XGraph.XEdge) xe).disable();
//                    disabledEdges.add((XGraph.XEdge) xe);
                }

            }
        }
        return xg;
    }

    static Vertex next(Iterator<Vertex> it) {
        return it.hasNext() ? it.next() : null;
    }

}
