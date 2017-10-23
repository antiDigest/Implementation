
package cs6301.g1025;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import cs6301.g00.Timer;

public class SplayDriver {

	public static void main(String[] args) throws IOException {

		Timer timer = new Timer();
		timer.start();

		timer.end();
		// TODO Auto-generated method stub
		SplayTree t = new SplayTree();
		TreeSet j = new TreeSet();
		FileWriter wr = new FileWriter("Comparison.txt");

		wr.write("\n");
		
		for (int i = 0; i < 1000000; i++) {
			int randomNum = ThreadLocalRandom.current().nextInt(1, 4);
			Integer x = ThreadLocalRandom.current().nextInt(-5000000, 5000000);
			if (randomNum == 1) {
				wr.write("Add");
				wr.write("\n");
				timer.start();
				t.add(x);
				timer.end();
				write(wr,"SplayTree :",timer);
				timer.start();
				j.add(x);
				timer.end();
				write(wr,"JavaTree :",timer);
				wr.write("_______________________________________________________________");
				wr.write("\n");
			} else if (randomNum == 2) {
				wr.write("Remove");
				wr.write("\n");
				timer.start();
				t.remove(x);
				timer.end();
				write(wr,"SplayTree :",timer);
				timer.start();
				j.remove(x);
				timer.end();
				write(wr,"JavaTree :",timer);
				wr.write("_______________________________________________________________");
				wr.write("\n");
			} else {
				wr.write("Remove");
				wr.write("\n");
				timer.start();
				t.contains(x);
				timer.end();
				write(wr,"SplayTree :",timer);
				timer.start();
				j.contains(x);
				timer.end();
				write(wr,"JavaTree :",timer);
				wr.write("_______________________________________________________________");
				wr.write("\n");
				
			}
		}
		wr.flush();
		wr.close();
	}

	static void write(FileWriter wr, String head, Timer timer) throws IOException {
	
			wr.write(head);
			wr.write("  ");
			wr.write(timer.toString());
			wr.write("\n");
			

		

	}

}
