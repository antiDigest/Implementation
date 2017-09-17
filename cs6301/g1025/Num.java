// Starter code for lp1.

// Change following line to your group number
// Changed type of base to long: 1:15 PM, 2017-09-08.
package cs6301.g1025;

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
			long next = nextInt(it);
			while (next > -1) {
				if (k > 0) {
					first.num.add(next);
					k--;
				} else {
					second.num.add(next);
				}
				next = nextInt(it);
			}

		}
	}

	static long TEN = 10; // This can be changed to what you want it to

	// To check base
	long base = 10; // Change as needed

	public LinkedList<Long> num;
	boolean sign = false;

	/**
	 * Start of Constructors
	 */

	Num(String s) {
		int n = 0;
		num = new LinkedList<Long>();
		Character token;
		if (s != "")
			try {
				token = s.charAt(n);
				if (token == '-') {
					sign = true;
					n++;
				}
				for (int i = n; i < s.length(); i++) {
					token = s.charAt(i);
					if (Tokenizer.tokenize(token.toString()) == Tokenizer.Token.NUM) {
						this.num.addFirst(Long.parseLong(token.toString())); // Added
																				// in
																				// base
																				// 10
					} else {
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Java Unhandled Exception: " + e.getMessage());
			}
	}

	// constructor for initialising base with string input
	Num(String s, long base) {
		this(s);
		this.base = base;
	}

	// constructor for initialising base with long input
	Num(long x, long base) {
		this(x);
		this.base = base;
	}

	// Constructor for Num of type long
	Num(long x) {
		this();
		if (x < 0) {
			sign = true;
		}
		x = Math.abs(x);
		if (x == 0) {
			this.num.add(x);
		} else {
			while (x > 0) {
				long digit = x % base;
				this.num.add(digit);
				x /= base;
			}
		}
	}

	// constructor added to create an empty num class
	Num() {
		num = new LinkedList<Long>();
	}

	/**
	 * End of Constructors
	 */

	/**
	 * Start of Level 1 operation functions: add xor subtract product
	 */

//	static Num xor(Num a, Num b) {
//		Num res = new Num();
//
//		Iterator<Long> ita = a.num.iterator();
//		Iterator<Long> itb = b.num.iterator();
//
//		long borrow = 0;
//		long diff;
//		while (ita.hasNext() || itb.hasNext() || borrow > 0) {
//			diff = next(ita) - next(itb) - borrow;
//			borrow = 0;
//			if (diff < 0) {
//				diff += res.base;
//				borrow = 1;
//			}
//			if (!(!itb.hasNext() && diff == 0))
//				res.num.add(diff);
//		}
//
//		return res;
//	}

	static Num subtract(Num a, Num b) {
		if (a.sign ^ b.sign) //opp sign
			return unsignedAdd(a, b);
		 else
			return subHelper(a, b);

	}
	
	static Num subHelper(Num a, Num b){
		if(a.num.size() > b.num.size())
			return unsignedSubtract(a, b, false);
		else if(a.num.size() < b.num.size())
			return unsignedSubtract(b, a, true);
		else{
			if(a.compareTo(b) >= 0){// a >= b hence, (a - b) or -(a - b), sign is of a
				return unsignedSubtract(a, b, a.sign);
			}else{ // b > a hence -(b - a) or (b - a) sign is !sign of b//inverse
				return  unsignedSubtract(b, a, !b.sign);
			}
		}
	}

	/**
	 *Always abs(a) >= abs(b) sign is set based on the previous method's input
	 */
	static Num unsignedSubtract(Num a, Num b, boolean sign) {
		Num res = new Num();
		Iterator<Long> ita = a.num.iterator();
		Iterator<Long> itb = b.num.iterator();
		long borrow = 0l;
		long diff;
		while (ita.hasNext() || itb.hasNext() || borrow > 0) {
			diff = next(ita) - next(itb) - borrow;
			borrow = 0l;
			if (diff < 0) {
				diff += res.base;
				borrow = 1 ;
			}
			if (!(!itb.hasNext() && diff == 0) || (diff == 0))
				res.num.add(diff);
		}
		res.sign = sign; 
		return res;
	}
	
	//Done
	static Num unsignedAdd(Num a, Num b) {
		Num res = new Num();
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
	//Done
	static Num add(Num a, Num b) {
		if (!(a.sign ^ b.sign)) {// if both signs are same, xor will be false
			return unsignedAdd(a, b);
		} else
			return a.sign ? subtract(b, a) : subtract(a, b);
	}

//	static Num subtractOld(Num a, Num b) {
//		Num res;
//		if (!a.sign) {
//			if (b.sign) {
//				b.sign = false;
//				return add(a, b);
//			} else {
//				if (a.compareTo(b) <= 0) {
//					res = xor(b, a);
//					res.sign = true;
//					return res;
//
//				} else {
//					res = xor(a, b);
//					return res;
//				}
//			}
//		} else if (a.sign) {
//			a.sign = false;
//			if (!b.sign) {
//				res = add(a, b);
//				res.sign = true;
//				return res;
//			} else {
//				b.sign = false;
//				res = add(a, b);
//				res.sign = !res.sign;
//				return res;
//			}
//		} else
//			return null;
//	}

	private static Num product(Num n, long b) {
		Iterator<Long> it = n.num.iterator();
		long carry = 0l;
		Num res = new Num();
		res.base = n.base;
		// long next = nextInt(it);
		while (it.hasNext() || carry > 0) {
			long sum = (next(it) * b + carry);
			res.num.add(sum % res.base);
			carry = sum / res.base;
		}
		return res;
	}

	// Implement Karatsuba algorithm for excellence credit
	static Num product(Num a, Num b) {
		if (size(a) >= size(b)) {
			return karatsubaProduct(a, b);
		} else
			return karatsubaProduct(b, a);
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
	static Num karatsubaProduct(Num a, Num b) {
		if (size(b) == 1) {
			Num res = product(a, b.num.getFirst());
			return res;
		} else if (size(a) == 0 || size(b) == 0) {
			Num res = a;
			res.num.add(0l);
			return res;
		} else if (size(a) >= size(b)) {
			return karatsubaSplit(a, b);
		} else
			return karatsubaSplit(b, a);
	}

	static Num karatsubaSplit(Num a, Num b) {

		int k = (int) size(b) / 2;
		Num part1 = new Num();

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

		Num aHbHProd = karatsubaProduct(aH, bH);
		part1.num = new LinkedList<>(aHbHProd.num);
		leftShift(part1, 2 * k);

		Num part3 = karatsubaProduct(aL, bL);

		Num aLaHSum = add(aL, aH);
		Num bLbHSum = add(bL, bH);
		Num aLHbLHProd = karatsubaProduct(aLaHSum, bLbHSum);
		Num part2 = subtract(subtract(aLHbLHProd, aHbHProd), part3);
		leftShift(part2, k);
		Num res = add(add(part1, part2), part3);
		res.sign = (a.sign ^ b.sign);
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
	// static Num divide(Num a, Num b) {
	// Num start = new Num(1);
	// Num end = a;
	// Num mid = null;
	//
	// return mid;
	// }
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
		if(ret < 0) return new Num(0l, a.base);
		else if(ret == 0) {
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
		Num start = new Num(1);
		Num end = a;
		Num mid = null;
		while (start.compareTo(end) <= 0) {
			mid = add(start, end);
			rightShift(mid);
			if (product(mid, mid).compareTo(a) <= 0
					&& product(add(mid, new Num(1)), add(mid, new Num(1))).compareTo(a) > 0) {
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
	 * CompareTo implemented from Comparable
	 *
	 * @param other:
	 *            Num
	 * @return Num
	 */
//	public int compareTo(Num other) {
//		if (!this.sign && other.sign) {
//			return -1;
//		} else if (!this.sign && other.sign) {
//			return 1;
//		} else {
//			return traverseToCompare(this, other);
//		}
//	}
	
	public int compareTo(Num other) {
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
		Iterator<Long> it = this.num.iterator();
		String sum = "";
		long next = nextInt(it);
		while (next > -1) {
			sum = (int) (next) + sum;
			next = nextInt(it);
		}
		return (this.sign ? '-' : "") + sum;
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

	static Num convertBase(long baseA, Num a, long baseB) {
		// TODO
		// String value = "";
		Num res = new Num("");
		// Num B = new Num(baseB);
		// res.sign = a.sign;
		// res.base = 2;
		// int n = 0;
		// for (Long item : a.num) {
		// while (item != 0) {
		//// long x = mod(item, B);
		// res.num.add(x);
		// item = item / baseB;
		// }
		// }
		return res;
	}
}
