// Sample code for Level 2 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.math.BigInteger;
import java.util.Random;

import cs6301.g00.Timer;

public class LP1L2 {

    public static void main(String[] args) throws Exception {
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        int aCount = 10;
        int bCount = 5;
        Random r = new Random();
        for(int i=0; i< aCount; i++){
        	a.append(r.nextInt(10));
        }
        
        for(int i=0; i< bCount; i++){
        	b.append(r.nextInt(10));
        }
        
//        BigInteger b1 = new BigInteger(a.toString());
//        BigInteger b2 = new BigInteger(b.toString());
        Num x = new Num(a.toString());
        Num y = new Num(b.toString());
        

        
        Timer t = new Timer();
        t.start();
        //Num z = Num.divideBy2NEW(x);
        //Num q = new Num("556", 20);
        Num z = Num.power(x, y);
        //Num k = Num.power(x, Long.parseLong(y.toString()));
        //boolean w = Num.odd(q);
        //System.out.println(z);
        
        t.end();
        System.out.println(t);
        
        //t.start();
        //BigInteger b3 = b1.divide(b2);
        //t.end();
        //System.out.println(t);
        
        //int val = z.compareTo(k);
        //System.out.println(val);
        //int val = z.toString().compareTo(b3.toString());
        //String out = val == 0 ? "PASS" : val == 2 ? "NOT ASSERTED" : "FAIL";
        //System.out.println(out);
        
//        System.err.println(z);
//        System.out.println(b3);

    }

}
