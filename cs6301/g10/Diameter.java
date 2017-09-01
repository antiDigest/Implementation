package cs6301.g10;

import cs6301.g10.utils.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Diameter {
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
        BFS bfs = new BFS(g);

        Graph.Vertex last = null;
        for(Graph.Vertex u: g) {
            if (!bfs.seen(u))
                last = bfs.bfsVisit(u);
        }

        BFS bfs2 = new BFS(g);
        last = bfs2.bfsVisit(last);

        BFS.BFSVertex l = bfs2.getBFSVertex(last);
        int diameter = 0;
        System.out.println("Printing Diameter");
        while(l!=null){
            if(l.prev!=null)
                System.out.print((l.element.name+1) + "->");
            else
                System.out.println((l.element.name+1));
            l = l.prev;
            diameter++;
        }
        System.out.println("Diameter: "+diameter);
    }
}
