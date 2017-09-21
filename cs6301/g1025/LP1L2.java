// Sample code for Level 2 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.math.BigInteger;

public class LP1L2 {
    public static void main(String[] args) throws Exception {
        String a = "18";
        String b = "4";
        //System.out.println("A: "+a.length()+", B: "+b.length());

        BigInteger b1 = new BigInteger(a);
        BigInteger b2 = new BigInteger(b);
        Num x = new Num(a);
        Num y = new Num(b);
        Num z = Num.divide(x, y);

        System.err.println(z);
        System.out.println(b1.divide(b2).toString());

        int val = z.toString().compareTo(b1.divide(b2).toString());
        String out = val == 0 ? "PASS" : "FAIL";
        System.out.println(out);
        // z.printList();

    }
}
