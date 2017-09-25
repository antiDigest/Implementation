// Starter code for Level 3 driver for lp1

// Change following line to your group number
package cs6301.g1025;

import java.util.Scanner;

public class LP1L3 {


    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        LP1L3 x = new LP1L3();
        char lastVariable = 0;
        Num[] vars = new Num[26];
        while (in.hasNext()) {
			String line = in.nextLine();
			if (line.indexOf("=") == -1 && !line.equals(";")) {// for case var;
				System.out.println(vars[line.charAt(0) - 97]);
			} else if(line.equals(";")){// for case ;
				     
					if (lastVariable != 0) {
						vars[lastVariable - 97].printList();
					}
					break;
				} else {
					char result = x.evaluateLine(line, vars);// for case var=expression;
					if(result!=' ')
					lastVariable = result;
				}
			}

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
    char evaluateLine(String line, Num vars[]) throws Exception {
        String[] lines = line.split("=");
        String left = lines[0];
        String right = lines[1];

        left = left.trim();
        if (Character.isLetter(left.charAt(0))) {
            char variable = left.charAt(0);

            right = right.replace(';', ' ').trim();
            if (!right.matches("[0-9]+")) {
                vars[variable - 97] = ShuntingYard.evaluatePostfix(right, vars);
            } else {
                if (right.matches("[0-9]+")) {
                    vars[variable - 97] = new Num(right);
                }
            }
            System.out.println(vars[variable - 97]);
            return variable;
        }
        else return ' ';

        
    }

}

