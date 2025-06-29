package AdjacencyMatrix;


import GraphAlgorithms.GraphTools;
import Nodes_Edges.AbstractNode;
import Nodes_Edges.DirectedNode;

import java.util.ArrayList;
import java.util.List;

import AdjacencyList.AdjacencyListDirectedGraph;

/**
 * This class represents the directed graphs structured by an adjacency matrix.
 * We consider only simple graph
 */
public class AdjacencyMatrixDirectedGraph {

	//--------------------------------------------------
    // 				Class variables
    //--------------------------------------------------

    protected int nbNodes;		// Number of vertices
    protected int nbArcs;		// Number of edges/arcs
    protected int[][] matrix;	// The adjacency matrix
	
	//--------------------------------------------------
	// 				Constructors
	//-------------------------------------------------- 

    public AdjacencyMatrixDirectedGraph() {
        this.matrix = new int[0][0];
        this.nbNodes = 0;
        this.nbArcs = 0;
    }

      
	public AdjacencyMatrixDirectedGraph(int[][] mat) {
		this.nbNodes = mat.length;
		this.nbArcs = 0;
		this.matrix = new int[this.nbNodes][this.nbNodes];
		for(int i = 0; i<this.nbNodes; i++){
			for(int j = 0; j<this.nbNodes; j++){
				this.matrix[i][j] = mat[i][j];
				this.nbArcs += mat[i][j];
			}
		}
	}

	public AdjacencyMatrixDirectedGraph(AdjacencyListDirectedGraph g) {
		this.nbNodes = g.getNbNodes();
		this.nbArcs = g.getNbArcs();
		this.matrix = g.toAdjacencyMatrix();
	}

	//--------------------------------------------------
	// 					Accessors
	//--------------------------------------------------


    /**
     * Returns the matrix modeling the graph
     */
    public int[][] getMatrix() {
        return this.matrix;
    }

    /**
     * Returns the number of nodes in the graph (referred to as the order of the graph)
     */
    public int getNbNodes() {
        return this.nbNodes;
    }
	
    /**
	 * @return the number of arcs in the graph
 	 */	
	public int getNbArcs() {
		return this.nbArcs;
	}

	
	/**
	 * @param u the vertex selected
	 * @return a list of vertices which are the successors of u
	 */
	public List<Integer> getSuccessors(int u) {
		List<Integer> succ = new ArrayList<Integer>();
		for(int v =0;v<this.matrix[u].length;v++){
			if(this.matrix[u][v]>0){
				succ.add(v);
			}
		}
		return succ;
	}

	/**
	 * @param v the vertex selected
	 * @return a list of vertices which are the predecessors of v
	 */
	public List<Integer> getPredecessors(int v) {
		List<Integer> pred = new ArrayList<Integer>();
		for(int u =0;u<this.matrix.length;u++){
			if(this.matrix[u][v]>0){
				pred.add(u);
			}
		}
		return pred;
	}

	// ------------------------------------------------
	// 					Methods 
	// ------------------------------------------------		
	
	/**
	 * @return true if the arc (from,to) exists in the graph.
 	 */
	public boolean isArc(int from, int to) {
		return this.matrix[from][to]>0;
	}

	public boolean isConnectedFrom(int start) {
		boolean[] visited = new boolean[this.nbNodes];
		dfs(start, visited);

		for (boolean v : visited) {
			if (!v) return false; // Un sommet non atteint => pas connexe
		}
		return true;
	}

	private void dfs(int node, boolean[] visited) {
		visited[node] = true;
		for (int neighbor : getSuccessors(node)) {
			if (!visited[neighbor]) {
				dfs(neighbor, visited);
			}
		}
	}

	public boolean isStronglyConnected() {
		boolean[] visited = new boolean[this.nbNodes];

		// DFS normal
		dfs(0, visited);
		for (boolean v : visited) {
			if (!v) return false;
		}

		// DFS sur le graphe inverse
		AdjacencyMatrixDirectedGraph reversed = this.computeInverse();
		visited = new boolean[this.nbNodes];
		reversed.dfs(0, visited);
		for (boolean v : visited) {
			if (!v) return false;
		}

		return true;
	}

	/**
	 * removes the arc (from,to) if there exists one between these nodes in the graph.
	 */
	public void removeArc(int from, int to) {
		if(this.isArc(from,to)){
			this.matrix[from][to] = 0;
		} else {
			System.out.printf("Arc (%d,%d) is already removed\n", from, to);
		}
	}

	/**
	 * Adds the arc (from,to). 
	 */
	public void addArc(int from, int to) {
		if(!this.isArc(from,to)){
			this.matrix[from][to] = 1;
		} else {
			System.out.printf("Arc (%d,%d) is already added\n", from, to);
		}
	}

	/**
	 * @return a new graph which is the inverse graph of this.matrix
	 */
	public AdjacencyMatrixDirectedGraph computeInverse() {
		AdjacencyMatrixDirectedGraph amInv = new AdjacencyMatrixDirectedGraph(new int[this.nbNodes][this.nbNodes]);

		for (int i = 0; i < this.nbNodes; i++) {
			for(int j = 0; j < this.nbNodes; j++) {
				if (this.isArc(i, j)) {
					amInv.addArc(j, i);
				}
			}
		}
		return amInv;
	}


	@Override
	public String toString(){
		StringBuilder s = new StringBuilder("Adjacency Matrix: \n");
		for (int[] ints : matrix) {
			for (int anInt : ints) {
				s.append(anInt).append("\t");
			}
			s.append("\n");
		}
		s.append("\n");
		return s.toString();
	}

	public static void main(String[] args) {
		int[][] matrix2 = GraphTools.generateGraphData(10, 20, false, false, false, 100001);
		AdjacencyMatrixDirectedGraph am = new AdjacencyMatrixDirectedGraph(matrix2);
		System.out.println(am);
		System.out.println("n = "+am.getNbNodes()+ "\nm = "+am.getNbArcs() +"\n");
		
		// Successors of vertex 1 :
		System.out.println("Sucesssors of vertex 1 : ");
		List<Integer> t = am.getSuccessors(1);
		for (Integer integer : t) {
			System.out.print(integer + ", ");
		}
		
		// Predecessors of vertex 2 :
		System.out.println("\n\nPredecessors of vertex 2 : ");
		List<Integer> t2 = am.getPredecessors(2);
		for (Integer integer : t2) {
			System.out.println(integer + ", ");
		}
		// A completer

		System.out.println("\nIs Edge tests :");
		System.out.println("(0, 0) ? " + am.isArc(0, 0) + " Should be FALSE");
		System.out.println("(3, 1) ? " + am.isArc(3, 1) + " Should be TRUE");
		System.out.println("(7, 3) ? " + am.isArc(7, 3) + " Should be TRUE");
		System.out.println("\n/*----------------*/\n");

		System.out.println("Remove Arc tests :");
		am.removeArc(0, 0);
		System.out.println("(0,0) Should be Arc already removed");
		am.removeArc(2, 0);
		System.out.println("(2,0) Should be Arc already removed");
		am.removeArc(3, 1);
		System.out.println("(3,1) ? " + am.isArc(3, 1) + " Should be FALSE");
		am.removeArc(7, 3);
		System.out.println("(7,3) ? " + am.isArc(7, 3) + " Should be FALSE");
		System.out.println("\n/*----------------*/\n");

		System.out.println("Add Arc tests :");
		am.addArc(0, 0);
		System.out.println("(0,0) ? " + am.isArc(0, 0) + " Should be TRUE");
		am.addArc(2, 0);
		System.out.println("(2,0) ? " + am.isArc(2, 0) + " Should be TRUE");
		am.addArc(3, 1);
		System.out.println("(3,1) ? " + am.isArc(3, 1) + " Should be TRUE");
		am.addArc(7, 3);
		System.out.println("(7,3) ? " + am.isArc(7, 3) + " Should be TRUE");
		am.addArc(8, 1);
		System.out.println("(8,1) Should be Arc already added");
		System.out.println("\n/*----------------*/\n");

		System.out.println("Inverted Matrix tests :");
		System.out.println("(3,7) ? " + am.isArc(3, 7) + " Should be FALSE");
		System.out.println("(1,3) ? " + am.isArc(1, 3) + " Should be FALSE");
		System.out.println("(0,2) ? " + am.isArc(0, 2) + " Should be FALSE");
		System.out.println("Matrix Inverted -> ");
		AdjacencyMatrixDirectedGraph amInverted = am.computeInverse();
		System.out.println("(3,7) ? " + amInverted.isArc(3, 7) + " Should be TRUE");
		System.out.println("(1,3) ? " + amInverted.isArc(1, 3) + " Should be TRUE");
		System.out.println("(0,2) ? " + amInverted.isArc(0, 2) + " Should be TRUE");
		System.out.println("\n/*----------------*/\n");

	}
}
