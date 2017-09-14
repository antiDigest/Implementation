// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

public class LP1L1 {
    public static void main(String[] args) {
        Num x = new Num("9087");
        System.out.println(x);
        Num y = new Num("9086");
        System.out.println(y);
        System.out.println(Num.subtract(x, y));
        Num.convertBase(10, x, 2);
//		Num a = Num.power(x, 8);
//		System.out.println(a);
//		z.printList();
    }
}
