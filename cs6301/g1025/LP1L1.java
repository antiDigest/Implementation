// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

public class LP1L1 {
    public static void main(String[] args) {
	Num x = new Num(999);
	Num y = new Num("8");
	Num z = Num.add(x, y);
	System.out.println(z);
	Num a = Num.power(x, 8);
	System.out.println(a);
	z.printList();
    }
}
