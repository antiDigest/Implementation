package cs6301.g10;

import cs6301.g10.utils.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Diameter {

    public Diameter(){}

    public static LinkedList<Graph.Vertex> diameter(Graph g){
        BFS bfs = new BFS(g);

        Graph.Vertex last = null;
        for(Graph.Vertex u: g) {
            if (!bfs.seen(u))
                last = bfs.bfsVisit(u);
        }

        BFS bfs2 = new BFS(g);
        last = bfs2.bfsVisit(last);

        LinkedList<Graph.Vertex> list = new LinkedList<Graph.Vertex>();

        BFS.BFSVertex l = bfs2.getBFSVertex(last);
        int diameter = 0;
        while(l!=null){
            list.add(l.element);
            l = l.prev;
            diameter++;
        }
//        System.out.println("Diameter: "+diameter);

        return list;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int evens = 0;
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
        Graph g = Graph.readGraph(in);
        Diameter d = new Diameter();
        System.out.println( "Diameter: " + d.diameter(g));
    }
}
