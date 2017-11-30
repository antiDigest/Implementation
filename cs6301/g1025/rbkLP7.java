// Driver program for testing LP7
package cs6301.g1025;
import cs6301.g1025.Graph;
import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class rbkLP7 {
    static int VERBOSE = 0, which = 0;
    public static void main(String[] args) throws FileNotFoundException {
        int extra = 0;
        if(args.length > 0) { which = Integer.parseInt(args[0]); }
        if(args.length > 1) { extra = Integer.parseInt(args[1]); }
        if(args.length > 2) { VERBOSE = Integer.parseInt(args[2]); }
//        java.util.Scanner in = new java.util.Scanner(System.in);
        Scanner in;
        if (args.length > 3) {
            File inputFile = new File(args[3]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
        Graph g = Graph.readDirectedGraph(in);
        int s = in.nextInt();
        int t = in.nextInt();
        java.util.HashMap<Edge,Integer> capacity = new java.util.HashMap<>();
        int[] arr = new int[1 + g.edgeSize()];
        for(int i=1; i<=g.edgeSize(); i++) {
            arr[i] = 1;   // default capacity
        }
        while(in.hasNextInt()) {
            int i = in.nextInt();
            int cap = in.nextInt();
            arr[i] = cap;
        }

        Vertex src = g.getVertex(s);
        Vertex target = g.getVertex(t);

        if(extra > 0) {
            for(Edge e: src) {
                Vertex v = e.otherEnd(src);
                arr[e.getName()] += extra;
                for(Edge e2: v) {
                    arr[e2.getName()] += extra/10;
                }
            }
            java.util.Iterator<Edge> it = target.reverseIterator();
            while(it.hasNext()) {
                Edge e = it.next();
                Vertex v = e.otherEnd(target);
                java.util.Iterator<Edge> it2 = v.reverseIterator();
                while(it2.hasNext()) {
                    Edge e2 = it2.next();
                    arr[e2.getName()] += extra/10;
                }
                arr[e.getName()] += extra;
            }
        }

        for(Vertex u: g) {
            for(Edge e: u) {
                capacity.put(e, arr[e.getName()]);
            }
        }

        cs6301.g00.Timer timer = new cs6301.g00.Timer();
        Flow f = new Flow(g, src, target, capacity);
        int value = -1;
        if(which == 0) {
            System.out.println("Relabel to front:");
            value = f.relabelToFront();
        } else {
            System.out.println("Dinitz:");
            value = f.dinitzMaxFlow();
        }

        System.out.println(value);
        java.util.Set<Vertex> cutS = f.minCutS();
        java.util.Set<Vertex> cutT = f.minCutT();
        System.out.println("|S| = " + cutS.size() + " |T| = " + cutT.size());
        int capS = 0;
        for(Edge e: src) {
            capS += f.capacity(e);
        }
        int capT = 0;
        java.util.Iterator<Edge> it = target.reverseIterator();
        while(it.hasNext()) {
            Edge e = it.next();
            capT += f.capacity(e);
        }
        System.out.println("capS = " + capS + " capT = " + capT);

        if(VERBOSE > 0) {
            for(Vertex u: g) {
                System.out.print(u + " : ");
                for(Edge e: u) {
                    if(f.flow(e) != 0) { System.out.print(e + ":" + f.flow(e) + "/" + f.capacity(e) + " | "); }
                }
                System.out.println();
            }
            System.out.println("Min cut: S = " + cutS);
            System.out.println("Min cut: T = " + cutT);
        }

        System.out.println(timer.end());
    }
}

