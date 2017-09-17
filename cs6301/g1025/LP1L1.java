// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.math.BigInteger;

public class LP1L1 {
    public static void main(String[] args) {
        Num x = new Num("123456");
        Num y = new Num("44444");
        Num z = Num.power(x, y );
        //BigInteger a = new BigInteger("1234567891234567891234567891234567891234567890");
        //BigInteger b = new BigInteger("9876543219876543219876543219876543219876543210");
        //BigInteger c = a.pow(11);
        //System.out.println(c);
        System.out.println(z);
        //System.out.println(c.toString().compareTo(z.toString()));
    }
}
