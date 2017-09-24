package cs6301.g1025;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Frame {
    int lineno;
    char variable;
    boolean condition;
    int gotoTrue = -1;
    int gotoFalse = -1;
    String right;

    Frame(int no, String left, String right, boolean condition, Num[] vars) {
        this.lineno = no;
        if (error(left.charAt(0), vars)) {
            throw new Error("Cannot find symbol: " + left.charAt(0));
        }
        this.variable = left.charAt(0);
        this.condition = condition;
        right = right.replace(';', ' ').trim();
        if (!condition)
            this.right = ShuntingYard.shuntingYard(right, vars);
        else {
            this.right = right;
            if (right.contains(":")) {
                right = right.replaceAll("\\s+", "");
                String[] rightparts = right.split(":");

                gotoTrue = Integer.parseInt(rightparts[0]);
                gotoFalse = Integer.parseInt(rightparts[1]);
            } else {
                gotoTrue = Integer.parseInt(right);
            }
        }
    }

    public String toString() {
        return this.lineno + " " + this.variable + (condition ? " ? " : " = ") + this.right;
    }

    public int goTo(Num[] vars) {
        return (vars[this.variable - 97].compareTo(Num.ZERO) != 0) ? gotoTrue : gotoFalse;
    }

    /**
     * Checks the character for letter or not
     */
    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z');
    }

    /**
     * Checks the character for digit or not
     */
    public static boolean isDigit(char c) {
        return (c >= 0 && c <= 9);
    }

    boolean error(char c, Num[] vars) {
        return (vars[c - 97] == null) ? true : false;
    }
}
