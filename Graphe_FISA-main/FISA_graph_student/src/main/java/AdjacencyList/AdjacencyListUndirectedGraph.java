package AdjacencyList;

import java.util.ArrayList;
import java.util.List;

import AdjacencyMatrix.AdjacencyMatrixUndirectedGraph;
import GraphAlgorithms.GraphTools;
import Nodes_Edges.Edge;
import Nodes_Edges.UndirectedNode;


public class AdjacencyListUndirectedGraph {

	//--------------------------------------------------
    // 				Class variables
    //--------------------------------------------------

	protected List<UndirectedNode> nodes; // list of the nodes in the graph
	protected List<Edge> edges; // list of the edges in the graph
    protected int nbNodes; // number of nodes
    protected int nbEdges; // number of edges

    
    //--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------
    
	public AdjacencyListUndirectedGraph() {
		 this.nodes = new ArrayList<UndirectedNode>();
		 this.edges = new ArrayList<Edge>();
		 this.nbNodes = 0;
	     this.nbEdges = 0;
	}
		
	public AdjacencyListUndirectedGraph(List<UndirectedNode> nodes,List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
        this.nbNodes = nodes.size();
        this.nbEdges = edges.size();
        
    }

    public AdjacencyListUndirectedGraph(int[][] matrix) {
        this.nbNodes = matrix.length;
        this.nodes = new ArrayList<UndirectedNode>();
        this.edges = new ArrayList<Edge>();
        
        for (int i = 0; i < this.nbNodes; i++) {
            this.nodes.add(new UndirectedNode(i));
        }
        for (UndirectedNode n1 : this.getNodes()) {
            for (int j = n1.getLabel(); j < matrix[n1.getLabel()].length; j++) {
            	UndirectedNode n2 = this.getNodes().get(j);
                if (matrix[n1.getLabel()][j] != 0) {
                    Edge e1 = new Edge(n1,n2);
                    n1.addEdge(e1);
                    this.edges.add(e1);
                	n2.addEdge(new Edge(n2,n1));
                    this.nbEdges++;
                }
            }
        }
    }

    public AdjacencyListUndirectedGraph(AdjacencyListUndirectedGraph g) {
        super();
        this.nbNodes = g.getNbNodes();
        this.nbEdges = g.getNbEdges();
        this.nodes = new ArrayList<UndirectedNode>();
        this.edges = new ArrayList<Edge>();
        
        
        for (UndirectedNode n : g.getNodes()) {
            this.nodes.add(new UndirectedNode(n.getLabel()));
        }
        
        for (Edge e : g.getEdges()) {
        	this.edges.add(e);
        	UndirectedNode new_n   = this.getNodes().get(e.getFirstNode().getLabel());
        	UndirectedNode other_n = this.getNodes().get(e.getSecondNode().getLabel());
        	new_n.addEdge(new Edge(e.getFirstNode(),e.getSecondNode(),e.getWeight()));
        	other_n.addEdge(new Edge(e.getSecondNode(),e.getFirstNode(),e.getWeight()));
        }        
    }

    // ------------------------------------------
    // 				Accessors
    // ------------------------------------------
    
    /**
     * Returns the list of nodes in the graph
     */
    public List<UndirectedNode> getNodes() {
        return this.nodes;
    }
    
    /**
     * Returns the list of edges in the graph
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Returns the number of nodes in the graph
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
     * @return true if there is an edge between x and y
     */
    public boolean isEdge(UndirectedNode x, UndirectedNode y) {
        List<Edge> edgesOfX = x.getIncidentEdges();

        if (edgesOfX.size() == 0) {
            return false;
        }

        for (Edge e : edgesOfX) {
            if (e.getFirstNode().equals(y) || e.getSecondNode().equals(y)) {
                return true;
            }
        }

    	return false;
    }

    /**
     * Removes edge (x,y) if there exists one. And remove this edge and the inverse in the list of edges from the two extremities (nodes)
     */
    public void removeEdge(UndirectedNode x, UndirectedNode y) {
    	if(isEdge(x,y)){
            Edge e1 = new Edge(x,y);
            this.edges.remove(e1);
            this.getNodeOfList(x).getIncidentEdges().remove(e1);
            this.getNodeOfList(y).getIncidentEdges().remove(e1);
            this.nbEdges--;
        }
    }

    /**
     * Adds edge (x,y) if it is not already present in the graph, requires that nodes x and y already exist. 
     * And adds this edge to the incident list of both extremities (nodes) and into the global list "edges" of the graph.
     * In non-valued graph, every edge has a cost equal to 0.
     */
    public void addEdge(UndirectedNode x, UndirectedNode y) {
    	if(!isEdge(x,y)){
    		Edge e1 = new Edge(x,y);
            this.getEdges().add(e1);
            this.getNodeOfList(x).addEdge(e1);
            this.getNodeOfList(y).addEdge(e1);
            this.nbEdges++;
    	}
    }

    //--------------------------------------------------
    // 					Methods
    //--------------------------------------------------
    
    

    /**
     * @return the corresponding nodes in the list this.nodes
     */
    public UndirectedNode getNodeOfList(UndirectedNode v) {
        return this.getNodes().get(v.getLabel());
    }
    
    /**
     * @return a matrix representation of the graph 
     */
    public int[][] toAdjacencyMatrix() {
        int[][] matrix = new int[nbNodes][nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            for (int j = 0; j < nbNodes; j++) {
                if (this.isEdge(this.getNodes().get(i), this.getNodes().get(j))) {
                    matrix[i][j] = 1;
                    matrix[j][i] = 1;
                }
            }
        }
        return matrix;
    }

    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("List of nodes and their neighbours :\n");
        for (UndirectedNode n : this.nodes) {
            s.append("Node ").append(n).append(" : ");
            s.append("\nList of incident edges : ");
            for (Edge e : n.getIncidentEdges()) {
                s.append(e).append("  ");
            }
            s.append("\n");            
        }
        s.append("\nList of edges :\n");
        for (Edge e : this.edges) {
        	s.append(e).append("  ");
        }
        s.append("\n");
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] mat = GraphTools.generateGraphData(10, 20, false, true, false, 100001);

        //GraphTools.afficherMatrix(mat);
        AdjacencyListUndirectedGraph al = new AdjacencyListUndirectedGraph(mat);
        //System.out.println(al);
        //System.out.println("(n_2,n_5) is it in the graph ? " +  al.isEdge(al.getNodes().get(2), al.getNodes().get(5)));

        UndirectedNode n0 = al.nodes.get(0);
        UndirectedNode n1 = al.nodes.get(1);
        UndirectedNode n2 = al.nodes.get(2);
        UndirectedNode n3 = al.nodes.get(3);
        UndirectedNode n4 = al.nodes.get(4);
        UndirectedNode n5 = al.nodes.get(5);

        System.out.println("Is Edge tests :");
        System.out.println("{0,1} ? " + al.isEdge(n0, n1) + " Should be FALSE");
        System.out.println("{2,0} ? " + al.isEdge(n0, n1) + " Should be FALSE");
        System.out.println("{2,1} ? " + al.isEdge(n2, n1) + " Should be TRUE");
        System.out.println("{0,3} ? " + al.isEdge(n0, n3) + " Should be TRUE");
        System.out.println("{3,0} ? " + al.isEdge(n3, n0) + " Should be also TRUE");

        System.out.println("\n/*----------------*/\n");

        System.out.println("Remove Edge tests :");
        al.removeEdge(n0, n1);
        System.out.println("{0,1} ? " + al.isEdge(n0, n1) + " Should be still FALSE");

        al.removeEdge(n2, n1);
        System.out.println("{2,1} ? " + al.isEdge(n2, n1) + " Should be FALSE");
        System.out.println("{1,2} ? " + al.isEdge(n1, n2) + " Should be FALSE");
        System.out.println("{0,3} ? " + al.isEdge(n0, n3) + " Should be TRUE");
        System.out.println("{3,0} ? " + al.isEdge(n3, n0) + " Should be also TRUE");
        System.out.println("Is a new edge removed to incident list of the other edge ? " + n2.getIncidentEdges().stream().anyMatch(e -> e.getFirstNode().equals(n1) || e.getSecondNode().equals(n1)) + " Should be false");

        System.out.println("\n/*----------------*/\n");

        System.out.println("Add Edge tests :");
        al.addEdge(n0, n1);
        System.out.println("{0,1} ? " + al.isEdge(n0, n1) + " Should be TRUE");
        System.out.println("{1,0} ? " + al.isEdge(n1, n0) + " Should be TRUE");
        al.addEdge(n0, n3);
        System.out.println("{0,3} ? " + al.isEdge(n0, n3) + " Should be still TRUE");
        System.out.println("{3,0} ? " + al.isEdge(n3, n0) + " Should be still TRUE");
        al.addEdge(n0, n5);
        System.out.println("Is a new edge added to incident list of the other edgr ? " + n0.getIncidentEdges().stream().anyMatch(e -> e.getFirstNode().equals(n5) || e.getSecondNode().equals(n5)) + " Should be true");

        System.out.println("\n/*----------------*/\n");

        System.out.println("To Adjacency Matrix tests :");
        AdjacencyMatrixUndirectedGraph adjacencyMatrix = new AdjacencyMatrixUndirectedGraph(al.toAdjacencyMatrix());
        //System.out.println(adjacencyMatrix);

        System.out.println("{0,3} ? " + adjacencyMatrix.isEdge(0, 1) + " Should be TRUE");
        System.out.println("{2,9} ? " + adjacencyMatrix.isEdge(2, 9) + " Should be TRUE");
        System.out.println("{2,8} ? " + adjacencyMatrix.isEdge(2, 8) + " Should be FALSE");
        System.out.println("{6,3} ? " + adjacencyMatrix.isEdge(6, 3) + " Should be FALSE");
        System.out.println("{6,5} ? " + adjacencyMatrix.isEdge(6, 5) + " Should be TRUE");

        System.out.println("\n/*----------------*/\n");
    }

}
