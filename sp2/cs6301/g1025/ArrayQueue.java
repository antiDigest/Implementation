package sp2.cs6301.g1025;

import cs6301.g00.Utils;

/**
 * Implement array-based, bounded-sized queues, that support the following
 * operations: offer, poll, peek, isEmpty (same behavior as in Java's Queue
 * interface). In addition, implement the method resize(), which doubles the
 * queue size if the queue is mostly full (over 90%, say), or halves it if the
 * queue is mostly empty (less then 25% occupied, say). Let the queue have a
 * minimum size of 16, at all times.
 *
 */

/**
 *ArrayQueue implements array based bounded size queue
 *
 */

public class ArrayQueue<T>{
	int size;
	T[] queue; // actual queue
	int head = 0;//head of the queue
	int tail = 0;//end of the queue
	int currSize = 0;//to store the actual size of the queue
	final int minimum = 16;//minimum size of the queue at all times
	
	public ArrayQueue() {
		this.size = minimum;//minimum
		this.currSize = 0;
		this.queue = (T[]) new Object[size];
	}
	
	/**
	 *resizes the queue when explicitly called by the user
	 *doubles if the currSize is 90% of the size or halves if 
	 *currSize is 25% of the size
	 */
	public void resize() {

		if (currSize > (int) (0.9 * size)) {
			this.resize("double");
		} else if (size > minimum && currSize < (int) (0.25 * size)) {
			this.resize("half");
		} else
			return;

	}

	private void resize(String str){
		int newSize = str.equals("double") ? 2*this.size : this.size / 2;
		T[] newQ = (T[]) new Object[newSize];
		
		for(int i=0; i < currSize; i++){
			newQ[i] = this.queue[(head + i) % queue.length];
		}
		this.queue = newQ;
		this.head = 0;
		this.tail = currSize;
		this.size = newSize;
	}

	/**
	 *returns element without deleting at the head of the queue or null if queue is empty
	 */
	public T peek() {
		return this.queue[head];
	}
	
	/**
	 *adds an element at the end of the queue and returns true 
	 *if currSize is equal to the size returns false
	 */
	public boolean offer(T elem) {
		if (size == currSize){
			return false;
		} else {
			queue[tail] = elem;
			this.tail = tail == size - 1 ? 0 : this.tail + 1;
			currSize++;
			return true;
		}
	}

	/**
	 *returns an element at the head of the queue
	 *if queue is empty returns null;
	 */
	public T poll() {
		if (isEmpty())
			return null;
		T elem = this.queue[head];
		this.queue[head] = null;
		this.head = head == size - 1 ? 0 : this.head + 1;
		currSize--;
		return elem;
	}
	
	public boolean isEmpty() {
		return currSize == 0;
	}

	public static void main(String[] args) {
		ArrayQueue<Integer> q = new ArrayQueue<Integer>();
		Utils utils = new Utils();
		Integer[] arr = utils.getRandomArray(20);
		for(Integer x : arr){
			q.offer(x);
		}
		q.resize();
		System.out.println(q.size);
		System.out.println(q.head);
		System.out.println(q.tail);
		
		for(int i=0; i<12; i++){
			q.poll();
		}
		q.resize();
		System.out.println(q.size);
		System.out.println(q.head);
		System.out.println(q.tail);
		q.resize();
	}

}
