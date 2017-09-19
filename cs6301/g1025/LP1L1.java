// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.math.BigInteger;

import cs6301.g00.Timer;

public class LP1L1 {
    public static void main(String[] args) {
        Num x = new Num("6000");
        Num y = new Num("5400");
        
        BigInteger a = new BigInteger("6000");
        BigInteger b = new BigInteger("5400");
        
        
        Timer timer = new Timer();
        timer.start();
        Num z = Num.divide(x, y );
        timer.end();
        System.out.println("Num class: "+ timer);
        System.out.println();
        
        timer.start();
        BigInteger c = a.divide(b);
        timer.end();
        System.out.println("BigInteger: " + timer);
        
        System.out.println(c);
        x.printList();
        y.printList();
        z.printList();
        System.out.println(z);
        //System.out.println(c.toString().compareTo(z.toString()));
    }
}
