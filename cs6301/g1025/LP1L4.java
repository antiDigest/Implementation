// Starter code for Level 4 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.util.HashMap;
import java.util.Scanner;

public class LP1L4 {

    static Num[] vars = new Num[26];
    static int base;
    HashMap<Integer, Frame> hashmap = new HashMap<>();
    Frame head = null;

    public Frame evaluateLine(String line) throws Exception {
        Frame frame = null;

        if (line.equals(";")) {// for case ;
            return frame;
        }

        if (line.contains("?")) {
            String[] parts = line.split("\\?");

            String left = parts[0];
            String[] leftparts = left.split("\\s+");
            int lineno = Integer.parseInt(leftparts[0]);
            left = leftparts[1].trim();

            String right = parts[1];
            right = right.trim();

            frame = new Frame(lineno, left, right, true, vars);
            hashmap.put(frame.lineno, frame);
        } else if (line.contains("=")) {
            String[] parts = line.split("=");
            String left = parts[0];
            String right = parts[1];

            if (Character.isDigit(left.charAt(0))) {
                left = left.trim();
                String[] leftparts = left.split("\\s+");
                int lineno = Integer.parseInt(leftparts[0].trim());
                left = leftparts[1].trim();

                right = right.trim();

                frame = new Frame(lineno, left, right, false, vars);
                hashmap.put(frame.lineno, frame);
            } else {
                right = right.replace(';', ' ').trim();
                frame = new Frame(null, left, right, false, vars);
            }
        } else {
            line = line.replace(';', ' ').trim();
            frame = new Frame(null, line, null, false, vars);
        }

        return frame;
    }

    void evaluate(Scanner in) throws Exception {
        Frame prev = head;
        String line = null;
        while (!(line = in.nextLine()).equals(";")) {
            if (prev == null) {
                head = this.evaluateLine(line);
                prev = head;
            } else {
                prev.next = this.evaluateLine(line);
                prev = prev.next;
            }
        }
    }

    void executeAll(Frame head) throws Exception {
        Frame frame = head;
        Frame prev = frame;

        int point;
        while (frame != null) {
            point = frame.execute(vars);
            if (point == -1) {
                prev = frame;
                frame = frame.next;
            } else {
                frame = hashmap.get(point);
            }
        }

        vars[prev.variable - 97].printList();
    }

    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 0) {
            base = Integer.parseInt(args[0]);
            // Use above base for all numbers (except I/O, which is in base 10)
        }

        in = new Scanner(System.in);
        LP1L4 x = new LP1L4();
        x.evaluate(in);
        x.executeAll(x.head);

        return;
    }
}
