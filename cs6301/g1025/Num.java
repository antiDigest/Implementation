// Starter code for lp1.




// Change following line to your group number
// Changed type of base to long: 1:15 PM, 2017-09-08.
package cs6301.g1025;



// Starter code for lp1.

// Change following line to your group number

import java.util.Iterator;
import java.util.LinkedList;




public class Num implements Comparable<Num> {

	/**
	 * Helper class Split to split for Karatsuba product
	 */
	class Split {
		Num first;
		Num second;

		Split() {
			first = new Num();
			second = new Num();
		}

		public void split(Num n, int k) {
			first.base = n.base;
			second.base = n.base;
			Iterator<Long> it = n.num.iterator();
			while (it.hasNext()) {
				if (k > 0) {
					first.num.add(next(it));
					k--;
				} else {
					second.num.add(next(it));
				}
			}
		}
	}

	static long defaultBase = (long) Math.pow(2, 32); // This can be changed to what you want it to
	// be.
	long base = defaultBase; // Change as needed

	// To check base
	// long base = 20; // Change as needed

	public LinkedList<Long> num;
	boolean sign = false;

	/**
	 * Start of Constructors
	 */

	// constructor for initializing input string to default base
	public Num(String s) {
		this(s, defaultBase);

	}

	// constructor for initializing input string with given base
	Num(String s, long base) {
		this();
		this.base = base;
		Num res = new Num();
		res.base = base;
		Num baseW = new Num(10, base);
		Character token;
		char[] string = s.toCharArray();
		int n = 0;
		if (s != "")
			try {
				token = string[n];
				if (token == '-') {
					this.sign = true;
					n++;
				}
				for (int i = n; i < s.length(); i++) {
					token = string[i];
					if (Tokenizer.tokenize(token.toString()) == Tokenizer.Token.NUM) {
						res = add(product(res, baseW), new Num(Integer.parseInt(string[i]+"")));
					} else {
						break;
					}
				}
				this.num = res.num;

			} catch (Exception e) {
				System.out.println("Java Unhandled Exception: " + e.getMessage() + " at Value: " + s);
			}
	}

	// constructor for initialising base with long input
	Num(long x, long base) {
		this();
		this.base = base;
		if (x < 0) {
			sign = true;
		}
		x = Math.abs(x);
		if (x == 0) {
			this.num.add(x);
		} else {
			while (x > 0) {
				long digit = x % this.base;
				this.num.add(digit);
				x /= this.base;
			}
		}

	}

	// Constructor for Num of type long
	Num(long x) {
		this(x, defaultBase);
	}

	// constructor added to create an empty num class
	Num() {
		num = new LinkedList<Long>();
	}

	/**
	 * End of Constructors
	 */

	/**
	 * Start of Level 1 operation functions: add, subtract, product
	 */

	/**
	 * Difference of two signed big integers
	 * 
	 * @param a:
	 *            Num
	 * @param b:
	 *            Num
	 * @return: Num (a - b)
	 */
	public static Num subtract(Num a, Num b) {
		if (a.sign ^ b.sign) // opp sign
			return unsignedAdd(a, b);
		else
			return a.unsignedCompareTo(b) <= 0 ? unsignedSubtract(b, a, !b.sign) : unsignedSubtract(a, b, a.sign);

	}

	/**
	 * Difference of two unsigned big integers Always abs(a) >= abs(b), sign is
	 * set based on the previous method's input
	 */
	static Num unsignedSubtract(Num a, Num b, boolean sign) {
		Num res = new Num("",a.base);
		
		Iterator<Long> ita = a.num.iterator();
		Iterator<Long> itb = b.num.iterator();
		long borrow = 0l;
		long diff;
		while (ita.hasNext() || itb.hasNext() || borrow > 0) {
			diff = next(ita) - next(itb) - borrow;
			borrow = 0l;
			if (diff < 0) {
				diff += res.base;
				borrow = 1;
			}
			if (!(!itb.hasNext() && diff == 0) || (diff == 0))
				res.num.add(diff);
		}
		res.trim();
		sign(res, sign);
		return res;
	}

	static void sign(Num a, boolean sign){
		a.sign = size(a) == 1 ? (a.num.peek() == 0 ? false : sign) : sign;
	}

	void trim() {
		while (this.num.peekLast() == 0) {
			if (size(this) == 1)
				return;
			else
				this.num.removeLast();
		}
	}

	/**
	 * Sum of two unsigned big integers
	 */
	static Num unsignedAdd(Num a, Num b) {
		Num res = new Num("",a.base);
		long carry = 0l;
		Iterator<Long> ita = a.num.iterator();
		Iterator<Long> itb = b.num.iterator();

		long sum = 0;
		while (ita.hasNext() || itb.hasNext() || carry > 0) {
			sum = next(ita) + next(itb) + carry;
			res.num.add(sum % res.base);
			carry = sum / res.base;
		}
		res.sign = a.sign;
		return res;
	}

	/**
	 * Sum of two signed big integers
	 * 
	 * @param a:
	 *            Num
	 * @param b:
	 *            Num
	 * @return: Num - a + b
	 */
	public static Num add(Num a, Num b) {
		if (!(a.sign ^ b.sign)) {// if both signs are same, xor will be false
			return unsignedAdd(a, b);
		} else
			return a.unsignedCompareTo(b) <= 0 ? unsignedSubtract(b, a, b.sign) : unsignedSubtract(a, b, a.sign);
	}

	/**
	 * Product of a Num and long
	 * 
	 * @param n
	 *            Num
	 * 
	 * @param b
	 *            long
	 * 
	 *            return Num a*b
	 */
	private static Num product(Num n, long b) {
		if(b == 0) return new Num(0l, n.base);
		Iterator<Long> it = n.num.iterator();
		long carry = 0l;
		Num res = new Num("",n.base);
		
		while (it.hasNext() || carry > 0) {
			long sum = (next(it) * b + carry);
			res.num.add(sum % res.base);
			carry = sum / res.base;
		}
		return res;
	}

	/**
	 * Karatsuba Product Num * Num RT = O(n^log3)
	 *
	 * @param a:
	 *            Num
	 * @param b:
	 *            Num
	 * @return: Num - a*b
	 */
	public static Num product(Num a, Num b) {
		Num res = new Num();
		if (size(b) == 1) {
			res = product(a, b.num.getFirst());
		} else if (size(a) == 1) {
			res = product(b, a.num.getFirst());
		} else if (size(a) == 0 || size(b) == 0) {
			res = new Num("",a.base);
		} else if (size(a) >= size(b)) {
			res = karatsubaSplit(a, b);
		} else {
			res = karatsubaSplit(b, a);
		}
		sign(res, (a.sign ^ b.sign));
		return res;
	}

	static Num karatsubaSplit(Num a, Num b) {

		int k = (int) size(b) / 2;
		Num part1 = new Num("",a.base);

		// Splitting a to aL and aH
		Split split = a.new Split();
		split.split(a, k);
		Num aL = split.first;// first k bits;
		Num aH = split.second;// a.size - k bits;\

		// Splitting b to bL and bH
		split = b.new Split();
		split.split(b, k);
		Num bL = split.first;// first k bits;
		Num bH = split.second;// b.size - k bits;

		Num aHbHProd = product(aH, bH);
		part1.num = new LinkedList<>(aHbHProd.num);
		leftShift(part1, 2 * k);

		Num part3 = product(aL, bL);

		Num aLaHSum = add(aL, aH);
		Num bLbHSum = add(bL, bH);
		Num aLHbLHProd = product(aLaHSum, bLbHSum);
		Num part2 = subtract(subtract(aLHbLHProd, aHbHProd), part3);
		leftShift(part2, k);
		Num res = add(add(part1, part2), part3);

		return res;
	}

	// Use divide and conquer

	/**
	 * a^n, power function
	 *
	 * @param a:
	 *            Num
	 * @param n:
	 *            Long
	 * @return Num - a^n
	 */
	static Num power(Num a, long n) {

		if (n == 0)
			return new Num(1);
		else if (n == 1)
			return a;
		else {
			Num div1 = power(a, n / 2);
			Num res = product(div1, div1);
			if (n % 2 == 1) {
				return product(res, a);
			} else
				return res;
		}
	}

	/* End of Level 1 */

	/* Start of Level 2 */

	/**
	 * Divide operation - uses binary search
	 *
	 * @param a:
	 *            Num
	 * @param b:
	 *            Num
	 * @return Num - quotient of a/b
	 */

	static long search(long a, Num b) {
		long start = 0;
		long end = a;
		long mid = 0;
		while (start < end) {
			mid = (start + end) >>> 1;
			Num midNum = new Num(mid);
			Num aNum = new Num(a);
			if (product(midNum, b).compareTo(aNum) <= 0 && product(new Num(mid + 1), b).compareTo(aNum) > 0) {
				return mid;
			} else if (product(midNum, b).compareTo(aNum) > 0) {
				end = mid;
			} else if (product(midNum, b).compareTo(aNum) < 0) {
				start = mid;
			}
		}
		return mid;
	}

	static Num divide(Num a, Num b) {
		int ret = a.compareTo(b);
		Num quotient = new Num();
		if (ret < 0)
			return new Num(0l, a.base);
		else if (ret == 0) {
			quotient.num.add(1l);
			quotient.sign = a.sign ^ b.sign;
			return quotient;
		}

		long borrow = 0;
		long rem = 0;
		long q;
		while (a.num.peekLast() != null || borrow > 0) {
			long ai = last(a);
			rem = borrow * a.base + ai;
			q = search(rem, b);
			borrow = rem - Long.parseLong(product(b, q).toString());
			quotient.num.addFirst(q);
		}
		return quotient;
	}

	/**
	 * mod function
	 *
	 * @param a:
	 *            Num
	 * @param b:
	 *            Num
	 * @return: Num - remainder of a/b
	 */
	static Num mod(Num a, Num b) {
		Num res = subtract(a, product(divide(a, b), b));
		return res;
	}

	// Use divide and conquer
	static Num power(Num a, Num n) {
		Iterator<Long> itn = n.num.iterator();
		long n0 = nextInt(itn);
		if (size(n) == 1)
			return power(a, n0);
		rightShift(n);
		return product(power(power(a, n), a.base), power(a, n0));
	}

	/**
	 * Square Root function
	 *
	 * @param a
	 *            Num
	 * @return Num - a^(1/2)
	 */
	static Num squareRoot(Num a) {
        Num start = new Num(0);
        Num end = a;
        Num mid = new Num();
        while (start.compareTo(end)<=0) {
            mid = divide(add(start, end), new Num(2));
            if (product(mid, mid).compareTo(a) <= 0 &&
                    product(add(mid, new Num(1)), add(mid, new Num(1))).compareTo(a) > 0) {
                return mid;
            } else if (product(mid, mid).compareTo(a) > 0) {
                end = mid;
            } else if (product(mid, mid).compareTo(a) < 0) {
                start = mid;
            }
        }
        return mid;
	}

	/* End of Level 2 */

	// Utility functions
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise

	/**
	 * CompareTo implemented from Comparable, (unsigned compare)
	 *
	 * @param other:
	 *            Num
	 * @return Num
	 */
	public int unsignedCompareTo(Num other) {
		if (size(this) < size(other)) {
			return -1;
		} else if (size(this) > size(other)) {
			return 1;
		} else {
			return traverseToCompare(this, other);
		}
	}

	/**
	 * Output using the format "base: elements of list ..." For example, if
	 * base=100, and the number stored corresponds to 10965, then the output is
	 * "100: 65 9 1"
	 */
	void printList() {
		Iterator<Long> iterator = num.iterator();
		System.out.print(base + ": ");
		while (iterator.hasNext())
			System.out.print(iterator.next() + " ");
		System.out.print("\n");
	}

	// Return number to a string in base 10
	public String toString() {
		Num targetBase = convertBase(this, 10);

		StringBuilder sb = new StringBuilder();

		Iterator<Long> it = targetBase.num.iterator();
		long digit = nextInt(it);
		while (digit != -1) {
			sb.insert(0, digit);
			digit = nextInt(it);
		}
		return (this.sign ? '-' : "") + sb.toString();

	}

	public long base() {
		return this.base;
	}

	/**
	 * Now follow all the helper functions: nextInt next size traverseToCompare
	 * leftShift rightShift convertBase
	 */

	static long nextInt(Iterator<Long> it) {
		return it.hasNext() ? it.next() : -1;
	}

	static long next(Iterator<Long> it) {
		return it.hasNext() ? it.next() : 0;
	}

	static long last(Num a) {
		return (a.num.peekLast() != null) ? a.num.removeLast() : 0;
	}

	static long getLast(Num a) {
		return (a.num.peekLast() != null) ? a.num.getLast() : 0;
	}

	static long size(Num a) {
		return a.num.size();
	}

	/**
	 * Compares two nums by traversing from the back and returns the comparison
	 * of two numbers
	 */
	static int traverseToCompare(Num a, Num b) {
		Iterator<Long> ita = a.num.descendingIterator();
		Iterator<Long> itb = b.num.descendingIterator();
		while (ita.hasNext()) {
			Long ai = next(ita);
			Long bi = next(itb);
			if (ai.compareTo(bi) != 0) {
				return ai.compareTo(bi);
			}
		}
		return 0;
	}

	static void leftShift(Num n, int k) {
		while (k > 0) {
			n.num.addFirst(0l);
			k--;
		}
	}

	static void leftShift(Num n) {
		n.num.addFirst(0l);
	}

	static void rightShift(Num n) {
		n.num.removeFirst();
	}

	static void rightShift(Num n, int k) {
		while (k > 0) {
			n.num.removeFirst();
			k--;
		}
	}

	static Num convertBase(Num a, long baseB) {
		Iterator<Long> it = a.num.iterator();
		long baseA = a.base;

		Num res= convertbaseHelper(new Num(baseA, baseB), it, nextInt(it), baseB);
		res.sign=a.sign;
		return res;

	}

	static Num convertbaseHelper(Num A, Iterator<Long> it, long digit, long baseB) {

		if (digit == -1) {
			return new Num("", baseB);

		}

		return add(product(convertbaseHelper(A, it, nextInt(it), baseB), A), new Num(digit, baseB));

	}

	@Override
	public int compareTo(Num other) {

		if (!this.sign && other.sign) {
			return -1;
		} else if (!this.sign && other.sign) {
			return 1;
		} else {
			if (size(this) != size(other))
				return size(this) < size(other) ? -1 : 1;
			else
				return traverseToCompare(this, other);
		}

	}
}
