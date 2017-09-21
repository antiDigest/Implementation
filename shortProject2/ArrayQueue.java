package shortProject2;

/**
 * Implement array-based, bounded-sized queues, that support the following
 * operations: offer, poll, peek, isEmpty (same behavior as in Java's Queue
 * interface). In addition, implement the method resize(), which doubles the
 * queue size if the queue is mostly full (over 90%, say), or halves it if the
 * queue is mostly empty (less then 25% occupied, say). Let the queue have a
 * minimum size of 16, at all times.
 *
 */
public class ArrayQueue<T> {

	T[] queue;
	int currSize;// no of elements in the queue
	int start;
	int end;
	int minimumlength = 16;

	public ArrayQueue() {

		this.queue = (T[]) new Object[minimumlength];
		start = 0;
		end = 0;

	}

	public void resize() {

		int size = minimumlength;
		boolean resize = false;
		if (isFullForGivenConditions()) {
			size = queue.length * 2;
			resize = true;

		} else if (isEmptyForGivenConditions()) {
			int x = queue.length / 2;
			if (size < x) {
				size = x;
			}
			resize = true;
		}
		if (resize) {
			T[] newQueue = (T[]) new Object[size];

			// Copy the elements from the queue to new queue starting from start
			for (int i = 0; i < currSize; i++) {
				// if it reaches end, simply modulo with queue.length
				newQueue[i] = queue[(start + i) % queue.length];
			}
			// in new queue -elements are from start to oldqueue currSize
			queue = newQueue;
			start = 0;
			end = currSize;
		}
	}

	boolean isFullForGivenConditions() {
		int x = (queue.length * 9) / 10;
		if (currSize >= x) {
			return true;
		} else {
			return false;
		}

	}

	boolean isQueueFull() {
		if (currSize == queue.length) {
			return true;
		} else {
			return false;
		}

	}

	// inserts the element in to the queue
	public boolean offer(T elem) {
		if (isQueueFull()) {// for given constraints
			return false;
		} else {
			queue[end] = elem;
			// increment the current size
			currSize++;
			// increment the last index pointer
			end = end + 1;
			if (end == queue.length)
				end = 0;
			return true;
		}
	}

	/// checks if the queue is empty or not
	boolean isQueueEmpty() {
		if (currSize == 0) {
			return true;
		} else {
			return false;
		}

	}

	// for given conditions less than 25%
	boolean isEmptyForGivenConditions() {
		if (currSize > 0 && currSize < queue.length / 4) {
			return true;

		} else {
			return false;
		}

	}

	// retreives teh head
	public T peek() {

		if (isQueueEmpty()) {
			return null;
		}

		else {
			return queue[start];
		}

	}

	// retreives and removes the head
	public T poll() {

		if (isQueueEmpty()) {
			return null;

		}
		T elem = queue[start];
		// decrement currSize
		currSize = currSize - 1;
		queue[start] = null;
		// increment start pointer
		start = start + 1;
		// check if start is equal to q length
		if (start == queue.length)
			start = 0;

		return elem;
	}

	public static void main(String[] args) {
		ArrayQueue<Integer> q = new ArrayQueue<Integer>();

		q.offer(8);
		q.offer(9);
		System.out.println(q.peek());
		System.out.println(q.poll());
		System.out.println(q.poll());

		System.out.println(q.poll());
		System.out.println(q.poll());
		System.out.println(q.poll());
		System.out.println(q.poll());
		System.out.println(q.peek());
		System.out.println(q.peek());

		q.offer(11);
		q.offer(12);
		q.offer(13);
		q.offer(14);
		System.out.println(q.peek());
		System.out.println(q.poll());
		System.out.println(q.poll());
		System.out.println(q.peek());

		
	}

}
