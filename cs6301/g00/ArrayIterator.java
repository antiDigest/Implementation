/**
 * Iterator for arrays
 *
 * @author rbk
 * Ver 1.0: 2017/08/08
 * Ver 1.1: 2017/08/28.  Updated some methods to public
<<<<<<< HEAD
 */
=======
*/
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900

package cs6301.g00;

import java.util.Iterator;
<<<<<<< HEAD

public class ArrayIterator<T> implements Iterator<T> {
    T[] arr;
    int startIndex, endIndex, cursor;

    ArrayIterator(T[] a) {
        arr = a;
        startIndex = 0;
        endIndex = a.length - 1;
        cursor = -1;
    }

    ArrayIterator(T[] a, int start, int end) {
        arr = a;
        startIndex = start;
        endIndex = end;
        cursor = start - 1;
    }

    public boolean hasNext() {
        return cursor < endIndex;
    }

    public T next() {
        return arr[++cursor];
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
=======
import java.lang.UnsupportedOperationException;

public class ArrayIterator<T> implements Iterator<T> {
	T[] arr;
	int startIndex, endIndex, cursor;

	ArrayIterator(T[] a) {
		arr = a;
		startIndex = 0;
		endIndex = a.length - 1;
		cursor = -1;
	}

	ArrayIterator(T[] a, int start, int end) {
		arr = a;
		startIndex = start;
		endIndex = end;
		cursor = start - 1;
	}

	public boolean hasNext() {
		return cursor < endIndex;
	}

	public T next() {
		return arr[++cursor];
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
>>>>>>> 00b8023c27308018abd7772bd8e4893683704900
}
