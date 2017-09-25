package cs6301.g1025;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Frame {
    Integer lineno;
    char variable;
    boolean condition;
    int gotoTrue = -1;
    int gotoFalse = -1;
    boolean print = false;
    boolean evaluate = false;
    String right;
    Frame next = null;

    Frame(Integer no, String left, String right, boolean condition, Num[] vars) throws Exception {
//        System.out.println(no + " " + left + " " + right + " " + condition);
        this.lineno = no;
        this.variable = left.charAt(0);
        if (right == null) {
            this.print = true;
            return;
        }
        this.condition = condition;
        right = right.replace(';', ' ').trim();
        if (right.matches("[0-9]+") && !condition) {
            vars[this.variable - 97] = new Num(right);
            this.right = right;
            return;
        }
        if (!condition) {
            if (!ShuntingYard.checkPostfix(right)) {
                this.right = ShuntingYard.shuntingYard(right, vars);
                this.evaluate = true;
            }
            else {
                this.right = right.replaceAll(" ", "");
            }
        } else {
            this.right = right;
            this.evaluate = true;
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

    public int execute(Num[] vars) throws Exception {
        if (this.evaluate)
            if (!this.condition)
                vars[this.variable - 97] = ShuntingYard.evaluatePostfix(this.right, vars);
            else
                return this.goTo(vars);
        else if (this.print)
            System.out.println(vars[this.variable - 97]);
        return -1;
    }
}
