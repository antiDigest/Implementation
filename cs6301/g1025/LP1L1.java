// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

public class LP1L1 {
    public static void main(String[] args) {
        Num x = new Num("-400");
        Num y = new Num("200");
//        System.out.println(Num.subtract(x, y));
//        System.out.println(x.compareTo(y));
//        System.out.println(x);
//        System.out.println(Num.add(x, y));
        System.out.println(Num.karatsubaProduct(x, y));
        System.out.println(Num.subtract(x, y));
        //System.out.println(Num.product(x, y));
    }
}
