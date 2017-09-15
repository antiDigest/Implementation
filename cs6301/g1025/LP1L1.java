// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

public class LP1L1 {
    public static void main(String[] args) {
        Num x = new Num("2");
        System.out.println(x);
        Num y = new Num("2");
        System.out.println(y);
        Num w = Num.product(x, y);
        w.printList();
//        System.out.println(Num.product(x, y));
        Num.convertBase(10, x, 2);
//		Num a = Num.power(x, 8);
//		System.out.println(a);
//		z.printList();
//    	x = new Num(25);
//		y = new Num(25);
//		Num z = Num.add(x, y);
//		Num w = Num.product(x, y);
//		Num t = Num.power(x, 4);
//		t.printList();
//		w.printList();
//		z.printList();
	}
}
