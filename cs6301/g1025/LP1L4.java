// Starter code for Level 4 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.util.HashMap;
import java.util.Scanner;

public class LP1L4 {

    static Num[] vars = new Num[26];
    static int base;
    HashMap<Integer, Frame> hashmap = new HashMap<>();

    public String evaluateLine(String line) throws Exception {

        if (line.contains("?")) {
            String[] parts = line.split("\\?");

            String left = parts[0];
            String[] leftparts = left.split("\\s+");
            int lineno = Integer.parseInt(leftparts[0]);
            left = leftparts[1].trim();

            String right = parts[1];
            right = right.trim();

            Frame frame = new Frame(lineno, left, right, true, vars);
            hashmap.put(frame.lineno, frame);
            return String.valueOf(frame.goTo(vars));
        } else if (line.contains("=")) {
            String[] parts = line.split("=");
            String left = parts[0];
            String right = parts[1];

            if (Frame.isDigit(left.charAt(0))) {
                String[] leftparts = left.split("\\s+");
                int lineno = Integer.parseInt(leftparts[0]);
                left = leftparts[1].trim();

                right = right.trim();

                Frame frame = new Frame(lineno, left, right, false, vars);
                hashmap.put(frame.lineno, frame);
                return "next";
            } else {
                char variable = left.charAt(0);

                right = right.replace(';', ' ').trim();
                if (!right.matches("[0-9]+")) {
                    vars[variable - 97] = ShuntingYard.evaluatePostfix(right, vars);
                } else {
                    if (right.matches("[0-9]+")) {
                        vars[variable - 97] = new Num(right, base);
                    }
                }
                return String.valueOf(variable);
            }
        }

        return " ";
    }

    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 0) {
            base = Integer.parseInt(args[0]);
            // Use above base for all numbers (except I/O, which is in base 10)
        }

        in = new Scanner(System.in);
        LP1L4 x = new LP1L4();

        char lastVariable = 0;
        while (in.hasNext()) {
            String line = in.nextLine();
            // TODO: parse words for level 4
            String result = x.evaluateLine(line);
            if (result.equals(" ")) {
                if (lastVariable != 0) {
                    vars[lastVariable - 97].printList();
                }
                break;
            } else if (Frame.isLetter(result.charAt(0)) && result.length() == 1) {
                lastVariable = result.charAt(0);
            } else if (result.matches("[0-9]+")) {
                System.out.println(result);
            }
        }
    }
}
