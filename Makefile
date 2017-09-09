CC = javac
FILE = cs6301/g10/

all: Timer Shuffle Item ItemExt Sort run

Graph: $(FILE)utils/Graph.java
	$(CC) $<

Timer: $(FILE)utils/Timer.java
	$(CC) $<

Shuffle: $(FILE)utils/Shuffle.java
	$(CC) $<

ItemExt: $(FILE)ItemExt.java
	$(CC) $<

Item: $(FILE)Item.java
	$(CC) $<

Sort: $(FILE)Sort.java
	$(CC) $<

run: $(FILE)Sort.class
	java -Xss256M $(FILE)Sort

clean:
	rm -rf $(FILE)*.class
	rm -rf $(FILE)*/*.class