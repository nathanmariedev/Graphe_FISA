package AdjacencyMatrix;


import GraphAlgorithms.GraphTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AdjacencyList.AdjacencyListUndirectedGraph;

/**
 * This class represents the undirected graphs structured by an adjacency matrix.
 * We consider only simple graph
 */
public class AdjacencyMatrixUndirectedGraph {
	
	//--------------------------------------------------
    // 				Class variables
    //--------------------------------------------------

    protected int nbNodes;		// Number of vertices
    protected int nbEdges;		// Number of edges/arcs
    protected int[][] matrix;	// The adjacency matrix
	
	//--------------------------------------------------
	// 				Constructors
	//-------------------------------------------------- 
	
	public AdjacencyMatrixUndirectedGraph() {
		this.matrix = new int[0][0];
        this.nbNodes = 0;
        this.nbEdges = 0;
	}

	public AdjacencyMatrixUndirectedGraph(int[][] mat) {
		this.nbNodes = mat.length;
		this.nbEdges = 0;
		this.matrix = new int[this.nbNodes][this.nbNodes];
		for (int i = 0; i < this.nbNodes; i++) {
			for (int j = i + 1; j < this.nbNodes; j++) {
				this.matrix[i][j] = mat[i][j];
				this.matrix[j][i] = mat[i][j];
				if (mat[i][j] != 0) {
					this.nbEdges++;
				}
			}
		}
	}


	public AdjacencyMatrixUndirectedGraph(AdjacencyListUndirectedGraph g) {
		this.nbNodes = g.getNbNodes(); 				
		this.nbEdges = g.getNbEdges(); 				
		this.matrix = g.toAdjacencyMatrix(); 
	}

	//--------------------------------------------------
	// 					Accessors
	//--------------------------------------------------

	/**
     * @return the matrix modeling the graph
     */
    public int[][] getMatrix() {
        return this.matrix;
    }

    /**
     * @return the number of nodes in the graph (referred to as the order of the graph)
     */
    public int getNbNodes() {
        return this.nbNodes;
    }
	
    /**
	 * @return the number of edges in the graph
 	 */	
	public int getNbEdges() {
		return this.nbEdges;
	}

	/**
	 * 
	 * @param x the vertex selected
	 * @return a list of vertices which are the neighbours of x
	 */
	public List<Integer> getNeighbours(int v) {
		List<Integer> l = new ArrayList<>();
		for(int i = 0; i<matrix[v].length; i++){
			if(matrix[v][i]>0){
				l.add(i);
			}
		}
		return l;
	}
	
	// ------------------------------------------------
	// 					Methods 
	// ------------------------------------------------		
	
	/**
     	* @return true if the edge is in the graph.
     	*/
	public boolean isEdge(int x, int y) {
		return this.matrix[x][y]!=0;
	}
	
	/**
     	* removes the edge (x,y) if there exists one between these nodes in the graph.
    	 */
	public void removeEdge(int x, int y) {
		if (isEdge(x, y)) {
			this.matrix[x][y]=0;
		} else {
			System.out.printf("Edge {%d,%d} is already removed\n", x, y);
		}
	}

	/**
     	* adds the edge (x,y) if there is not already one.
     	*/
	public void addEdge(int x, int y) {
		if (!isEdge(x, y)) {
			this.matrix[x][y]=1;
		} else {
			System.out.printf("Edge {%d,%d} is already removed\n", x, y);
		}
	}

	
	/**
    	* @return the adjacency matrix representation int[][] of the graph
    	*/
	public int[][] toAdjacencyMatrix() {
		return this.matrix;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("\nAdjacency Matrix: \n");
		for (int[] ints : this.matrix) {
			for (int anInt : ints) {
				s.append(anInt).append("\t");
			}
			s.append("\n");
		}
		s.append("\n");
		return s.toString();
	}

	public static void main(String[] args) {
		int[][] mat2 = GraphTools.generateGraphData(10, 35, false, true, false, 100001);
		AdjacencyMatrixUndirectedGraph am = new AdjacencyMatrixUndirectedGraph(mat2);
		System.out.println(am);
		System.out.println("n = "+am.getNbNodes()+ "\nm = "+am.getNbEdges() +"\n");
		
		// Neighbours of vertex 2 :
		System.out.println("Neighbours of vertex 2 : ");
		List<Integer> t2 = am.getNeighbours(2);
		for (Integer integer : t2) {
			System.out.print(integer + ", ");
		}
		
		// We add three edges {3,5} :
		System.out.println("\n\nisEdge(3, 5) ? " + am.isEdge(3, 5));
		for(int i = 0; i<3;i++)
			am.addEdge(3, 5);
		
		System.out.println("\n"+am);
		
		System.out.println("\nAfter removing one edge {3,5} :");
		am.removeEdge(3,5);
		System.out.println(am);

		System.out.println("Is Edge tests :");
		System.out.println("(0, 0) ? " + am.isEdge(0, 0) + " Should be FALSE");
		System.out.println("(2, 0) ? " + am.isEdge(2, 0) + " Should be TRUE");
		System.out.println("(3, 1) ? " + am.isEdge(3, 1) + " Should be TRUE");
		System.out.println("(7, 3) ? " + am.isEdge(7, 3) + " Should be TRUE");
		System.out.println("(4, 3) ? " + am.isEdge(4, 3) + " Should be FALSE");
		System.out.println("/*----------------*/");

		System.out.println("Remove Edge tests :");
		am.removeEdge(0, 0);
		System.out.println("(0,0) Should be Edge already removed");
		am.removeEdge(2, 0);
		System.out.println("(2,0) ? " + am.isEdge(2, 0) + " Should be FALSE");
		am.removeEdge(3, 1);
		System.out.println("(3,1) ? " + am.isEdge(3, 1) + " Should be FALSE");
		am.removeEdge(7, 3);
		System.out.println("(7,3) ? " + am.isEdge(7, 3) + " Should be FALSE");
		System.out.println("/*----------------*/");

		System.out.println("Add Edge tests :");
		am.addEdge(0, 0);
		System.out.println("(0,0) ? " + am.isEdge(0, 0) + " Should be TRUE");
		am.addEdge(2, 0);
		System.out.println("(2,0) ? " + am.isEdge(2, 0) + " Should be TRUE");
		am.addEdge(3, 1);
		System.out.println("(3,1) ? " + am.isEdge(3, 1) + " Should be TRUE");
		am.addEdge(7, 3);
		System.out.println("(7,3) ? " + am.isEdge(7, 3) + " Should be TRUE");
		System.out.println("/*----------------*/");
	}

}
