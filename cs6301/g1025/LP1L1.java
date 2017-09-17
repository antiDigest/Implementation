// Sample code for Level 1 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.math.BigInteger;

import cs6301.g00.Timer;

public class LP1L1 {
    public static void main(String[] args) {
        Num x = new Num("1239086231487532401960927341097456");
        Num y = new Num("100");
        
        BigInteger a = new BigInteger("1239086231487532401960927341097456");
        BigInteger b = new BigInteger("100");
        
        
        Timer timer = new Timer();
        timer.start();
        Num z = Num.power(x, y );
        timer.end();
        System.out.println("Num class: "+ timer);
        System.out.println();
        
        timer.start();
        BigInteger c = a.pow(100);
        timer.end();
        System.out.println("BigInteger: " + timer);
        
        System.out.println(c);
        System.out.println(z);

        //System.out.println(c.toString().compareTo(z.toString()));
    }
}
