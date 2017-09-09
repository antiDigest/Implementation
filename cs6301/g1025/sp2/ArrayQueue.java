package cs6301.g1025.sp2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.naming.SizeLimitExceededException;

/**
 *Implement array-based, bounded-sized queues, that support the following
 *operations: offer, poll, peek, isEmpty (same behavior as in Java's Queue
 *interface).  In addition, implement the method resize(), which doubles
 *the queue size if the queue is mostly full (over 90%, say), or halves it
 *if the queue is mostly empty (less then 25% occupied, say).  Let the
 *queue have a minimum size of 16, at all times.
 *
 */

public class ArrayQueue<T> implements Iterable<T> {
	int size;
	T[] queue; //actual queue
	int head;
	int tail;
	
	public ArrayQueue(){
		
	}
	
	public ArrayQueue(int n){
		size = n;
	}
	public void resize(){
		// TODO
	}
	
	
	public boolean add(T elem) throws IllegalStateException, ClassCastException, NullPointerException,
									  SizeLimitExceededException {
		// TODO try to use assert
		if((tail - head) > size || (size - head + tail) < size ) {
			throw new SizeLimitExceededException();
		}
		else {
			queue[++tail] = elem;
		}
		return true;
	}
	
	public T peek(){
		return queue[head];
	}
	
	public T pop() {
		T elem = queue[head];
		queue[head] = null;
		head++;
		return elem;
	}
	
	public boolean offer(T elem) throws IllegalStateException, SizeLimitExceededException{
		 
		if((tail - head) > size || (size - head + tail) < size ) {
			return false;
		}
		
		queue[++tail] = elem;
		return true;
	}
	
	public T poll(){
		return null;
	}
	
	public boolean isEmpty(){
		return peek() == null;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) throws NoSuchElementException {
		
		
	}

}
