/**
 * @author Swaroop, Antriksh
 * Array Based Queues
 * Ver 1.0: 2017/09/09
 * * Ver 1.1: 2017/09/10: Resizing added
 */

package cs6301.g1025;

import javax.naming.SizeLimitExceededException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayQueue<T> implements Iterable<T> {
    int size;
    T[] queue; //actual queue
    int head = 0;
    int tail = 0;

    public ArrayQueue(int n) {
        this.size = n;
        this.queue = (T[]) new Object[n];
    }

    /**
     * Resizes the queue on demand
     */
    public void resize() {
        int head = this.head;
        int tail = this.tail;
        int n = this.queue.length;
        int length = Math.abs(tail - head) + 1;

        if (head==tail){
            return;
        }

        System.out.println(n + " " + length );

        T[] temp = this.queue;
        if ((double) (length / n) >= 0.90) {
            this.queue = (T[]) new Object[n * 2];
            this.size = n * 2;
        } else if ((double) (length / n) <= 0.25) {
            this.queue = (T[]) new Object[n / 2];
            this.size = n / 2;
        }

        int k = 0;
        int min = (head <= tail) ? head : tail;
        if (min == tail) {
            for (int i = head; i < this.size; i++) {
                this.queue[k++] = temp[i];
            }
            for (int i = 0; i <= tail; i++) {
                this.queue[k++] = temp[i];
            }
        } else {
            for (int i = head; i <= tail; i++) {
                this.queue[k++] = temp[i];
            }
        }
        this.head = 0;
        this.tail = k - 1;

    }


    /**
     * Add element to queue
     *
     * @param elem: Element to be added
     * @return
     * @throws IllegalStateException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws SizeLimitExceededException
     */
    public boolean add(T elem) throws IllegalStateException, ClassCastException, NullPointerException,
            SizeLimitExceededException {
        if (this.queue[tail] != null || (tail - head) > size - 1 ||
            (tail < head && size - (tail - 1) - (head - 1) < 0)) {
            throw new SizeLimitExceededException("Queue size limit exceeded, please resize the queue");
        } else {
            this.queue[tail] = elem;
            this.tail = tail == size - 1 ? 0 : this.tail + 1;
        }
        return true;
    }

    /**
     * Shows the element at the pointer position
     *
     * @return element: queue head
     */
    public T peek() {
        return this.queue[head];
    }

    /**
     * returns and removes head element
     *
     * @return head element
     * @throws IllegalStateException
     */
    public T pop() throws IllegalStateException {
        if (isEmpty()) throw new NoSuchElementException("Underflow Exception");
        T elem = this.queue[head];
        this.queue[head] = null;
        this.head = head == size - 1 ? 0 : this.head + 1;
        return elem;
    }

    /**
     * Similar to add but does not throw exception on parallel execution
     *
     * @param elem: Element to be added
     * @return added: true/false
     * @throws IllegalStateException
     * @throws SizeLimitExceededException
     */
    public boolean offer(T elem) throws IllegalStateException, SizeLimitExceededException {
        if (isEmpty()) return false;
        else if (this.queue[tail] != null || (tail - head) > size - 1 || (tail < head && size - (tail - 1) - (head - 1) < 0)) {
            return false;
        } else {
            queue[tail] = elem;
            this.tail = tail == size - 1 ? 0 : this.tail + 1;
            return true;
        }
    }

    /**
     * Similar to pop but does not throw exception on parallel execution
     *
     * @return head element
     */
    public T poll() {
        if (isEmpty()) return null;
        T elem = this.queue[head];
        this.queue[head] = null;
        this.head = head == size - 1 ? 0 : this.head + 1;
        return elem;
    }

    public boolean isEmpty() {
        return peek() == null;
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) throws SizeLimitExceededException, IllegalStateException, ClassCastException, NullPointerException {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(10);

//        System.out.println(q.peek());
        q.add(1);
        q.add(2);
        q.add(3);
        q.offer(4);
        q.add(5);
        q.add(6);

//        System.out.println(q.pop());

        System.out.println(q.size);
        q.add(7);
        q.offer(8);
//        System.out.println(q.offer(9));
        q.offer(9);
        System.out.println(q.size);
        System.out.println(q.tail - q.head + 1);
        q.resize();
        System.out.println(q.size);
        q.pop();
        q.pop();
        q.pop();
        q.pop();
        q.pop();
        q.pop();
        q.pop();
        q.pop();
        q.resize();
        System.out.println(q.size);
        q.peek();
//        System.out.println(q.pop());
//        System.out.println(q.pop());
        q.add(7);
//        System.out.println(q.pop());
//        System.out.println(q.pop());
//        System.out.println(q.pop());
//        System.out.println(q.pop());
//        System.out.println(q.peek());
//        System.out.println(q.peek());
        q.add(10);
        q.offer(11);
        q.offer(12);
        q.offer(13);
        q.offer(14);
//        System.out.println(q.peek());
//        System.out.println(q.pop());
//        System.out.println(q.pop());
//        System.out.println(q.peek());
    }

}