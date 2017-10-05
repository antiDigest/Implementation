package cs6301.g1025;

import java.util.PriorityQueue;

public class PrimeFactor implements Comparable<PrimeFactor>{
	
	static final int[] primes = {5, 7, 3};
	long value;
	int state;
	PrimeFactor(int st, long val){
		state = st;
		this.value = val;	
	}
	
	@Override
	public int compareTo(PrimeFactor that) {
		return this.value < that.value ? -1 : this.value == that.value ? 0 : 1;
	}
	
	@Override
	public String toString(){
		return Long.toString(this.value);
	}

	
	public static void main(String[] args) {

		PriorityQueue<PrimeFactor> pq = new PriorityQueue<PrimeFactor>();

		for(int i = 0; i < primes.length; i++){
			PrimeFactor x = new PrimeFactor(i, primes[i]);
			pq.add(x);
		}
		long prev = 0;
		long n = (long) Math.pow(2, 63);
		while(!pq.isEmpty() && pq.peek().value < n){
			
			PrimeFactor x = pq.poll();
			if (x.value != prev) System.out.print(x+ " ");
			prev = x.value;
			
			int state = x.state;
			long value = x.value;
			for(int i = state; i < primes.length; i++){
				if(primes[i]*value < n)
					pq.add(new PrimeFactor(state, primes[i]*value));
			}
			
		}
	}
}
