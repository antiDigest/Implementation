// Sample code for Level 1 driver for lp1

// Change following line to your group number
package LongProject1;

import java.math.BigInteger;

public class LP1L1 {
//    public static void main(String[] args) {
//	Num x = new Num(10965,8);
//	//Num y=new Num(100,10);
//	//y.printList();
//	//System.out.println(x);
//	//Num z=new Num(23);
//	//Num res=x.product(x,z);
//    	Num p1=new Num("-1234444444132334",20);
//    	p1.printList();
//    	
//    	System.out.println(p1);
//	//Num p2=new Num("100457348",20);
//	
// 	//Num r=p1.product(p1,p2);
//    //r.printList();
//  Num d= p1.convertBase(p1,45);
//   d.printList();
//System.out.println(d);
////	System.out.println(d);
//	//Num y = new Num("8");
//	//Num y = new Num(999999999);
//	//Num z = Num.add(x, y);
//	//z.printList();
//	
//    }


    public static void main(String[] args) {
        Num x = new Num(2);
        x=x.power(x, 2^33);
        Num y = new Num("5000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000543534566543535252624362465462456542626262666666666666666");
        //Num z=x.divide(y, x);
      //  System.out.println(z);
        BigInteger a = new BigInteger("3");
        BigInteger b = new BigInteger("4");
        
        Num l=new Num("12",2);
        System.out.println(l);
        
        //Timer timer = new Timer();
      //  timer.start();
      
      //  timer.end();
       // System.out.println("Num class: "+ timer);
        System.out.println();
        
       // timer.start();
        BigInteger c = a.multiply(b);
      //  timer.end();
      //  System.out.println("BigInteger: " + timer);
        
        System.out.println(c);
       // System.out.println(z);
        //System.out.println(c.toString().compareTo(z.toString()));
    }
}