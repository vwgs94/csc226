/* IndependentSet.java
   CSC 226 - Spring 2015
   Assignment 5 - Template for Independent Set
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java IndependentSet 
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java IndependentSet file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix will be set to 1 if an edge
   exists between vertices i and j and 0 if no edge exists. Since the graph
   is undirected, the matrix A will be symmetric.
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.


   B. Bird - 11/19/2014
*/

/*
****************************************************************************************************
*** ID                  : xxxxxxxxx
*** Name                : Victoria Sahle
*** Date                : November 20, 2015
*** Program Name        : IndependentSet.java
*** Program Description : Assignment 5: Implement an algorithm to find an independent set of maximum 
***                       size in an undirected graph. An independent set S of a graph G is a set of
***                       vertices of G such that no two vertices in S are adjacent.                      
*** Program Input       : A n × n array G representing a graph.
*** Program Output      : An independent set S of maximum size with respect to G.
****************************************************************************************************
*/

import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

//Do not change the name of the IndependentSet class
public class IndependentSet{

        static void FindSets(int[][] G, VertexSet B, VertexSet S, VertexSet F, int v){
            //If v = |V(G)|
            int n = G.length;
            if (v == n){
                //If S is larger than the current best set, set the new best set to be a copy of S
                if (S.getSize() > B.getSize()){
                    B.copyFrom(S);
                }
                    return;
                
            }
          
            if(S.getSize() + (n - F.getSize()) <= B.getSize()){
            	    return;          	     
            }
            
            //F1 copy F
            VertexSet F1 = new VertexSet(n);
            F1.copyFrom(F);
            F1.addVertex(v);
            //Add v to F1
            //Recurse since v is in F
           if (F.isInSet(v)){
                 FindSets(G, B, S, F1, v+1);
            }else if (!F.isInSet(v)) {
                //The vertex can be added to S, since it is not in F
                //add v to S
                S.addVertex(v);

                //Build neighbor of v
                for(int j=0; j < n; j++) {
                    if(G[v][j] == 1){
                        F1.addVertex(j);
                    }
                }
                //Recurse to vertix v+1
               FindSets(G, B, S, F1, (v+1));
               S.removeVertex(v);
            }
            
            
        }

        /* MaximumIndependentSet(G)
		Given an adjacency matrix for graph G, return a maximum
		independent set of G.

	*/
	static VertexSet MaximumIndependentSet(int[][] G){
		int numVerts = G.length;
                //Current set
                VertexSet S = new VertexSet(numVerts);
                //Set found so far
                VertexSet B = new VertexSet(numVerts);
                //Forbidden vertices
                VertexSet F = new VertexSet(numVerts);
                //Start the recursive algorithm on vertex 0
                FindSets(G, B, S, F, 0);
                return B;

	}

	/* verifyIndSet(G,S)
	   Verifies that the provided independent set S is valid.
	   If the set is valid, the function returns the number of vertices
	   in the set. If the set is not valid, the function returns -1.
	*/
	static int verifyIndSet(int[][] G, VertexSet S){
		int n = G.length;
		int count = 0;
		for(int i = 0; i < n; i++){
			if (!S.isInSet(i))
				continue;
			//Check that the neighbours of i are not in the set
			for(int j = 0; j < n; j++)
				if (G[i][j] == 1 && S.isInSet(j))
					return -1;
			count++;
		}
		return count;
	}

	/* VertexSet class */
	/* Do not add, change or remove anything in the class definition below */
	/* If any contents of the VertexSet class are changed, your submission will
	   not be marked. */
	/* A VertexSet represents a set of vertices in a graph. */
	public static class VertexSet{
		private int size;
		private boolean[] set;
		/* Constructor
		   Initialize a vertex set over an n vertex graph, with the
		   set initialized to be empty.
		*/
		public VertexSet(int numVerts){
			set = new boolean[numVerts];
			size = 0;
		}

		/* addVertex(vertexIndex)
		   Add the vertex vertexIndex to the set.
		   If vertexIndex is already in the set, this operation
		   has no effect.
		*/
		public void addVertex(int vertexIndex){
			if (set[vertexIndex])
				return;
			set[vertexIndex] = true;
			size++;
		}
		/* removeVertex(vertexIndex)
		   Delete vertexIndex from the set.
		   If vertexIndex not in the set, this operation has no
		   effect.
		*/
		public void removeVertex(int vertexIndex){
			if (!set[vertexIndex])
				return;
			set[vertexIndex] = false;
			size--;
		}
		/* isInSet(vertexIndex)
		   Returns true if vertexIndex is a member of the set and
		   false otherwise.
		*/
		public boolean isInSet(int vertexIndex){
			return set[vertexIndex];
		}
		/* getSize()
		   Returns the current size of the set.
		*/
		public int getSize(){
			return size;
		}

		/* copyFrom(otherSet)
		   Copies all of the data from otherSet into the current set
		   (completely replacing all data in the current set).
		*/
		public void copyFrom(VertexSet otherSet){
			this.size = otherSet.size;
			this.set = otherSet.set.clone();
		}

		/* printVertices()
		   Prints the indices of vertices in the set.
		*/
		public void printVertices(){
			for(int i = 0; i < set.length; i++)
				if (set[i])
					System.out.printf("%d ",i);
			System.out.printf("\n");
		}
	}


	/* main()
	   Contains code to test the MaximumIndependentSet function. You may modify
	   the testing code if needed, but nothing in this function will be
	   considered during marking, and the testing process used for marking will
	   not execute any of the code below.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}

		int graphNum = 0;
		double totalTimeSeconds = 0;

		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();

			VertexSet maxSet = MaximumIndependentSet(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;

			int setSize = (maxSet == null)? -1: verifyIndSet(G,maxSet);
			if(setSize == -1)
				System.out.printf("Graph %d: Set of size %d is not a valid independent set.\n",graphNum,maxSet.getSize());
			else{
				System.out.printf("Graph %2d: Maximum independent set found with size %d.\n",graphNum,setSize);
				System.out.printf("      S = ");
				maxSet.printVertices();
			}
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\n",graphNum,(graphNum != 1)?"s":"");
		System.out.printf("Total Time (seconds): %.2f\n",totalTimeSeconds);
		System.out.printf("Average Time (seconds): %.2f\n",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}
