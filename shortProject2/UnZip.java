package shortProject2;

import shortProject2.SinglyLinkedList;

public class UnZip<T extends Comparable<? super T>> extends SinglyLinkedList<T> {

	public void unzip(int k) {
		
		
		//space complexity is o(k) and time complexity is o(n) where n is size of the list)
		if (size <=k) { 
			return;
		}

		Entry<T> main = this.head;
		Entry<T>[] arr = (Entry<T>[]) new Entry[k];

		Entry<T> c = main.next;
		int l = 0;

		//create  states
		while (c != null && l < k){ 
			arr[l] = c;
            c=c.next;
            if(l+1==k)
            arr[l].next=null;
            else{
            	arr[l].next=c;
            }
			l++;

		}
		//save heads of states
		Entry<T>[] arr1 = (Entry<T>[]) new Entry[k];
		for (int i = 0; i < arr.length; i++) {
			arr1[i] = arr[i];
		}

		int state = 0;
		
		//traverse the heads of states
		while (c != null) {

			arr[state].next = c;
			arr[state] = c;
			c = c.next;
			//link the next state head
			if (state+1 != k)
				arr[state].next = arr1[state+1];
			
			//for the last state link null
			if (state+1 == k) {
				arr[state].next=null;
				state = 0;
			}
			else{
				state=state+1;
			}
		}
		this.head.next = arr1[0];
	}

	public static void main(String[] args) {
		UnZip<Integer> list = new UnZip<Integer>();

		int[] arr = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		for (int i = 0; i < arr.length; i++) {
			list.add(new Integer(arr[i]));
		}
		list.printList();
		list.unzip(4);

		list.printList();

	}

}
