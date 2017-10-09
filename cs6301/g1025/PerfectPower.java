/******************************************************************************
 *  Compilation:  javac PerfectPower.java
 *  Execution:    java cs6301.g1025.PerfectPower
 *  
 *  Perfect powers problem using algorithm given in the class
 *
 * @author antriksh, swaroop, gunjan, saikumar
 * Ver 1.0: 2017/10/8. Implemented
 *
 ******************************************************************************/

package cs6301.g1025;

import java.util.PriorityQueue;

public class PerfectPower implements Comparable<PerfectPower> {
	int a;
	int b;
	long value;

	public PerfectPower(int a, int b) {
		this.value = (long) Math.pow(a, b);
		this.a = a;
		this.b = b;
	}

	@Override
	public int compareTo(PerfectPower that) {
		return this.value < that.value ? -1 : this.value == that.value ? 0 : 1;
	}
	
	@Override
	public String toString() {
		return this.a + " " + this.b + " " + this.value;
	}

	public static void main(String[] args) {

		long n = 1000;
		PerfectPower initial = new PerfectPower(2, 2);
		PriorityQueue<PerfectPower> pq = new PriorityQueue<PerfectPower>();

		pq.add(initial);
		System.out.println(initial);
		long prev = initial.value;
		while (pq.peek() != null && pq.peek().value < n) {
			PerfectPower t = pq.poll();
			if (t.value != prev)
				System.out.println(t);
			prev = t.value;
			if (t.a == 2) {
				pq.add(new PerfectPower(3, t.b));
				pq.add(new PerfectPower(2, t.b + 1));
			} else {
				pq.add(new PerfectPower(t.a + 1, t.b));
			}
		}

	}

}
