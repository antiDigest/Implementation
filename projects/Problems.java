package cs6301.projects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import cs6301.g00.Graph;
import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

public class Problems {
	
//	/**
//	 *Generic merge sort for generic type which extends comparable interface 
//	 */
//	static<T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp){
//		mergeSort(arr, tmp, 0, arr.length - 1);
//	}
//	/**
//	 * Implementation of merge sort using generic type.
//	 */
//	static<T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp, int p, int r){
//		if(p >= r) return;
//		int q = (p + r) >>> 1;
//		mergeSort(arr, tmp, p, q);
//		mergeSort(arr, tmp, q+1, r);
//		//merge operation
//		for(int i = p; i <= r; i++){
//			tmp[i] = arr[i];
//		}
//		int i = p; 
//		int j = q+1;
//		for(int k = p; k <= r; k++){
//			if( j > r || (i <= q && tmp[i].compareTo(tmp[j]) < 0)){
//				arr[k] = tmp[i++];
//			} else {
//				arr[k] = tmp[j++];
//			}		
//		}
//	}
//	
//	static void mergeSort(int[] arr, int[] tmp){
//		mergeSort(arr, tmp, 0, arr.length - 1);
//	}
//	/* 
//	 * tmp array is used to store values during the merge operation. 
//	 * */
//	static void mergeSort(int[] arr, int[] tmp, int p, int r){
//		if(p >= r) return;
//		int q = (p + r) >>> 1;
//		mergeSort(arr, tmp, p, q);
//		mergeSort(arr, tmp, q+1, r);
//		//merge operation
//		for(int i = p; i <= r; i++){
//			tmp[i] = arr[i];
//		}
//		int i = p; 
//		int j = q+1;
//		for(int k = p; k <= r; k++){
//			if( j > r || (i <= q && tmp[i] <= tmp[j])){
//				arr[k] = tmp[i++];
//			} else {
//				arr[k] = tmp[j++];
//			}		
//		}
//	}
//	
//	static void merge(int arr[], int tmp[], int p, int q, int r){
//		for(int i = p; i <= r; i++){
//			tmp[i] = arr[i];
//		}
//		int i = p; 
//		int j = q+1;
//		for(int k = p; k <= r; k++){
//			if( j > r || (i <= q && tmp[i] <= tmp[j])){
//				arr[k] = tmp[i++];
//			} else {
//				arr[k] = tmp[j++];
//			}		
//		}
//	
//	}
//	
//
//	static<T extends Comparable<? super T>> void nSquareSort(T[] arr){
//		
//		
//	}
//	
//	static LinkedList<Graph.Vertex> diameter(Graph g) {
//		Random r = new Random();
//		//Vertex source = graph.verts.get(1);
//		//Graph.Vertex source = g.v[r.nextInt(g.size())];//chose a random int less than 9 and increment by 1 to avoid 0
//		Graph.Vertex source = g.v[0];
//		Graph.Vertex v = bfs(source, g.size());
//		resetParams(g);
//		Graph.Vertex x = bfs(v, g.size());
//		LinkedList<Graph.Vertex> lst = new LinkedList<Graph.Vertex>();
//		int dia = x.distance;
//		while(x != null){
//			lst.add(x);
//			x = x.parent;
//		}
//		System.out.println("Longest Path: " + lst);
//		System.out.println("Diameter: " + dia);
//
//		return lst;
//		
//	}
//	/**
//	 * reset all the params of vertices in a given graph g
//	 */
//	public static void resetParams(Graph g){
//		for(Graph.Vertex u : g.v){
//			u.distance = 0;
//			u.parent = null;
//			u.seen = false;
//		}
//	}
//	
//	static Graph.Vertex bfs(Graph.Vertex source, int size){
//		Queue<Graph.Vertex> q = new ArrayDeque<>();
//		q.add(source);
//		source.seen = true;
//		PriorityQueue<Graph.Vertex> pq = new PriorityQueue<Graph.Vertex>(size, new VertexDistComparator());
//		source.distance = 0;
//		pq.add(source);
//		while(!q.isEmpty()){
//			Graph.Vertex u = q.poll();
//			for(Graph.Edge e : u.adj){
//				Graph.Vertex v = e.otherEnd(u);
//				if(!v.seen){ 
//					q.add(v);
//					v.parent = u;
//					v.seen = true;
//					v.distance = 1 + u.distance;
//					pq.add(v);
//				}
//			}
//		}
//		return pq.poll();
//	}
//	
	public static void main(String[] args) throws FileNotFoundException { 
		int n = 16000000;//size of array
		File in = new File("src/cs6301/g25/sp1/input.txt");
		Scanner sc = new Scanner(in);
		
		Graph graph = Graph.readGraph(sc, false);
		//diameter(graph);
		
		int[] arr = new int[n];
		int[] tmp = new int[arr.length];
		for(int i=0; i<n; i++){
			arr[i] = i;
		}
		
		Character[] cArr = new Character[n];
		Character[] cTmp = new Character [n];
		Random r = new Random();
		final String alphabet = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int N = alphabet.length();
		for(int i=0; i<n; i++){
			cArr[i] = alphabet.charAt(r.nextInt(N));
		}
		
		Shuffle.shuffle(arr);
		
		//int[] a = {3,5,2,4, 2,4,5,3,2,6,7,4,7,7,5,4,6, 8, 9,9,1,4,7,4,6,5,7};
		//Character[] in1 = {'a', 'c', 'o', 'y', 'h', 'b'};

		
		Timer timer = new Timer();
		timer.start();
		//mergeSort(arr, tmp);
		timer.end();
		System.out.println(timer);
		System.out.println();
		timer.start();
		//mergeSort(cArr, cTmp);
		timer.end();
		System.out.println(timer);
		//System.out.println(Arrays.toString(in1)); 
		
		
	}

}