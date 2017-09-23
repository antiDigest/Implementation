/**
 * Fibonacii numbers in linear and logarithmic running time
 *
 * @author: gunjan
 *
 * Short project 4: 18 September, 2017
 *
 */
package cs6301.g1025;

import cs6301.g00.Timer;

import java.math.BigInteger;
import java.util.Scanner;


public class Fibonacci {
    /**
     *
     * @param n : int
     *
     * @return BigInteger
     *
     */
    static BigInteger exponentialFibonacci(int n){

        if(n == 0 || n == 1){
            return BigInteger.ONE;
        }
        else
            return exponentialFibonacci(n-2).add(exponentialFibonacci(n-1));
    }


    static BigInteger linearFibonacci(int n){

        BigInteger[] fib = new BigInteger[n + 1];
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }

        return fib[n];
    }

    static BigInteger logFibonacci(int n){

        BigInteger A[][] = new BigInteger[][]{{BigInteger.ONE,BigInteger.ONE},{BigInteger.ONE,BigInteger.ZERO}};
        int c[] = new int[]{1,0};

        if(n == 0){
            return BigInteger.ZERO;
        }
        power(A, n-1);

        return A[0][0];
    }

    public static void power(BigInteger[][] A, int n){

        if(n == 0 || n == 1){
            return;
        }

        BigInteger[][] B = new BigInteger[][]{{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}};
        power(A, n/2);
        multiply(A, A);
        if(n % 2 != 0){
            multiply(A, B);
        }
    }

    public static void multiply(BigInteger[][] P, BigInteger[][] Q){

        BigInteger i = P[0][0].multiply(Q[0][0]).add(P[0][1].multiply(Q[1][0]));
        BigInteger j = P[0][0].multiply(Q[0][1]).add(P[0][1].multiply(Q[1][1]));
        BigInteger k = P[1][0].multiply(Q[0][0]).add(P[1][1].multiply(P[1][0]));
        BigInteger l = P[1][0].multiply(Q[0][1]).add(P[1][1].multiply(Q[1][1]));

        P[0][0] = i;
        P[0][1] = j;
        P[1][0] = k;
        P[1][1] = l;

    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the value for n: ");
        int n = sc.nextInt();
        Timer t = new Timer();

        t.start();
        BigInteger logn = logFibonacci(n);
        t.end();
        System.out.println("Running O(logn) Fibonacci");
        System.out.println(t);

        t.start();
        BigInteger linearn = linearFibonacci(n);
        t.end();
        System.out.println("Running O(n) Fibonacci");
        System.out.println(t);

        System.out.println((logn.compareTo(linearn) == 0) ? "PASS" : "FAIL");

        System.out.println(logn);
        System.out.println(linearn);
    }
}

