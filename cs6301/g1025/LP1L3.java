// Starter code for Level 3 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.util.Scanner;
import java.util.Stack;

import static javafx.application.Platform.exit;

public class LP1L3 {


    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        LP1L3 x = new LP1L3();
        char lastVariable = 0;
        Num[] vars = new Num[26];
        while (in.hasNext()) {
            String line = in.nextLine();
            if (line.equals(";")) {
                System.out.println(line);
                if (lastVariable != 0) {
                    vars[lastVariable - 97].printList();
                }
                break;
            }
            char result = x.evaluateLine(line, vars, 10);

            if (result == ' ') {
                if (lastVariable != 0) {
                    vars[lastVariable - 97].printList();
                }
                break;
            } else {
                lastVariable = result;
            }

        }
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
    public boolean isDigit(char c) {
        return (c >= 0 && c <= 9);
    }

    /**
     * Evaluate each line:
     * Print when ; found
     *
     * @param line
     * @param vars
     * @param base
     * @return
     * @throws Exception
     */
    char evaluateLine(String line, Num vars[], long base) throws Exception {

        line = line.trim();

        String[] lines = line.split("=");
        String left = lines[0];
        String right = lines[1];

        left = left.trim();
        if (isLetter(left.charAt(0))) {
            char variable = left.charAt(0);

            right = right.replace(';', ' ').trim();
            if (!right.matches("[0-9]+")) {
                vars[variable - 97] = evaluatePostfix(right, vars);
            } else {
                if (right.matches("[0-9]+")) {
                    vars[variable - 97] = new Num(right, base);
                }
            }
            System.out.println(vars[variable - 97]);
            return variable;
        }

        return ' ';
    }

    static Integer itemsRequired(Character op) {
        switch (op) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '^':
                return 2;
            case '|':
                return 1;
            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    static boolean isOperator(Character token) {
        switch (token) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '^':
            case '|':
                return true;
            default:
                return false;
        }
    }

    static Num evalOperator(Character op, Num first, Num second) throws Exception {

        Num r = new Num();
        switch (op) {
            case '+':
                return r.add(first, second);
            case '-':
                return r.subtract(first, second);
            case '*':
                return r.product(first, second);
            case '/':
                return r.divide(first, second);
            case '^':
                return r.power(first, second);
            case '%':
                return r.mod(first, second);

            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    static Num evalOperator(Character op, Num first) {
        Num r = new Num();

        switch (op) {
            case '|':
                return r.squareRoot(first);
            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    public Num evaluatePostfix(String postfix, Num[] vars) throws Exception {
        Stack<Num> stack = new Stack<Num>();

        char[] post = postfix.toCharArray();

        for (int i = 0; i < postfix.length(); i++) {
            Character token = post[i];
            if (token == ' ')
                continue;
            if (isLetter(token)) {
                if (vars[token - 97] == null)
                    throw new Error("Cannot find symbol: " + token);
                Num num = vars[token - 97];
                stack.push(num);
            } else if (isOperator(token)) {
                if (itemsRequired(token) == 2) {
                    Num item1 = stack.pop();
                    Num item2 = stack.pop();
                    stack.push(evalOperator(token, item1, item2));
                } else {
                    Num item = stack.pop();
                    stack.push(evalOperator(token, item));
                }
            }
        }

        return stack.pop();
    }

}

