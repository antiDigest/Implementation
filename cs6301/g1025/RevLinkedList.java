/**
 * @author Gunjan
 * Reversal of linked list
 * Ver 1.0: 2017/09/10
 * Ver 1.1: 2017/09/10: Now extends Singly Linked List class
 */

package cs6301.g1025;

import cs6301.g1025.utils.SinglyLinkedList;
import java.util.NoSuchElementException;

public class RevLinkedList<T> extends SinglyLinkedList<T> {

    public void revListNonRecursive(){
        this.head.next = this.revListNonRecursive(this.head);
    }
    /**
     * Non recursive method to reverse a singly linked list
     * head of SLL passed as parameter
     * returns a node element as the first element after the dummy header
     *
     * @param head
     * @return node
     */
    public Entry<T> revListNonRecursive(Entry<T> head) {


        if (head == null || head.next == null) return head;

        Entry<T> node = head.next;
        RevLinkedList.Entry<T> prev = null;
        RevLinkedList.Entry<T> curr = node;
        RevLinkedList.Entry<T> nxt;

        while (curr != null) {
            /**
             * Loop invariant: List node returned by method points to the
             * first element at the front of reversed linked list
             * prev: points to the first node of the reversed linked list
             * curr: points to the first node in the right-most elements
             * left in the old linked list
             */
            nxt = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nxt;
        }

        node = prev;
        return node;

    }

    public void revListRecursive(){
        this.head.next = this.revListRecursive(this.head.next);
    }
    /**
     * Recursive method to perform reversal of SLL
     * takes parameter the first element in the list after dummy header
     * returns list node 'left'
     *
     * @param curr
     * @return left
     */
    public Entry<T> revListRecursive(Entry<T> curr) {

        if (curr == null || curr.next == null) return curr;

        Entry<T> tmp = curr.next;
        curr.next = null;
        Entry<T> left = revListRecursive(tmp);
        tmp.next = curr;
        return left;
    }

    public static void main(String[] args) throws NoSuchElementException {
        //reverse r = new reverse();
        int n1 = 20;
        int n2 = 15;
        if (args.length > 0) {
            n1 = Integer.parseInt(args[0]);
        }

        RevLinkedList<Integer> lst = new RevLinkedList<>();
        RevLinkedList<Integer> lst2 = new RevLinkedList<>();

        for (int i = 1; i <= n1; i++) {
            lst.add(new Integer(i));
        }

        for (int i = 1; i <= n2; i++) {
            lst2.add(new Integer(i));
        }

        System.out.println("The first list is: ");
        lst.printList();
        System.out.println("The first list reversed using a recursive method is:");
        lst.revListRecursive();
        lst.printList();
        System.out.println("The second list is: ");
        lst2.printList();
        System.out.println("The second list reversed using a non recursive method is:");
        lst2.revListNonRecursive();
        // lst.unzip();
        lst2.printList();
    }
}
