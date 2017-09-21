/**
 * Fibonacii numbers in linear and logarithmic running time
 *
 * @author: gunjan
 *
 * Short project 4: 18 September, 2017
 *
 */
package cs6301.g1025;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
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

    private static Map<Integer, BigInteger> map = new HashMap<>();


    static BigInteger linearFibonacci(int n){

        if(n == 0 || n == 1){
            return BigInteger.ONE;
        }

        if(map.containsKey(n)){
            return map.get(n);
        }

        BigInteger b = linearFibonacci(n-2).add(linearFibonacci(n-1));
        map.put(n, b);
        return b;
    }

    static BigInteger logFibonacci(int n){

        BigInteger A[][] = new BigInteger[][]{{BigInteger.ONE,BigInteger.ONE},{BigInteger.ONE,BigInteger.ZERO}};
        //    int B[][] = new int[][]{{0,0},{0,0}};
        int c[] = new int[]{1,0};

        if(n == 0){
            return BigInteger.ZERO;
        }
        power(A, n-1);
        // multiply(B, c);

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
        for(int i = 0; i < n; i++){
            System.out.println(logFibonacci(i));
        }
    }
}

