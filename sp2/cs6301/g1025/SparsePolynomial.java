package sp2.cs6301.g1025;

import java.util.AbstractMap.SimpleEntry;

import cs6301.g00.SinglyLinkedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//import cs6301.g25.sp2.SinglyLinkedList.Entry;

public class SparsePolynomial extends SinglyLinkedList<SimpleEntry<Integer,Integer>>{
	
	/**
	 *returns the sum of this polynomial and @param other polynomial and updates this polynomial 
	 */
	public void add(SparsePolynomial other) {
		//SparsePolynomial result = new SparsePolynomial();
		Entry<SimpleEntry<Integer,Integer>> tailx  = this.head;
		Entry<SimpleEntry<Integer,Integer>> tc = this.head.next;
		Entry<SimpleEntry<Integer,Integer>> oc = other.head.next;
		
		while(tc != null && oc != null){
			int res = tc.element.getKey().compareTo(oc.element.getKey());
			if(res == -1){
				tailx.next = tc;
				tc = tc.next;
			} else if(res == 0){
				tc.element.setValue(tc.element.getValue() + oc.element.getValue());
				tailx.next = tc;
				tc = tc.next;
				oc = oc.next;
				
			} else{
				tailx.next = oc;
				oc = oc.next;
			}
			tailx = tailx.next;
		}
		
		if(tc == null){
			tailx.next = oc;
			this.tail = other.tail;
		} else {
			tailx.next = tc;
		}
		
		this.resize();
		
	}
	
	public double evaluate(double val){
		double res = 0.0;
		Entry<SimpleEntry<Integer, Integer>> cursor = this.head.next;
		while(cursor != null){
			res += Math.pow(val, cursor.element.getKey())*cursor.element.getValue();
			cursor = cursor.next;
		}
		return res;
	}
	/**
	 *Updates the size of a polynomial after it is modified
	 */
	public void resize(){
		Entry<SimpleEntry <Integer, Integer>> cursor = this.head.next;
		int size = 0;
		while(cursor != null){
			cursor = cursor.next;
			size++;
		}
		this.size = size;
	}
	
	/**
	 *returns the difference of this polynomial and @param other polynomial and updates this polynomial 
	 */
	public void subtract(SparsePolynomial other){
		// SparsePolynomial signedSP = new SparsePolynomial();
		
		Entry<SimpleEntry <Integer, Integer>> cursor = other.head.next;
		while(cursor != null){
			cursor.element.setValue(-1*cursor.element.getValue());
			cursor = cursor.next;
		}
		this.add(other);
		
	}
	
	/**
	 *Creates a duplicate of current list and returns the duplicate
	 */
	public SparsePolynomial getDuplicate() {
		SparsePolynomial sp = new SparsePolynomial();
		Entry<SimpleEntry<Integer, Integer>> cursor = this.head.next;
		while(cursor != null){
			sp.add(cursor.element);
			cursor = cursor.next;
		}
		return sp;
		
	}
	
	
	/**
	 *multiplies this polynomial with @param other polynomial and updates this polynomial
	 */
	public void multiply(SparsePolynomial other){
		SparsePolynomial thisClone = this.getDuplicate();
		this.head.next = null;//setting the list to null
		Entry<SimpleEntry<Integer, Integer>> tc = thisClone.head.next;
		while(tc != null){
			SparsePolynomial sp = new SparsePolynomial();
			//constructing a list for element in this list - not space optimal
			Entry<SimpleEntry<Integer, Integer>> oc = other.head.next;
			while(oc != null){
				int mantissa = tc.element.getValue()*oc.element.getValue();
				int exponent = tc.element.getKey() + oc.element.getKey();
				sp.add(new SimpleEntry<Integer, Integer>(exponent, mantissa));
				oc = oc.next;
			}
			tc = tc.next;
			this.add(sp);
			
		}
	}
	
	
	@Override
	public void printList(){
		/**
		 * Code without using implicit iterator in for each loop:
		 * 
		 * Entry<T> x = head.next; while(x != null) { System.out.print(x.element
		 * + " "); x = x.next; }
		 */
		if(this.head.next == null) {
			System.out.println("Empty List");
			return;
		}
		System.out.print(this.size + ": ");
		StringBuilder sb = new StringBuilder();
		for (SimpleEntry<Integer, Integer> item : this) {
			//String coeff = item.getKey() == 0 ? item.getValue().toString() + " + " : item.getValue()+"x^"+item.getKey()+" + "; 							  
			sb.append("("+item.getValue()+", "+item.getKey()+")->");
		}
		
		sb.delete(sb.length()-2, sb.length());

		System.out.println(sb);
	}

	public static void main(String[] args) {
		
		SparsePolynomial sp1 = new SparsePolynomial();
		SparsePolynomial sp2 = new SparsePolynomial();
		
		Scanner sc;// = new Scanner();
		if(args.length > 0){
			File in = new File(args[0]);

			try {
				sc = new Scanner(in);
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				// e.printStackTrace();
			}
			//TODO read input and create polynomials
		} else {
		
		
			sp1.add(new SimpleEntry<Integer, Integer>(0, 3));
			sp1.add(new SimpleEntry<Integer, Integer>(25, 5));
			sp1.add(new SimpleEntry<Integer, Integer>(50, 2));
			
			sp2.add(new SimpleEntry<Integer, Integer>(1, 1));
			sp2.add(new SimpleEntry<Integer, Integer>(25, 3));
			sp2.add(new SimpleEntry<Integer, Integer>(49, 9));
		}
		System.out.print("Polynomial A  ");
		sp1.printList();
		System.out.print("Polynomial B ");
		sp2.printList();
		
		System.out.println(sp1.evaluate(1));
		/*System.out.print("Sum: ");
		SparsePolynomial sp1tmp = sp1.getDuplicate();
		sp1tmp.add(sp2);
		sp1tmp.printList();
		
		System.out.print("Difference: ");
		sp1tmp.head.next = null;
		sp1tmp = sp1.getDuplicate();
		sp1tmp.subtract(sp2);
		sp1tmp.printList();*/
		
		System.out.print("Product ");
		sp1.multiply(sp2);
		sp1.printList();
		

	}

}
