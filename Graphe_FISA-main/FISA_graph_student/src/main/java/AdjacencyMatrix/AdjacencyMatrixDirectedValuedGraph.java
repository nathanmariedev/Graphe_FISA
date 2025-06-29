package AdjacencyMatrix;

import GraphAlgorithms.GraphTools;

import java.lang.reflect.Array;
import java.util.*;


public class AdjacencyMatrixDirectedValuedGraph extends AdjacencyMatrixDirectedGraph {

	//--------------------------------------------------
	// 				Class variables
	//-------------------------------------------------- 

	// No class variable, we use the matrix variable but with costs values 

	//--------------------------------------------------
	// 				Constructors
	//-------------------------------------------------- 

	public AdjacencyMatrixDirectedValuedGraph(int[][] matrixVal) {
		super(matrixVal);
	}

	
	// ------------------------------------------------
	// 					Methods
	// ------------------------------------------------	
	
	
	/**
     * adds the arc (from,to,cost). If there is already one initial cost, we replace it.
     */	
	public void addArc(int from, int to, int cost ) {
		if(!this.isArc(from,to)){
			this.matrix[from][to] = cost;
		} else {
			System.out.printf("Arc (%d,%d) is already added\n", from, to);
		}
	}

	public int[] dijkstra(int from) {
		ArrayList<Integer> mark = new ArrayList<>();
        int[] val = new int[this.nbNodes];
		int[] pred = new int[this.nbNodes];

		for (int i = 0; i < this.nbNodes; i++) {
			mark.add(0);
			val[i] = Integer.MAX_VALUE / 2;
			pred[i] = -1;
			System.out.println("z");
		}

		val[from] = 0;
		pred[from] = from;

		while (mark.contains(0)) {
			System.out.println(mark);

			int x = 0;
			int min = Integer.MAX_VALUE / 2;

			for (int y = 0; y < this.nbNodes; y++) {

				if (mark.get(y) == 0 && val[y] < min) {
					x = y;
					min = val[y];
				}
			}

			if (min < Integer.MAX_VALUE / 2) {
				mark.set(x, 1);

				for (int y: this.getSuccessors(x)) {

					if (mark.get(y) == 0 && ((val[x] + this.matrix[x][y]) < val[y])) {
						val[y] = val[x] + this.matrix[x][y];
						pred[y] = x;
					}
				}
			}
		}
		return val;

	}

	
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("\n Matrix of Costs: \n");
		for (int[] lineCost : this.matrix) {
			for (int i : lineCost) {
				s.append(i).append("\t");
			}
			s.append("\n");
		}
		s.append("\n");
		return s.toString();
	}

	public static void main(String[] args) {
        int[][] matrixValued = GraphTools.generateValuedGraphData(10, false, false, true, false, 100001);
		AdjacencyMatrixDirectedValuedGraph am = new AdjacencyMatrixDirectedValuedGraph(matrixValued);
		System.out.println(am);

		int[][] matrix = {
			{ 0, 4, 1, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 2, 0, 5, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 3, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 2, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 6, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
		};

		AdjacencyMatrixDirectedValuedGraph am2 = new AdjacencyMatrixDirectedValuedGraph(matrix);

		System.out.println("Dijkstra tests :");
		int[] distances = am2.dijkstra(0);
		int[] expectedDistances = {0, 3, 1, 4, 7, 9, 10, 16, 18, 19};

			for (int i = 0; i < expectedDistances.length; i++) {
			if (distances[i] != expectedDistances[i]) {
				System.out.printf("Vertex %d Uncorrect : Expected %d, Given %d\n", i, expectedDistances[i], distances[i]);
			} else {
				System.out.printf("Vertex %d Correct : %d\n", i, distances[i]);
			}
		}
	}
}
