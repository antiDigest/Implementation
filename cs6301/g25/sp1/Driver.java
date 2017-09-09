package cs6301.g25.sp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cs6301.g00.Graph;
import cs6301.g00.Shuffle;
import cs6301.g00.Timer;

public class Driver {	
	final static int MILLION = 1000000;
	
	public static Integer[] getRandomArray(int size){
		Integer[] arr = new Integer[size];
		for(int i=0; i<size; i++){
			arr[i] = i;
		}
		
		Shuffle.shuffle(arr);
		return arr;
		
	}

	public static void main(String[] args) throws FileNotFoundException{
		
		/**
		 *Problem 1: Implementation of nlogn sorting algorithm (MergeSort) and n^2 sorting algorithm (insertion sort)
		 */
		int size = 1*MILLION; 
		Timer timer = new Timer();
		
		//Trials
		Integer[] arr;
		Integer[] tmp;
		for(int i=1; i<17; i=i*2){
			arr = getRandomArray(i*MILLION);
			tmp = new Integer[i*MILLION];
			System.out.println("Sorting "+ i + " Million Integers using Generic Merge Sort" );
			timer.start();
			Sorting.mergeSort(arr, tmp);
			timer.end();
			System.out.println(timer);
			System.out.println();
		
		}
		
		for(int i=1; i<17; i=i*2){
			
			arr = getRandomArray(i*100000);
			tmp = new Integer[size];
			System.out.println("Sorting "+ i + " hundred thousand Integers using insertion Sort" );
			timer.start();
			Sorting.nSquareSort(arr);
			timer.end();
			System.out.println(timer);
			System.out.println();
		}
		
		
		/**
		 * Question 2: Finding Diameter of a graph using given algorithm in the question.
		 * Please use Driver
		 */
		Scanner scanner;
		
		if (args.length > 0) {
			File in = new File(args[0]);
			scanner = new Scanner(in);
		} else {
			System.out.println("Enter the graph: ");
			scanner = new Scanner(System.in);
		}
		
		Graph graph = Graph.readGraph(scanner, false);
		Diameter diameter = new Diameter(graph);
		diameter.getDiameter();
		
		System.out.println();
		
	}

}
