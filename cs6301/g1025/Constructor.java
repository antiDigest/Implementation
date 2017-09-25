package cs6301.g1025;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import cs6301.g00.Timer;

public class Constructor {

	public static void main(String[] args) throws Exception {
		StringBuilder a = new StringBuilder();

		int aCount = 20;

		Random r = new Random();
		Timer t = new Timer();
		a.append(ThreadLocalRandom.current().nextInt(1, 10));

		for (int i = 1; i < aCount; i++) {
			a.append(r.nextInt(10));
		}

		String s = a.toString();

		t.start();
		Num x = new Num(s);
		t.end();

		System.out.println("Constructor: " + t);
		int val = x.toString().compareTo(a.toString());
		String out = val == 0 ? "PASS" : "FAIL";
		System.out.println(out);
		
		//System.err.println(x);
		//System.err.println(s);
	}

}
