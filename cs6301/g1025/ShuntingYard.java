/**
 * Class to process infix to postfix using shunting yard
 *  @author swaroop, saikumar, antriksh, gunjan
 *
 */
package cs6301.g1025;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ShuntingYard {

    private static Stack<Character> stack = new Stack<Character>();
    private static Queue<Character> q = new LinkedList<Character>();

    /**
     * Shunting Yard Algorithm
     * Converts infix expressions to postfix expressions using the operator-precedence algorithm
     * or the Shunting-Yard algorithm.
     *
     * @param infix: Input infix expression.
     * @return String "postfix" expression.
     */
    public static String shuntingYard(String infix, Num[] vars) {
        String prefix = "";
        char[] infixExp = infix.toCharArray();
        for (int i = 0; i < infix.length(); i++) {
            Character token = infixExp[i];
            if (token == ' ')
                continue;
            if (Character.isDigit(token)) {
                q.add(token);
            } else if (Character.isLetter(token)) {
                q.add(token);
            } else if (token == '(') {
                stack.push(token);
            } else if (token == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    q.add(stack.pop());
                }
                if (stack.isEmpty())
                    throw new IllegalArgumentException("Parenthesis do not match Error");
                else if (stack.peek() == '(')
                    stack.pop();
            } else if (isOperator(token)) {
                if (!stack.isEmpty()) {
                    Character top = stack.peek();
                    if (precedent(top, token)) {
                        q.add(stack.pop());
                    }
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty() && isOperator(stack.peek())) {
            q.add(stack.pop());
        }
        for (Character s : q) {
            prefix += s.toString() + " ";
        }
        return prefix;
    }

    public static boolean checkPostfix(String anyfix) {
        char[] fix = anyfix.toCharArray();

        if (anyfix.matches("[0-9]+")) {
            return false;
        }

        int count = 0;
        for (char token : fix) {
            if (token == ' ')
                continue;
            if (isOperator(token)) {
                count -= itemsRequired(token);
            } else if (!isOperator(token)) {
                count++;
            }
            if (count < 0) {
                return false;
            }
        }
        return true;
    }


    public static Num evaluatePostfix(String postfix, Num[] vars) throws Exception {
        Stack<Num> stack = new Stack<Num>();

        char[] post = postfix.toCharArray();

        for (int i = 0; i < postfix.length(); i++) {
            Character token = post[i];
            if (token == ' ')
                continue;
            if (Character.isLetter(token)) {
                if (error(token, vars)) {
                    throw new Error("Cannot find symbol: " + token);
                }
                Num num = vars[token - 97];
                stack.push(num);
            } else if (Character.isDigit(token)) {
                Num num = new Num(token.toString());
                stack.push(num);
            } else if (isOperator(token)) {
                if (itemsRequired(token) == 2) {
                    Num item1 = stack.pop();
                    Num item2 = stack.pop();
                    stack.push(evalOperator(token, item2, item1));
                } else {
                    Num item = stack.pop();
                    stack.push(evalOperator(token, item));
                }
            }
        }

        return stack.pop();
    }

    /**
     * All Utility functions go after this line
     */

    /**
     * Defining operator precedence
     * <p>
     * Parenthesized expressions (...)
     * Unary operator: Square Root (|)
     * Exponentiation (^), right associative.
     * Product (*), division (/), mod (%).  These operators are left associative.
     * Sum (+), and difference (-).  These operators are left associative.
     *
     * @param op: Operator
     * @return Integer value of precedence level.
     */
    static int precedenceLevel(Character op) {
        switch (op) {
            case '+':
            case '-':
                return 0;
            case '*':
            case '/':
            case '%':
                return 1;
            case '^':
                return 2;
            case '|':
                return 3;
            case '(':
                return -1;
            default:
                throw new IllegalArgumentException("Operator unknown: " + op);
        }
    }

    /**
     * Defining Operator Associativity
     * <p>
     * Exponentiation (^), right associative.
     * Product (*), division (/).  These operators are left associative.
     * Sum (+), and difference (-).  These operators are left associative.
     *
     * @param op: Operator
     * @return String, "left" or "right" associative
     */
    static String associativity(Character op) {
        switch (op) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
                return "left";
            case '^':
                return "right";
            default:
                throw new IllegalArgumentException("Operator unknown: " + op);
        }
    }

    /**
     * Operator Precedence Check
     * @param top:   stack top item
     * @param token: token read from input
     * @return top has higher precedence than token: true or false
     */
    static boolean precedent(Character top, Character token) {
        int precedenceTop = precedenceLevel(top);
        int precedenceToken = precedenceLevel(token);

        if (top == token) {
            if (associativity(top) == "left")
                return true;
            else
                return false;
        } else if (precedenceTop == precedenceToken) {
            if (associativity(top) == "left")
                return true;
            else
                return false;
        }
        return (precedenceTop > precedenceToken);
    }

    /**
     * Operator check
     * Check if the input token is an operator.
     * @param token: input token
     * @return true or false
     */
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

    /**
     * Items Required
     * Returning items required to perform the operation using the input operator
     *
     * @param op: input operator
     * @return Integer 2/1
     * @throws IllegalArgumentException
     */
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

    static Num evalOperator(Character op, Num first, Num second) throws Exception {

        switch (op) {
            case '+':
                return Num.add(first, second);
            case '-':
                return Num.subtract(first, second);
            case '*':
                return Num.product(first, second);
            case '/':
                return Num.divide(first, second);
            case '^':
                return Num.power(first, second);
            case '%':
                return Num.mod(first, second);

            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    static Num evalOperator(Character op, Num first) {
        

        switch (op) {
            case '|':
                return Num.squareRoot(first);
            default:
                throw new IllegalArgumentException("Operator not found.");
        }
    }

    static boolean error(char c, Num[] vars) {
        return (vars[c - 97] == null) ? true : false;
    }

}
