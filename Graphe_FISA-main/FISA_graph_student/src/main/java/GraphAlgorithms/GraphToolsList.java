package GraphAlgorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import AdjacencyList.AdjacencyListDirectedGraph;
import Nodes_Edges.Arc;
import Nodes_Edges.DirectedNode;

public class GraphToolsList  extends GraphTools {
	
	private static int _DEBBUG =0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt=0;

	//--------------------------------------------------
	// 				Constructors
	//--------------------------------------------------

	public GraphToolsList(){
		super();
	}

	// ------------------------------------------
	// 				Accessors
	// ------------------------------------------



	// ------------------------------------------
	// 				Methods
	// ------------------------------------------
	
	public static void BFS(AdjacencyListDirectedGraph al) {
		
		int s = 0;
		int n = al.getNbNodes();
        boolean[] mark = new boolean[n];

        for (int v = 0; v < n; v++) {
            mark[v] = false;
        }

        mark[s] = true;

        Queue<Integer> fifo = new LinkedList<>();
        fifo.add(s);

        while (!fifo.isEmpty()) {
            int v = fifo.poll();
            System.out.print(v + " ");
            DirectedNode currentNode = al.getNodes().get(v);

            for (Arc arc : currentNode.getArcSucc()) {
                DirectedNode neighbor = arc.getSecondNode();
                int w = neighbor.getLabel();

                if (!mark[w]) {
                    mark[w] = true;
                    fifo.add(w);
                }
            }
        }
        
        System.out.println();
		
	}
	
	public static void explorerSommet(DirectedNode s, Set<DirectedNode> atteint) {
		
		visite[s.getLabel()] = 1;
		debut[s.getLabel()] = cpt++;
	    atteint.add(s);
	    System.out.print(s.getLabel() + " ");

	    for (Arc arc : s.getArcSucc()) {
	        DirectedNode voisin = arc.getSecondNode();
	        if (!atteint.contains(voisin)) {
	            explorerSommet(voisin, atteint);
	        }
	    }
	    
	    visite[s.getLabel()] = 2;
	    fin[s.getLabel()] = cpt++;
	    order_CC.add(s.getLabel());
	}
	
	public static void explorerGraphe(AdjacencyListDirectedGraph graph) {
		
		int n = graph.getNbNodes();
		visite = new int[n];
		debut = new int[n];
	    fin = new int[n];
	    Set<DirectedNode> atteint = new HashSet<DirectedNode>();
	    order_CC = new ArrayList<Integer>();

	    for (DirectedNode s : graph.getNodes()) {
	        if (!atteint.contains(s)) {
	            explorerSommet(s, atteint);
	        }
	    }
	    
	    String debuts = "";
	    String fins = "";
	    for (int i = 0; i < n; i++) {
	    	debuts += debut[i] + " ";
	    	fins += fin[i] + " ";
	    }

	    System.out.println("\nDébuts : " + debuts);
	    System.out.println("Fins   : " + fins);
	    
	    System.out.println("Ordre de fin des sommets : " + order_CC);
	}


	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		AdjacencyListDirectedGraph al = new AdjacencyListDirectedGraph(Matrix);
		System.out.println(al);

		System.out.println("Ordre de visite avez le BFS à partir du sommet 0 :");
        GraphToolsList.BFS(al);
        
        System.out.println("\nOrdre de visite avez le DFS :");
        GraphToolsList.explorerGraphe(al);
	}
}
