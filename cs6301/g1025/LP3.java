/**
 * Long Project 3
 * Implementing Tarjan's algorithm for finding MST in directed graphs
 * @author Antriksh, Saikumar, Swaroop, Gunjan
 * Version 1.0 10/28/2017: Implemented
 */
package cs6301.g1025;

import cs6301.g00.Timer;
import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Integer.min;

public class LP3 {
    public static final int INFINITY = Integer.MAX_VALUE; // For min weight
    static int VERBOSE = 0;


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

        int start = in.nextInt();  // root node of the MST
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
     * TO DO: List dmst is an empty list. When your algorithm finishes,
     * it should have the edges of the directed MST of g rooted at the
     * start vertex.  Edges must be ordered based on the vertex into
     * which it goes, e.g., {(7,1),(7,2),null,(2,4),(3,5),(5,6),(3,7)}.
     * In this example, 3 is the start vertex and has no incoming edges.
     * So, the list has a null corresponding to Vertex 3.
     * The function should return the total weight of the MST it found.
     */
    public static int directedMST(Graph g, Vertex start, List<Edge> dmst) {
        XGraph dmstg = new XGraph(g);
        return dMST(dmstg, start, dmst);
    }

    public static int dMST(XGraph g, Vertex start, List<Edge> dmst) {
        SCC cc = new SCC(g);
        int components = cc.stronglyConnectedComponents(g);

        while (components > 1) {
            // Transform Weights
            for (Vertex u : g) {
                XGraph.XVertex k = g.getVertex(u);
                if (u != start) {
                    int minWeight = INFINITY;
                    for (Edge e : k.getRevAdj()) {
                        minWeight = min(e.getWeight(), minWeight);
                    }
                    for (Edge e : k.getRevAdj()) {
                        e.setWeight(e.getWeight() - minWeight); // Reducing weight
                        // Disabling edges
                        if (e.getWeight() > 0) {
                            ((XGraph.XEdge) e).disable();
                        }
                    }
                }
            }

            // Check components -- scc == 1: break ? do Nothing
            cc = new SCC(g);
            components = cc.stronglyConnectedComponents(g);

            if (components <= 1)
                break;

            // HashMap to store the list of vertices for each cno !
            HashMap<Integer, List<Vertex>> hash = new HashMap<>();

            Iterator<Vertex> it = g.iterator();
            Vertex u = next(it);
            while (u != null) {
                int cno = cc.getVertex(u).getCno();
                List<Vertex> list = hash.get(cno);
                if (list == null) {
                    list = new LinkedList<>();
                }
                for (XGraph.XEdge e : g.getVertex(u).getRevAdj()) {
                    if (e.getWeight() > 0) {
                        e.enable();
                    } else {
                        e.disable();
                    }
                }
                list.add(u);
                hash.put(cno, list);
                u = next(it);
            }

            for (HashMap.Entry<Integer, List<Vertex>> map : hash.entrySet()) {
                List<Vertex> list = map.getValue();
                if (list.size() > 1) {
                    g.addVertex(list);
                    g.disableAll(list);
                }
            }
        }

        // Unravel all components
        for (Vertex u : g) {
            if(g.getVertex(u).isDisabled()){
                System.out.println("DISABLED: " + u);
            }
            g.unRavel(u);
        }

        return -1;
    }

    static Vertex next(Iterator<Vertex> it) {
        return it.hasNext() ? it.next() : null;
    }

}
