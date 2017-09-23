#Extract the files into cs6301 folder

#Using an IDE:
Place the extracted folder (cs6301\g1025\..) in a src folder of an IDE (eg. eclipse) and stock files in cs6301\g00\

##Using cmd:
compile all files using javac *.java
Move to location outside cs6301.
In cmd, execute java cs6301.g1025.<ClassName>

1.Topological Order
Implemented the Topological Order function
test: java cs6301.g1025.TopologicalOrder <Enter the filename>

2.Strongly Connected Components
Implemented DFS to find Strongly connected components
test: java cs6301.g1025.SCC <Enter the filename>

3.Eulerian
Will call the SCC.
test: java cs6301.g1025.Eulerian <Enter the filename>

4.DAG
Implemented the DFS class to find isDAG.
test: java cs6301.g1025.DAG <Enter the filename>

5.BridgeCut
Implemented the DFS class to find list of Bidges and cut vertices.
test: java cs6301.g1025.BridgeCut <Enter the filename>
