package AdjacencyList;

import java.util.ArrayList;
import java.util.List;


import AdjacencyMatrix.AdjacencyMatrixUndirectedGraph;
import GraphAlgorithms.GraphTools;
import Nodes_Edges.Arc;
import Nodes_Edges.DirectedNode;
import Nodes_Edges.Edge;
import Nodes_Edges.UndirectedNode;



public class AdjacencyListDirectedGraph {

	//--------------------------------------------------
    // 				Class variables
    //--------------------------------------------------

	private static int _DEBBUG =0;
	
	protected List<DirectedNode> nodes; // list of the nodes in the graph
	protected List<Arc> arcs; // list of the arcs in the graph
    protected int nbNodes; // number of nodes
    protected int nbArcs; // number of arcs
	
    

    
    //--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------
 

	public AdjacencyListDirectedGraph(){
		this.nodes = new ArrayList<DirectedNode>();
		this.arcs= new ArrayList<Arc>();
		this.nbNodes = 0;
	    this.nbArcs = 0;		
	}
	
	public AdjacencyListDirectedGraph(List<DirectedNode> nodes,List<Arc> arcs) {
		this.nodes = nodes;
		this.arcs= arcs;
        this.nbNodes = nodes.size();
        this.nbArcs = arcs.size();                
    }

    public AdjacencyListDirectedGraph(int[][] matrix) {
        this.nbNodes = matrix.length;
        this.nodes = new ArrayList<DirectedNode>();
        this.arcs= new ArrayList<Arc>();
        
        for (int i = 0; i < this.nbNodes; i++) {
            this.nodes.add(new DirectedNode(i));
        }
        
        for (DirectedNode n1 : this.getNodes()) {
            for (int j = 0; j < matrix[n1.getLabel()].length; j++) {
            	DirectedNode n2 = this.getNodes().get(j);
                if (matrix[n1.getLabel()][j] != 0) {
                	Arc a = new Arc(n1,n2);
                    n1.addArc(a);
                    this.arcs.add(a);                    
                    n2.addArc(a);
                    this.nbArcs++;
                }
            }
        }
    }

    public AdjacencyListDirectedGraph(AdjacencyListDirectedGraph g) {
        super();
        this.nodes = new ArrayList<>();
        this.arcs= new ArrayList<Arc>();
        this.nbNodes = g.getNbNodes();
        this.nbArcs = g.getNbArcs();
        
        for(DirectedNode n : g.getNodes()) {
            this.nodes.add(new DirectedNode(n.getLabel()));
        }
        
        for (Arc a1 : g.getArcs()) {
        	this.arcs.add(a1);
        	DirectedNode new_n   = this.getNodes().get(a1.getFirstNode().getLabel());
        	DirectedNode other_n = this.getNodes().get(a1.getSecondNode().getLabel());
        	Arc a2 = new Arc(a1.getFirstNode(),a1.getSecondNode(),a1.getWeight());
        	new_n.addArc(a2);
        	other_n.addArc(a2);
        }  

    }

    // ------------------------------------------
    // 				Accessors
    // ------------------------------------------

    /**
     * Returns the list of nodes in the graph
     */
    public List<DirectedNode> getNodes() {
        return nodes;
    }
    
    /**
     * Returns the list of nodes in the graph
     */
    public List<Arc> getArcs() {
        return arcs;
    }

    /**
     * Returns the number of nodes in the graph
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
	 * @return true if arc (from,to) exists in the graph
 	 */
    public boolean isArc(DirectedNode from, DirectedNode to) {
        List<Arc> arcs = this.getArcs();
        return arcs.stream().anyMatch( arc -> arc.getFirstNode().equals(from) && arc.getSecondNode().equals(to));
    }

    /**
	 * Removes the arc (from,to), if it exists. And remove this arc and the inverse in the list of arcs from the two extremities (nodes)
 	 */
    public void removeArc(DirectedNode from, DirectedNode to) {
    	boolean arcExists = this.arcs.contains(new Arc(from,to));
        if (!arcExists) {
            return;
        }
        this.arcs.remove(new Arc(from,to));
        this.nbArcs--;
        from.getArcSucc().remove(new Arc(from,to));
        to.getArcPred().remove(new Arc(from,to));
    }

    /**
	* Adds the arc (from,to) if it is not already present in the graph, requires the existing of nodes from and to. 
	* And add this arc to the incident list of both extremities (nodes) and into the global list "arcs" of the graph.   	 
  	* On non-valued graph, every arc has a weight equal to 0.
 	*/
    public void addArc(DirectedNode from, DirectedNode to) {
        boolean arcExists = this.arcs.contains(new Arc(from,to));
        if (arcExists) {
            return;
        }
        this.arcs.add(new Arc(from,to));
        this.nbArcs++;
        from.getArcSucc().add(new Arc(from,to));
        to.getArcPred().add(new Arc(from,to));
    }

    //--------------------------------------------------
    // 				Methods
    //--------------------------------------------------

     /**
     * @return the corresponding nodes in the list this.nodes
     */
    public DirectedNode getNodeOfList(DirectedNode src) {
        return this.getNodes().get(src.getLabel());
    }

    /**
     * @return the adjacency matrix representation int[][] of the graph
     */
    public int[][] toAdjacencyMatrix() {
        int[][] matrix = new int[nbNodes][nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            for (int j = 0; j < nbNodes; j++) {
                if (this.isArc(this.getNodes().get(i), this.getNodes().get(j))) {
                    matrix[i][j] = 1;
                }
            }
        }
        return matrix;
    }

    /**
	 * @return a new graph implementing IDirectedGraph interface which is the inverse graph of this
 	 */
    public AdjacencyListDirectedGraph computeInverse() {
        AdjacencyListDirectedGraph g = new AdjacencyListDirectedGraph();

        for (DirectedNode node : this.nodes) {
            g.nodes.add(new DirectedNode(node.getLabel()));
        }

        g.nbNodes = this.nbNodes;
        for (int i = 0; i < g.getNbNodes(); i++) {
            DirectedNode from = this.nodes.get(i);
            for (Arc arc : from.getArcSucc()) {
                DirectedNode to = arc.getSecondNode();
                DirectedNode newFrom = g.getNodes().get(to.getLabel());
                DirectedNode newTo = g.getNodes().get(from.getLabel());
                g.addArc(newFrom, newTo);
            }
        }
        return g;
    }
    
    @Override
    public String toString(){
    	StringBuilder s = new StringBuilder();
        s.append("List of nodes and their successors/predecessors :\n");
        for (DirectedNode n : this.nodes) {
            s.append("\nNode ").append(n).append(" : ");
            s.append("\nList of out-going arcs: ");
            for (Arc a : n.getArcSucc()) {
                s.append(a).append("  ");
            }
            s.append("\nList of in-coming arcs: ");
            for (Arc a : n.getArcPred()) {
                s.append(a).append("  ");
            }
            s.append("\n");
        }
        s.append("\nList of arcs :\n");
        for (Arc a : this.arcs) {
        	s.append(a).append("  ");
        }
        s.append("\n");
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, false, 100001);
        // GraphTools.afficherMatrix(Matrix);
        AdjacencyListDirectedGraph al = new AdjacencyListDirectedGraph(Matrix);
        // System.out.println(al);
        // System.out.println("(n_7,n_3) is it in the graph ? " +  al.isArc(al.getNodes().get(7), al.getNodes().get(3)));
        
        DirectedNode n0 = al.nodes.get(0);
        DirectedNode n1 = al.nodes.get(1);
        DirectedNode n2 = al.nodes.get(2);
        DirectedNode n3 = al.nodes.get(3);
        DirectedNode n4 = al.nodes.get(4);
        DirectedNode n5 = al.nodes.get(5);

        System.out.println("Is Arc tests :");
        System.out.println("(4,0) ? " + al.isArc(n4, n0) + " Should be TRUE");
        System.out.println("(3,2) ? " + al.isArc(n3, n2) + " Should be TRUE");
        System.out.println("(1,4) ? " + al.isArc(n1, n4) + " Should be FALSE");
        System.out.println("(3,3) ? " + al.isArc(n3, n3) + " Should be FALSE (because it's simple graphs)");

        System.out.println("\n/*----------------*/\n");

        System.out.println("Remove Arc tests :");
        al.removeArc(n4, n0);
        System.out.println("(1,4) ? " + al.isArc(n1, n4) + " Should be still FALSE");
        al.removeArc(n3, n2);
        System.out.println("(3,2) ? " + al.isArc(n3, n2) + " Should be FALSE");
        System.out.println("Is the arc still in the list of successors of the 'from' arc ? " + n3.getArcSucc().contains(new Arc(n3, n2)) + " Should be FALSE");
        System.out.println("Is the arc still in the list of predecessors of the 'to' arc ? " + n2.getArcPred().contains(new Arc(n3, n2)) + " Should be FALSE");
        System.out.println("Is the arc still in the list of arcs of the graph ? " + al.arcs.contains(new Arc(n3, n2)) + " Should be FALSE");
        System.out.println("\n/*----------------*/\n");

        System.out.println("Add Arc tests :");
        al.addArc(n4, n0);
        System.out.println("(4,0) ? " + al.isArc(n4, n0) + " Should be still TRUE");
        al.addArc(n1, n4);
        System.out.println("(1,4) ? " + al.isArc(n1, n4) + " Should be TRUE");
        System.out.println("Is the arc added in the list of successors of the 'from' arc ? " + n1.getArcSucc().contains(new Arc(n1, n4)) + " Should be TRUE");
        System.out.println("Is the arc added in the list of predecessors of the 'to' arc ? " + n4.getArcPred().contains(new Arc(n1, n4)) + " Should be TRUE");
        System.out.println("Is the arc added in the list of arcs of the graph ? " + al.arcs.contains(new Arc(n1, n4)) + " Should be TRUE");

        System.out.println("\n/*----------------*/\n");

        System.out.println("Compute Inverse tests :");
        AdjacencyListDirectedGraph la = al.computeInverse();
        System.out.println("(4,1) ? " + la.isArc(n4, n1) + " Should be TRUE");
        System.out.println("(1,4) ? " + la.isArc(n1, n4) + " Should be FALSE");
        System.out.println("Node 2 of inverseGraph should have 2 successors ? Number of successors -> " + la.nodes.get(2).getNbSuccs());

        al.addArc(n4, n1);
        al.addArc(n1, n4);

        la = al.computeInverse();

        System.out.println("(4,1) ? " + la.isArc(n4, n1) + " Should be TRUE");
        System.out.println("(1,4) ? " + la.isArc(n1, n4) + " Should be TRUE");

        al.removeArc(n4, n1);
        al.removeArc(n1, n4);

        la = al.computeInverse();

        System.out.println("(4,1) ? " + la.isArc(n4, n1) + " Should be FALSE");
        System.out.println("(1,4) ? " + la.isArc(n1, n4) + " Should be FALSE");

        System.out.println("\n/*----------------*/\n");

        System.out.println("To Adjacency Matrix tests :");
        al.removeArc(n4, n0);
        System.out.println("(1,4) ? " + al.isArc(n1, n4) + " Should be still FALSE");
        al.removeArc(n3, n2);
        System.out.println("(3,2) ? " + al.isArc(n3, n2) + " Should be FALSE");
        System.out.println("Is the arc still in the list of successors of the 'from' arc ? " + n3.getArcSucc().contains(new Arc(n3, n2)) + " Should be FALSE");
        System.out.println("Is the arc still in the list of predecessors of the 'to' arc ? " + n2.getArcPred().contains(new Arc(n3, n2)) + " Should be FALSE");
        System.out.println("Is the arc still in the list of arcs of the graph ? " + al.arcs.contains(new Arc(n3, n2)) + " Should be FALSE");
        System.out.println("\n/*----------------*/\n");
    }
}
