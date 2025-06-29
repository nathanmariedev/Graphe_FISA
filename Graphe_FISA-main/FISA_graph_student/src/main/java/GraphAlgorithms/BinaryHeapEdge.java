package GraphAlgorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Nodes_Edges.DirectedNode;
import Nodes_Edges.Edge;
import Nodes_Edges.UndirectedNode;

public class BinaryHeapEdge {

	/**
	 * A list structure for a faster management of the heap by indexing
	 * 
	 */
	private  List<Edge> binh;

    public BinaryHeapEdge() {
        this.binh = new ArrayList<Edge>();
    }

    public boolean isEmpty() {
        return binh.isEmpty();
    }

    /**
	 * Insert a new edge in the binary heap
	 * 
	 * @param from one node of the edge
	 * @param to one node of the edge
	 * @param val the edge weight
	 */
    public void insert(UndirectedNode from, UndirectedNode to, int val) {
		Edge e = new Edge(from, to, val);
		this.binh.add(e);
		int actueal = this.binh.size() - 1;

		while (actueal > 0) {
			int parent = (actueal - 1) / 2;

			if (binh.get(actueal).getWeight() < binh.get(parent).getWeight()) {
				this.swap(actueal, parent);
				actueal = parent;
			} else {
				break;
			}
		}
    }

    
    /**
	 * Removes the root edge in the binary heap, and swap the edges to keep a valid binary heap
	 * 
	 * @return the edge with the minimal value (root of the binary heap)
	 * 
	 */
	public Edge remove() {
		if (isEmpty()) {
			return null;
		}

		Edge min = binh.get(0);
		int last = binh.size() - 1;
		Edge edge = binh.remove(last);
		if (!isEmpty()) {
			binh.set(0, edge);

			int pos = 0;
			int child = getBestChildPos(pos);
			while (child != Integer.MAX_VALUE && binh.get(pos).getWeight() > binh.get(child).getWeight()) {
				swap(pos, child);
				pos = child;
				child = getBestChildPos(pos);

			}
		}

		return min;


	}




	/**
	 * From an edge indexed by src, find the child having the least weight and return it
	 * 
	 * @param src an index of the list edges
	 * @return the index of the child edge with the least weight
	 */
    private int getBestChildPos(int src) {
		int left = 2 * src + 1;
		int right = 2 * src + 2;

		if (left >= binh.size()) {
			return Integer.MAX_VALUE;
		}

		if (right >= binh.size()) {
			return left;
		}

		return (binh.get(left).getWeight() <= binh.get(right).getWeight()) ? left : right;
    }

    private boolean isLeaf(int src) {
		boolean isALEaf = this.binh.size() <= (2 * src +1);
		return isALEaf;
    }

    
    /**
	 * Swap two edges in the binary heap
	 * 
	 * @param father an index of the list edges
	 * @param child an index of the list edges
	 */
    private void swap(int father, int child) {         
    	Edge temp = binh.get(father);
    	binh.get(father).setFirstNode(binh.get(child).getFirstNode());
    	binh.get(father).setSecondNode(binh.get(child).getSecondNode());
    	binh.get(father).setWeight(binh.get(child).getWeight());
    	binh.get(child).setFirstNode(temp.getFirstNode());
    	binh.get(child).setSecondNode(temp.getSecondNode());
    	binh.get(child).setWeight(temp.getWeight());
    }

    
    /**
	 * Create the string of the visualisation of a binary heap
	 * 
	 * @return the string of the binary heap
	 */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Edge no: binh) {
            s.append(no).append(", ");
        }
        return s.toString();
    }
    
    
    private String space(int x) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<x; i++) {
			res.append(" ");
		}
		return res.toString();
	}
	
	/**
	 * Print a nice visualisation of the binary heap as a hierarchy tree
	 * 
	 */	
	public void lovelyPrinting(){
		if (this.binh.isEmpty()) {
			return;
		}
		int nodeWidth = this.binh.get(0).toString().length();
		int depth = 1+(int)(Math.log(this.binh.size())/Math.log(2));
		int index=0;


		
		for(int h = 1; h<=depth; h++){
			int left = ((int) (Math.pow(2, depth-h-1)))*nodeWidth - nodeWidth/2;
			int between = ((int) (Math.pow(2, depth-h))-1)*nodeWidth;
			int i =0;
			System.out.print(space(left));
			while(i<Math.pow(2, h-1) && index<binh.size()){
				System.out.print(binh.get(index) + space(between));
				index++;
				i++;
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	// ------------------------------------
    // 					TEST
	// ------------------------------------

	/**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @return a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    private boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
    	System.out.println("root= "+root);
    	int lastIndex = binh.size()-1; 
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            System.out.println("left = "+left);
            System.out.println("right = "+right);
            if (right >= lastIndex) {
                return binh.get(left).getWeight() >= binh.get(root).getWeight() && testRec(left);
            } else {
                return binh.get(left).getWeight() >= binh.get(root).getWeight() && testRec(left)
                    && binh.get(right).getWeight() >= binh.get(root).getWeight() && testRec(right);
            }
        }
    }

	public static List<Edge> prim(List<UndirectedNode> nodes, UndirectedNode start) {
		List<Edge> mst = new ArrayList<>();
		Set<UndirectedNode> visited = new HashSet<>();
		BinaryHeapEdge heap = new BinaryHeapEdge();

		visited.add(start);
		for (Edge edge : start.getIncidentEdges()) {
			heap.insert(edge.getFirstNode(), edge.getSecondNode(), edge.getWeight());
		}

		while (!heap.isEmpty() && visited.size() < nodes.size()) {
			Edge edge = heap.remove();
			UndirectedNode u = edge.getFirstNode();
			UndirectedNode v = edge.getSecondNode();

			if (visited.contains(u) && visited.contains(v)) continue;
			UndirectedNode next = visited.contains(u) ? v : u;
			visited.add(next);
			mst.add(edge);

			for (Edge e : next.getIncidentEdges()) {
				UndirectedNode neighbor = e.getSecondNode();
				if (!visited.contains(neighbor)) {
					heap.insert(e.getFirstNode(), e.getSecondNode(), e.getWeight());
				}
			}
		}

		return mst;
	}


	public static void main(String[] args) {
        BinaryHeapEdge jarjarBin = new BinaryHeapEdge();
        System.out.println(jarjarBin.isEmpty()+"\n");
        int k = 10;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));                        
            jarjarBin.insert(new UndirectedNode(k), new UndirectedNode(k+30), rand);            
            k--;
        }


		BinaryHeapEdge heap = new BinaryHeapEdge();

		System.out.println("==> Test insert() :");
		heap.insert(new UndirectedNode(0), new UndirectedNode(1), 12);
		heap.insert(new UndirectedNode(1), new UndirectedNode(2), 4);
		heap.insert(new UndirectedNode(2), new UndirectedNode(3), 9);
		heap.insert(new UndirectedNode(3), new UndirectedNode(4), 15);
		heap.insert(new UndirectedNode(4), new UndirectedNode(5), 2);

		System.out.println("Heap after insertin:");
		heap.lovelyPrinting();

		System.out.println("\n==> Test remove():");
		while (!heap.isEmpty()) {
			Edge removed = heap.remove();
			System.out.println("Removed edge: " + removed);
			System.out.println("Heap:");

			System.out.println("Valid? " + heap.test());
			System.out.println("---");
		}

		System.out.println("\n==> Test remove() on empty heap:");
		Edge e = heap.remove();
		System.out.println("Result: " + e +" ? Should be NULM");

		System.out.println("\n==> Test insert() again after empty:");
		heap.insert(new UndirectedNode(9), new UndirectedNode(10), 7);
		heap.insert(new UndirectedNode(11), new UndirectedNode(12), 3);
		System.out.println("Valid? " + heap.test());

		System.out.println("\n==> Test isLeaf() :");
		heap.insert(new UndirectedNode(20), new UndirectedNode(21), 8);
		heap.insert(new UndirectedNode(22), new UndirectedNode(23), 10);
		heap.insert(new UndirectedNode(24), new UndirectedNode(25), 12);
		heap.insert(new UndirectedNode(26), new UndirectedNode(27), 14);
		heap.insert(new UndirectedNode(28), new UndirectedNode(29), 16);

		System.out.println("isLeaf(0)? Should be FALSE" + heap.isLeaf(0));
		System.out.println("isLeaf(1)? Should be FALSE" + heap.isLeaf(1));
		System.out.println("isLeaf(last)? Should be TRUE" + heap.isLeaf(heap.binh.size() - 1));

		System.out.println("\n==> Test de Prim :");

		UndirectedNode a = new UndirectedNode(0);
		UndirectedNode b = new UndirectedNode(1);
		UndirectedNode c = new UndirectedNode(2);
		UndirectedNode d = new UndirectedNode(3);
		UndirectedNode e1 = new UndirectedNode(4);

		a.addEdge(new Edge(a, b, 4));
		b.addEdge(new Edge(b, a, 4));

		a.addEdge(new Edge(a, c, 1));
		c.addEdge(new Edge(c, a, 1));

		c.addEdge(new Edge(c, b, 3));
		b.addEdge(new Edge(b, c, 3));

		b.addEdge(new Edge(b, d, 2));
		d.addEdge(new Edge(d, b, 2));

		c.addEdge(new Edge(c, d, 5));
		d.addEdge(new Edge(d, c, 5));

		d.addEdge(new Edge(d, e1, 7));
		e1.addEdge(new Edge(e1, d, 7));


		List<UndirectedNode> graph = new ArrayList<>();
		graph.add(a);
		graph.add(b);
		graph.add(c);
		graph.add(d);
		graph.add(e1);

		List<Edge> mst = BinaryHeapEdge.prim(graph, a);

		System.out.println("Test prim ) :");
		int totalWeight = 0;
		for (Edge edge : mst) {
			System.out.println(edge);
			totalWeight += edge.getWeight();
		}
		System.out.println("Poid total : " + totalWeight );

	}

}

