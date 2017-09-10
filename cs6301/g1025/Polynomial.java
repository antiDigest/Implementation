package cs6301.g1025;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Polynomial {

    static class Tuple implements Comparable<Tuple> {
        final Integer coefficient;
        final Integer exponent;

        Tuple(Integer coefficient, Integer exponent) {
            this.coefficient = coefficient;
            this.exponent = exponent;
        }

        public String toString() {
            return "(" + this.coefficient + ", " + this.exponent + ")";
        }

        public int compareTo(Tuple other) {
            if (this.exponent < other.exponent) {
                return -1;
            } else if (this.exponent > other.exponent) {
                return 1;
            } else return 0;
        }
    }

    static <T> T next(Iterator<T> it) {
        return (it.hasNext() ? it.next() : null);
    }

    public Polynomial() {
    }

    public static List<Tuple> readPolynomial(Scanner in) {

        int lines = in.nextInt();

        List<Tuple> poly = new LinkedList<>();
        for (int i = 0; i < lines; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            poly.add(new Polynomial.Tuple(a, b));
        }
        return poly;
    }

    public void add(List<Tuple> poly1, List<Tuple> poly2, List<Tuple> outPoly) {
        Iterator<Tuple> it1 = poly1.iterator();
        Iterator<Tuple> it2 = poly2.iterator();

        Tuple item1 = next(it1);
        Tuple item2 = next(it2);

        while (item1 != null && item2 != null) {
            if (item1.compareTo(item2) < 0) {
                outPoly.add(item1);
                item1 = next(it1);
            } else if (item1.compareTo(item2) > 0) {
                outPoly.add(item2);
                item2 = next(it2);
            } else {
                Integer coeff = item1.coefficient + item2.coefficient;
                if (coeff > 0) outPoly.add(new Tuple(coeff, item1.exponent));
                item1 = next(it1);
                item2 = next(it2);
            }
        }
        while (item1 != null) {
            outPoly.add(item1);
            item1 = next(it1);
        }
        while (item2 != null) {
            outPoly.add(item2);
            item2 = next(it2);
        }
    }

    public void multiply(List<Tuple> poly1, List<Tuple> poly2, List<Tuple> outPoly) {
        return;
    }

    public double evaluate(List<Tuple> poly1, Integer x) {

        double sum = 0;
        for (Tuple item : poly1) {
            sum += (item.coefficient * Math.pow(x, item.exponent));
        }
        return sum;
    }

    public static void main(String[] args) throws FileNotFoundException {

        int evens = 0;
        Scanner in1, in2;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in1 = new Scanner(inputFile);
            inputFile = new File(args[1]);
            in2 = new Scanner(inputFile);
        } else {
            throw new FileNotFoundException("Input requires two files for two polynomials");
        }

        List<Tuple> poly1 = readPolynomial(in1);
        List<Tuple> poly2 = readPolynomial(in2);
        System.out.println(poly1);
        System.out.println(poly2);

        List<Tuple> outPoly = new LinkedList<>();
        Polynomial poly = new Polynomial();
        poly.add(poly1, poly2, outPoly);
        System.out.println(outPoly);

        System.out.println(poly.evaluate(outPoly, 5));
    }
}
