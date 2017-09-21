// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.math.BigInteger;

import cs6301.g00.Timer;

public class LP1L1 {
    public static void main(String[] args) {

        Num x = new Num("-1000000000000");
        Num y = new Num("-0");
        
        BigInteger a = new BigInteger("3");
        BigInteger b = new BigInteger("4");
        
    
        Timer timer = new Timer();
        timer.start();
        Num z = Num.product(x, y );
        timer.end();
        System.out.println("Num1 class: "+ timer);
        System.out.println();

        timer.start();
        BigInteger c = a.multiply(b);
        timer.end();
        System.out.println("BigInteger: " + timer);

        System.out.println(c);
        System.out.println(z);

//        System.out.println(false ^ true);
        //System.out.println(c.toString().compareTo(z.toString()));
    }
}
