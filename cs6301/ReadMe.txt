Team g1025

Authors:
antriksh agarwal
gunjan tomar
sai kumar suvanam
swaroop pydisetty

Testing the code:

#Extract the files into cs6301 folder

#Using an IDE:
Place the extracted folder (cs6301\g1025\..) in a src folder of an IDE (eg. eclipse) and stock files in cs6301\g00\

##Using cmd:

1. run javac *.java to compile the code

2. navigate to root folder

3. Execute each level separatey
  a. Level-1: java cs6301.g1025.LP1L1 
  	* Tests add, subtract, toString, product, power (long) and constructors 
  b. Level-2:: java cs6301.g1025.Lp1L2
  	* Tests power, divide, mod, squareroot	
  c. Level-3: java cs6301.g1025.Lp1L3
  	* Enter input in the console 
  	* evaluates the input and the program prints the output
  d. run java LP1L4 <base> // base of your choice between 2^(1 - 31)
	* Enter input into the console and program executes
	
Level 1 methods:

Methods implemented:
Constructors:
Num : String, base
Num : long, base 
add
subtract
divide
product
product (karatsuba)
toString
powerLong
printList
related files: LP1L1.java, Num.java

Level 2
divide
mod 
squareroot
powerNum
related files: LP1L2.java, Num.java

Level 3
postfix evaluation
related files: LP1L3.java, Num.java

Level 4
shuntingyard implementation
processing of multiple lines from input 
related files: LP1L4.java, Frame.java, ShuntingYard.java, Tokenizer.java, Num.java

