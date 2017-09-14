// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

public class LP1L1 {
	public static void main(String[] args) {
		Num x = new Num(25);
		Num y = new Num(25);
		Num z = Num.add(x, y);
		Num w = Num.product(x, y);
		Num t = Num.power(x, 4);
		t.printList();
		w.printList();
		z.printList();
	}
}
