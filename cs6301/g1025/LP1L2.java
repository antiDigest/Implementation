// Sample code for Level 2 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.math.BigInteger;

public class LP1L2 {
    public static void main(String[] args) {
    	String a = "434343434233874324234234324324324324324234234324234234234324324234343243242367683433434324343434234234242342342344";
    	String b = "21474836533";//17//2147483648
    	BigInteger b1 = new BigInteger(a);
    	BigInteger b2 = new BigInteger(b);
        Num x = new Num(a);
        Num y = new Num(b);
        Num z = Num.divide(x, y);
        int val = z.toString().compareTo(b1.divide(b2).toString());
        String out = val == 0 ? "PASS" : "FAIL";
        System.out.println(out);
        z.printList();
    }
}
