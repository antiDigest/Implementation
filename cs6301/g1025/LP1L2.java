/**
 * Class to execute level 2 methods
 *  @author swaroop, saikumar, antriksh, gunjan
 *
 */
package cs6301.g1025;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import cs6301.g00.Timer;

public class LP1L2 {

    public static void main(String[] args) throws Exception {
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        int aCount = 5;
        int bCount = 4;
        Random r = new Random();
        
        a.append(ThreadLocalRandom.current().nextInt(1, 10));
        for(int i=1; i< aCount; i++){
        	a.append(r.nextInt(10));
        }
        b.append(ThreadLocalRandom.current().nextInt(1, 10));
        for(int i=1; i< bCount; i++){
        	b.append(r.nextInt(10));
        }

        BigInteger b1 = new BigInteger(a.toString());
        BigInteger b2 = new BigInteger(b.toString());
        Num x = new Num(a.toString());
        Num y = new Num(b.toString());
        System.out.println(a);
        System.out.println(b);
        System.out.println(x);
        System.out.println(y);
        
        System.out.println("Timer Started");
        Timer t = new Timer();
        t.start();
        Num z = Num.add(x, y);
        t.end();
        System.out.println(t);
        //t.start();
        BigInteger b4 = new BigInteger(z.toString());
        BigInteger b3 = b4.multiply(b4);
        //t.end();
        //System.out.println(t);

        int val2 = (int)Math.sqrt(923493423);
        int val = 2;//z.toString().compareTo(Integer.toString(val2));
        String out = val == 0 ? "PASS" : val == 2 ? "NOT ASSERTED" : "FAIL";
        System.out.println(out);
        
        System.out.println(z);
        //System.err.println(val2);

    }

}
