
// Starter code for lp1.

// Change following line to your group number
// Changed type of base to long: 1:15 PM, 2017-09-08.
package cs6301.g1025;

import java.util.Iterator;
import java.util.LinkedList;

public class Num implements Comparable<Num> {

	static long TEN = 10; // This can be changed to what you want it to

	// To check base
	long base = TEN; // Change as needed

	public LinkedList<Long> num;
	boolean sign = false;

	/* Start of Level 1 */
	Num(String s) {
		long x = 0;
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
						this.num.addFirst(Long.parseLong(token.toString())); // Added in base 10
					} else {
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Java Unhandled Exception: " + e.getMessage());
			}
	}

	//constructor for 
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
		// num = new LinkedList<Long>();
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
	//constructor added to create an empty num class
	Num() {
		num = new LinkedList<Long>();
	}

	static long nextInt(Iterator<Long> it) {
		return it.hasNext() ? it.next() : -1;

	}

	static Num add(Num a, Num b) {
		if (a.sign == false && b.sign == true) {
			b.sign = false;
			return subtract(a, b);
		}
		if (a.sign == true && b.sign == false) {
			a.sign = false;
			return subtract(b, a);
		}
		long carry = 0l;
		Iterator<Long> ita = a.num.iterator();
		Iterator<Long> itb = a.num.iterator();
		Num res = new Num("");

		if (a.sign == true && b.sign == true) {
			res.sign = true;
		}
		long sum = 0;
		long ai = nextInt(ita);
		long bi = nextInt(itb);
		while (ai > 0 || bi > 0 || carry > 0) {
			sum = ai + bi + carry;

			res.num.add(sum % res.base);
			carry = sum / res.base;
			System.out.println(sum + " " + carry);
			ai = nextInt(ita);
			bi = nextInt(itb);
		}
		return res;
	}

	static Num xor(Num a, Num b) {
		Num res = new Num("");

		Iterator ita = a.num.iterator();
		Iterator itb = b.num.iterator();

		long ai = nextInt(ita);
		long bi = nextInt(itb);
		long diff = 0;
		while (ai > -1 && bi > -1) {
			diff = ai ^ bi;

			res.num.add(diff);
			ai = nextInt(ita);
			bi = nextInt(itb);
		}
//		System.out.println(res);
		return res;
	}

	static Num subtract(Num a, Num b) {
		Num res = new Num("");
		if (!a.sign) {
			if (b.sign) {
				b.sign = false;
				return add(a, b);
			} else {
				if (a.compareTo(b) <= 0) {
					res = xor(b, a);
					res.sign = true;
					return res;

				} else {
					res = xor(a, b);
					return res;
				}
			}
		} else if (a.sign) {
			a.sign = false;
			if (!b.sign) {
				res = add(a, b);
				res.sign = true;
				return res;
			} else {
				b.sign = false;
				res = subtract(a, b);
				res.sign = !res.sign;
				return res;
			}
		} else
			return null;
	}

	private static Num productHelper(Num n, long b) {
		Iterator<Long> it = n.num.iterator();
		long carry = 0l;
		Num res = new Num();
		res.base = n.base;
		while (it.hasNext() || carry > 0) {
			long sum = (nextInt(it) * b + carry);
			res.num.add(sum % res.base);
			carry = sum / res.base;

		}
		return res;
	}

	// Implement Karatsuba algorithm for excellence credit
	//temporary product, n^2 algorithm .. basic multiplication of two nums
	//haven't tested on negative nums
	static Num product(Num a, Num b) {
		Num res = new Num(0l, a.base);
		Iterator<Long> ib = b.num.iterator();
		int offset = 0;
		while (ib.hasNext()) {
			Num local = productHelper(a, nextInt(ib));
			int shift = offset++;
			while (shift > 0) {
				leftShift(local);
				shift--;
			}
			res = add(res, local);
		}
		return res;
	}

	// Use divide and conquer
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

	static Num convertBase(long baseA, Num a, long baseB) {
		//TODO
		String value = "";
		Num res = new Num("");
		res.sign = a.sign;
		res.base = 2;
		int n = 0;
		for (Long item : a.num) {
			while (item != 0) {
				long x = item % baseB;
				res.num.add(x);
				item = item / baseB;
			}
		}
		System.out.println(res);

		return a;
	}

	/* End of Level 1 */

	/* Start of Level 2 */
	static Num divide(Num a, Num b) {
//		TODO
		return null;
	}

	static Num mod(Num a, Num b) {
//		TODO
		return null;
	}

	// Use divide and conquer
	static Num power(Num a, Num n) {
//		TODO
		return null;
	}

	static Num squareRoot(Num a) {
//		TODO
		return null;
	}
	/* End of Level 2 */

	// Utility functions
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise
	public int compareTo(Num other) {

		if (this.sign == true && other.sign == false) {
			return -1;
		} else if (this.sign == false && other.sign == true) {
			return 1;
		} else {

			return this.num.toString().compareTo(other.toString());
			/*
			 * List<Long> t=this.num; List<Long> o=other.num;
			 * if(t.size()>o.size()){ return 1; } else if(t.size()<o.size()){
			 * return -1; } else{ int ts= t.size()-1; int os=o.size()-1;
			 * while(ts!=0){ if(t.get(ts)<o.get(os)){ return -1; } else
			 * if(t.get(ts)>o.get(os)){ return 1; } else{ ts--; os--; } } return
			 * 0; }
			 */
		}

	}
	static void leftShift(Num n){
		n.num.addFirst(0l);
//		return n;
	}

	static Num rightShift(Num n){
		n.num.removeFirst();
		return n;
	}

	// Output using the format "base: elements of list ..."
	// For example, if base=100, and the number stored corresponds to 10965,
	// then the output is "100: 65 9 1"
	void printList() {

		Iterator<Long> iterator = num.iterator();
		System.out.print(base + ": ");
		while (iterator.hasNext())
			System.out.print(iterator.next() + " ");
		System.out.println();

	}

	public Num baseConverter(long base) {
		Num result = new Num();
		result.base = base;

		return result;
	}

	// Return number to a string in base 10
	public String toString() {
//		return this.base + ": " + (sign ? "-" : "") + "" + this.num;
		/*
		 * Iterator<Long> it = this.num.iterator(); Long sum = 0l; int pow = 0;
		 * while(it.hasNext()){ sum += (long) Math.pow(this.base,
		 * pow)*it.next(); pow++; }
		 * 
		 * return sum.toString();
		 */
		

		Iterator<Long> it = this.num.iterator();
		Num nBaseTen = new Num();
		nBaseTen.base = 10l;
		Num base = new Num(this.base);
		long pow = 0l;
		while (it.hasNext()) {
			Num x = power(base, pow++);
			Num coeff = new Num(it.next());
			x = nBaseTen.product(coeff, x);
			nBaseTen = nBaseTen.add(x, nBaseTen);

		}
		nBaseTen.printList();
		return "";
	}

	public long base() {
		return base;
	}
}
