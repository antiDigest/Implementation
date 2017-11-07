// Change this to your group number
package cs6301.g1025;

import java.util.*;


// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> implements Iterable<T> {

	int MAX = 33;
	int size;
	int maxLevel;
	Entry<T> head, tail;

	/**
	 * Constructor
	 */
	public SkipList() {
		maxLevel = 0;
		size = 0;
		head = new Entry<>(null, MAX);
		tail = new Entry<>(null, MAX);

		for (int i = 0; i < MAX; i++) {
			head.next[i] = tail;
			head.span[i] = 0;
		}
	}

	/**
	 * Class storing information for each element
	 * 
	 * @param <T>
	 */
	private class Entry<T extends Comparable<? super T>> {
		T element;
		Entry<T>[] next;
		int[] span;

		private Entry(T element, int arraySize) {
			this.element = element;
			this.next = new Entry[arraySize];
			this.span = new int[arraySize];

		}

		@Override
		public String toString() {
			return (this.element == null ? "N" : this.element) + "\t" + Arrays.toString(this.span);
		}

	}

	/**
	 * Add x to list. If x already exists, replace it. Returns true if new node
	 * is added to list
	 * 
	 * @param x
	 * @return
	 */
	public boolean add(T x) {
		Entry<T>[] prev = find(x);
		if (!prev[0].next[0].equals(tail) && prev[0].next[0].element.compareTo(x) == 0) {
			prev[0].next[0].element = x;
			return false;
		} else {
			int lev = chooseLevel();
			// update spans between lev and maxLevel as we are going to add a
			// new node
			if (lev < maxLevel) {
				for (int i = lev; i < maxLevel; i++) {
					prev[i].span[i] = prev[i].span[i] + 1;
				}
			}
			Entry<T> n = new Entry<>(x, lev);
			for (int i = 0; i < lev; i++) {
				if (i == 1) {
					if (prev[i].equals(head)) {
						if (head.next[i].equals(tail)) {
							head.span[i] = count;
							n.span[i] = size - head.span[i];
						} else {
							n.span[i] = head.span[i] - count;
							head.span[i] = count;
						}

					} else {
						n.span[i] = prev[i].span[i] - count;
						prev[i].span[i] = count;
					}
				}

				else if (i > 0) {
					if (prev[i].equals(head)) {
						Entry<T> q = head;
						// use onelevel function to calculate current level span
						// from below level span
						int c = onelevel(q, i - 1, n);
						if (head.next[i].equals(tail)) {
							prev[i].span[i] = c;
							n.span[i] = size - c;
						} else {
							n.span[i] = prev[i].span[i] - c;
							prev[i].span[i] = c;
						}

					} else if (!prev[i].equals(prev[i - 1])) {
						Entry<T> q = prev[i];
						// use onelevel function to calculate current level span
						// from below level span
						int c = onelevel(q, i - 1, n);
						n.span[i] = prev[i].span[i] - c;
						prev[i].span[i] = c;

					} else {
						n.span[i] = prev[i].span[i] - prev[i].span[i - 1];
						prev[i].span[i] = prev[i - 1].span[i - 1];
					}
				}

				n.next[i] = prev[i].next[i];
				prev[i].next[i] = n;
			}
			size++;
			return true;
		}
	}

	/**
	 * just count from given level span-useful for add functionality
	 * 
	 * 
	 * @param x
	 * @return T type
	 */
	int onelevel(Entry<T> q, int level, Entry<T> n) {
		int c = -1;
		while (!q.equals(n)) {
			c = c + q.span[level] + 1;
			q = q.next[level];
		}
		return c;
	}

	/**
	 * Find smallest element that is greater or equal to x
	 * 
	 * @param x
	 * @return T type
	 */
	public T ceiling(T x) {
		Entry<T>[] prev = find(x);
		return prev[0].next[0].equals(tail) ? null : prev[0].next[0].element;
	}

	/**
	 * Does list contain x ?
	 * 
	 * @param x
	 * @return boolean
	 */
	public boolean contains(T x) {
		Entry<T>[] prev = find(x);
		return prev[0].next[0].element.compareTo(x) == 0;
	}

	/**
	 * Return first element of list
	 * 
	 * @return
	 */
	public T first() {
		return head.next[0].equals(tail) ? null : head.next[0].element;
	}

	/**
	 * Find largest element that is less than or equal to x
	 */
	public T floor(T x) {
		Entry<T>[] prev = find(x);
		return prev[0].equals(head) ? null : (prev[0].next[0].element == x ? prev[0].next[0].element : prev[0].element);
	}

	/**
	 * Return element at index n of list. First element is at index 0.
	 */
	public T get(int n) {
		if (n >= size) {
			return null;
		}
		Entry<T> p = head;
		int index = -1;
		for (int i = maxLevel - 1; i >= 0; i--) {
			while (true) {
				index = index + p.span[i] + 1;
				if (index == n) {
					p = p.next[i];
					return p.element;
				}
				if (index > n) {
					index = index - (p.span[i] + 1);
					break;
				}
				p = p.next[i];
			}

		}
		return null;

	}

	/**
	 * Is the list empty?
	 */
	public boolean isEmpty() {
		return (head.next[0].equals(tail));
	}

	/**
	 * Iterate through the elements of list in sorted order
	 */
	public Iterator<T> iterator() {
		return new SLIterator<T>(this);
	}

	private class SLIterator<E extends Comparable<? super E>> implements Iterator<E> {
		SkipList<E> list;
		Entry<E> cursor, prev;
		boolean ready;

		SLIterator(SkipList<E> list) {
			this.list = list;
			cursor = (Entry<E>) list.head;
			prev = null;
			ready = false;
		}

		public boolean hasNext() {
			return !cursor.next[0].equals(tail);
		}

		public E next() {
			if (hasNext()) {

				prev = cursor;
				cursor = cursor.next[0];
				ready = true;
				return cursor.element;
			}
			return null;
		}

		/**
		 * Removes the current element (retrieved by the most recent next())
		 * Remove can be called only if next has been called and the element has
		 * not been removed
		 */
		public void remove() {
			if (!ready) {
				throw new NoSuchElementException();
			}
			list.remove(cursor.element);
			cursor = prev;
			ready = false;

		}
	}

	/**
	 * Return last element of list
	 */
	public T last() {
		return get(size - 1);

	}

	/**
	 * Remove x from list. Removed element is returned. Return null if x not in
	 * list
	 * 
	 * @param x
	 * @return
	 */
	public T remove(T x) {
		Entry<T>[] prev = find(x);
		Entry<T> n = prev[0].next[0];
		if (n.element == null || n.element.compareTo(x) != 0) {
			return null;
		} else {
			for (int i = 0; i <= maxLevel; i++) {
				if (prev[i].next[i].equals(n)) {
					prev[i].span[i] = prev[i].span[i] + n.span[i];
					prev[i].next[i] = n.next[i];

				} else {
					while (i <= maxLevel) {
						prev[i].span[i] = prev[i].span[i] - 1;
						i++;
					}
					break;
				}
			}
			size--;
			return n.element;
		}

	}

	/**
	 * Choose number of levels for a new node randomly Prob(choosing level i) =
	 * 1/2 * Prob(choosing level i − 1)
	 *
	 * @return level (int)
	 */
	private int chooseLevel() {
		Random random = new Random();
		int mask = (1 << maxLevel) - 1;
		int lev = Integer.numberOfTrailingZeros(random.nextInt() & mask) + 1;
		if (lev > maxLevel) {
			return ++maxLevel;
		}
		return lev;
	}

	/**
	 * HELPER FUNCTION
	 */

	/**
	 * Finds the greatest element less than or equal to x
	 * 
	 * @param x
	 *            T type
	 * @return
	 */

	Integer count = 0;// useful for span implementation

	Entry<T>[] find(T x) {
		Entry<T> p = head;
		Entry<T>[] prev = new Entry[maxLevel + 1];
		for (int i = maxLevel; i >= 0; i--) {
			count = 0;
			while (!p.next[i].equals(tail) && p.next[i].element.compareTo(x) < 0) {
				p = p.next[i];
				count = i == 0 ? count + 1 : 0;
			}
			prev[i] = p;
		}
		return prev;
	}

	/**
	 * Return the number of elements in the list
	 * 
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns string containing size and level of the SkipList
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "[" + "maxLevel is " + this.maxLevel + ", " + "No. of elements in the list is " + this.size() + "]";
	}

	/**
	 * Print the elements at each level of SkipList and reports whether Skip
	 * list is valid or not . For each entry it prints element and its span.
	 * span meaning-gap between two nodes at a level
	 */

	public void print() {
		Entry<T> p = head;

		for (int i = 0; i < maxLevel; i++) {
			System.out.println("At level " + i);
			System.out.print("[");
			System.out.print(p.element + "," + "span is " + p.span[i]);
			System.out.print("]" + " ,");
			while (!p.next[i].equals(tail)) {

				p = p.next[i];
				System.out.print("[");
				System.out.print(p.element + "," + "span is " + p.span[i]);
				System.out.print("]" + " ,");

			}
			p = head;
			int c = -1;

			while (i == maxLevel - 1) {
				System.out.println();
				c = c + p.span[i] + 1;
				p = p.next[i];
				if (p.equals(tail)) {
					if (c == size) {
						System.out.println("SkipList is valid");
					} else {
						System.out.println("SkipList is invalid");
					}
					break;
				}
			}

			System.out.println();
		}

	}

	/**
	 * below function will just check whether get(i) functionality is working or
	 * not
	 *
	 * @return
	 */

	void checkgeti() {
		T[] arr = (T[]) new Object[size];
		Entry<T> p = head;
		for (int i = 0; i < size; i++) {
			p = p.next[0];
			arr[i] = p.element;
		}
		for (int i = 0; i < size; i++) {
			if (!arr[i].equals(get(i))) {
				System.out.println("get(n) functionality not working");
				return;
			}
		}
		System.out.println("get(n) functionality working");

	}

	/**
	 * below function will just check whether iterator functionality is working
	 * or not
	 *
	 * @return
	 */
	void checkIterator() {
		T[] arr = (T[]) new Object[size];
		Entry<T> p = head;
		for (int i = 0; i < size; i++) {
			p = p.next[0];
			arr[i] = p.element;
		}
		int i = 0;
		Iterator<T> it = this.iterator();
		while (it.hasNext()) {
			if (!it.next().equals(arr[i++])) {
				System.out.println("Iterator not working");
			}
        }
		System.out.println("Iterator working");

	}

	public static void main(String[] args) {
		SkipList<Integer> sl = new SkipList<>();
		Random r = new Random();
		r.nextInt(500);
		for (int i = 1; i <= 50; i++) {
			sl.add(r.nextInt(50));
		}
		sl.print();
		sl.checkgeti();
		sl.checkIterator();

	}

	/*
	 * Implemented Rebuild but not working perfectly
	 * 
	 */

	/**
	 * Reorganize the elements of the list into a perfect skip list
	 */
	public void rebuild() {

		rebuild(1);
	}

	void rebuild(int level) {
		if (level > maxLevel) {
			return;
		}
		Entry<T> prev = head;
		Entry<T> p = head;

		double gap = Math.pow(2, level);
		int cur = -1;
		while (!p.equals(tail)) {
			if (cur + p.span[level] + 1 == gap) {

				if (p.next[level].equals(tail)) {
					prev.next[level] = tail;
					prev.span[level] = (int) gap;
					break;
				} else {
					prev = p;
					p = p.next[level];
					cur = -1;
				}

			}

			else if (cur + p.span[level] + 1 < gap) {
				samelane(p, cur, gap, level);
			} else {
				// go down
				p = belowlane(prev, p, level - 1, level, cur, gap);
				prev = p;
				cur = -1;

			}

		}
		rebuild(level + 1);

	}

	void samelane(Entry<T> p, int cur, double gap, int b) {
		Entry<T> p1 = p;
		while (!p1.equals(tail)) {
			if (cur + p1.span[b] + 1 == gap) {
				p.next[b] = p1.next[b];
				p.span[b] = (int) gap;
				// p1.next[b] = p.next[b];
				break;
			} else {
				cur = cur + p1.span[b] + 1;
				p1 = p1.next[b];
			}

		}

	}

	Entry<T> belowlane(Entry<T> prev, Entry<T> p, int b, int u, int cur, double gap) {

		Entry<T> p1 = p;

		while (!p1.equals(tail)) {
			if (cur + p1.span[b] + 1 == gap) {
				prev.next[b] = p1;
				prev.span[b] = (int) gap;
				p1.next[b] = p.next[b];
			}

			else {
				cur = cur + p1.span[b] + 1;
				p1 = p1.next[b];
			}
		}
		return p1;

	}

}
