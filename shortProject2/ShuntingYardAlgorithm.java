package shortProject2;

import java.util.*;

public class ShuntingYardAlgorithm {

	
	
	public static void main(String[] args) {
	     ShuntingYardAlgorithm s = new ShuntingYardAlgorithm();
	        System.out.println("Infix: 3 * 4 * 5 * 6");
	        System.out.println("Postfix: " + s. infixToPostfix("3 * 4 * 5 * 6"));

	        s = new ShuntingYardAlgorithm();
	        System.out.println("Infix: 3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3");
	        System.out.println("Postfix: " + s. infixToPostfix("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3"));

	        s = new ShuntingYardAlgorithm();
	        System.out.println("Infix: 3 + 4 * 2 * ( 5! ) + 2 / ( 1 - 5 ) ^ 2 ^ 3");
	        System.out.println("Postfix: " + s. infixToPostfix("3 + 4 * 2 * ( 5! ) + 2 / ( 1 - 5 ) ^ 2 ^ 3"));

	        s = new ShuntingYardAlgorithm();
	        System.out.println("Infix: 3 * 5^2 - 6^4 + ( 9! ) + 3!");
	        System.out.println("Postfix: " + s. infixToPostfix("3 * 5^2 - 6^4 + ( 9! ) + 3!"));
	    
    }
	// rules:
	// //* Parenthesized expressions (...)
	// * Unary operator: factorial (!)
	// * Exponentiation (^), right associative.
	// * Product (*), division (/). These operators are left associative.
	// * Sum (+), and difference (-). These operators are left associative.

	private static boolean isHigerPrecedence(String token, String operator, HashMap<String, Integer> rules) {
		if (rules.containsKey(operator) && rules.get(token) > rules.get(operator)) {
			return true;

		} else if (rules.containsKey(operator) && token.equals("^")&&operator.equals(token)
				&& rules.get(token) == rules.get(operator)) {
			return true;
		} else {
			return false;
		}

	}

	public static String infixToPostfix(String exp) {
		StringBuilder result = new StringBuilder();
		Deque<String> stack = new LinkedList<>();
		HashMap<String, Integer> rules = new HashMap<String, Integer>();

		rules.put("+", 1);
		rules.put("-", 1);
		rules.put("*", 2);
		rules.put("/", 2);
		rules.put("^", 3);// right associative
		rules.put("!", 4);
		rules.put("(",5);
	
		for (int i=0;i<exp.length();i++) {
String token=exp.charAt(i)+"";
			if (token.isEmpty()) {
				continue;
			}
			if (rules.containsKey(token)) {

				if (!stack.isEmpty() && isHigerPrecedence(token, stack.peek(), rules)) {
					stack.push(token);
				}
				
				else {

					while (!stack.isEmpty()&&!isHigerPrecedence(token, stack.peek(), rules)&& !stack.peek().equals("(")) {
					   result.append(stack.pop());
						
					}
					stack.push(token);
				}
			} else if (token.equals("(")) {
				stack.push(token);

			} else if (token.equals(")")) {
				while (!stack.isEmpty() &&!stack.peek().equals("(")){
					result.append(stack.pop());}
				stack.pop();

				
			} else {
				result.append(token);
			}
		}

		while (!stack.isEmpty())
			result.append(stack.pop());

		return result.toString();
	}

}