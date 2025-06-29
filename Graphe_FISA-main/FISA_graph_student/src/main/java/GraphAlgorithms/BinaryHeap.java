package GraphAlgorithms;


public class BinaryHeap {

    private int[] nodes;
    private int pos;

    public BinaryHeap() {
        this.nodes = new int[32];
        for (int i = 0; i < nodes.length; i++) {
            this.nodes[i] = Integer.MAX_VALUE;
        }
        this.pos = 0;
    }

    public void resize() {
        int[] tab = new int[this.nodes.length + 32];
        for (int i = 0; i < nodes.length; i++) {
            tab[i] = Integer.MAX_VALUE;
        }
        System.arraycopy(this.nodes, 0, tab, 0, this.nodes.length);
        this.nodes = tab;
    }

    public boolean isEmpty() {
        return pos == 0;
    }

    public boolean insert(int element) {
        if (pos >= nodes.length) {
            resize();
        }

        this.nodes[pos] = element;
        int actualPosition = pos;
        pos++;

        while (true) {


            if (this.nodes[actualPosition] < this.nodes[(actualPosition -1) /2 ]) {
                this.swap(actualPosition, (actualPosition -1) /2);
                actualPosition = (actualPosition - 1) / 2;
            } else {
                break;
            }
        }

        return true;
    }

    public int remove() {
    	if (this.isEmpty()) {
            return -1;
        }

        int removed = nodes[0];
        pos--;
        swap(0, pos);

        nodes[pos] = Integer.MAX_VALUE;
        int current = 0;
        int bestChild = getBestChildPos(current);
        while (bestChild != Integer.MAX_VALUE && nodes[bestChild] < nodes[current]) {
            swap(current, bestChild);
            current = bestChild;
            bestChild = getBestChildPos(current);
        }

        return removed;
    }

    private int getBestChildPos(int src) {
        if (isLeaf(src)) {
            return Integer.MAX_VALUE;
        } else {
            int left = 2 * src + 1;
            int right = 2 * src + 2;

            if (right >= pos) {
                return left;
            }

            return (nodes[left] <= nodes[right]) ? left : right;
        }
    }

    
    /**
	 * Test if the node is a leaf in the binary heap
	 * 
	 * @returns true if it's a leaf or false else
	 * 
	 */
    private boolean isLeaf(int src) {
        return (2 * src + 1) >= pos;
    }

    private void swap(int father, int child) {
        int temp = nodes[father];
        nodes[father] = nodes[child];
        nodes[child] = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (int i = 0; i < pos; i++) {
            s.append(nodes[i]);
            if (i < pos - 1) {
                s.append(", ");
            }
        }
        s.append("]");
        return s.toString();
    }

    public String toStringTree() {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        int elementsInLevel = 1;
        int index = 0;

        while (index < pos) {
            sb.append("Niveau ").append(level).append(" : ");
            int count = 0;
            while (count < elementsInLevel && index < pos) {
                sb.append(nodes[index]).append(" ");
                count++;
                index++;
            }
            sb.append("\n");
            level++;
            elementsInLevel *= 2; // chaque niveau a 2 fois plus d’éléments que le précédent
        }

        return sb.toString();
    }

    /**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @returns a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    public boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
        if (isLeaf(root)) {
            return true;
        }

        int left = 2 * root + 1;
        int right = 2 * root + 2;

        boolean validLeft = true;
        if (left < pos && left < nodes.length) {
            validLeft = nodes[left] >= nodes[root] && testRec(left);
        }

        boolean validRight = true;
        if (right < pos && right < nodes.length) {
            validRight = nodes[right] >= nodes[root] && testRec(right);
        }

        return validLeft && validRight;
    }

    private boolean isCompact() {
        for (int i = 0; i < pos; i++) {
            if (nodes[i] == Integer.MAX_VALUE) {
                return false; // trou détecté
            }
        }
        return true;
    }

    public static void main(String[] args) {
        BinaryHeap binH = new BinaryHeap();
        System.out.println("Is empty? => " + binH.isEmpty()+"\n");
        int k = 20;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));
            binH.insert(rand);
            k--;
        }
        // A completer
        System.out.println("\n/*----------------*/");
        System.out.println("Binary Heap tests");
        System.out.println(binH.toStringTree());
        System.out.println("Is the BH Empty? (FALSE expected) => " + binH.isEmpty()+"\n");
        System.out.println("Is the BH compact? (TRUE expected) => " + binH.isCompact()+"\n");
        System.out.println("Is the BH well ordered? (TRUE expected) => " + binH.testRec(0)+"\n");

        System.out.println("\n/*----------------*/");
        System.out.println("Remove tests");
        System.out.println(binH.toStringTree());
        binH.remove();
        System.out.println(binH.toStringTree());
        System.out.println("Is the BH compact? (TRUE expected) => " + binH.isCompact()+"\n");
        System.out.println("Is the BH well ordered? (TRUE expected) => " + binH.testRec(0)+"\n");
        binH.remove();
        System.out.println(binH.toStringTree());
        System.out.println("Is the BH compact? (TRUE expected) => " + binH.isCompact()+"\n");
        System.out.println("Is the BH well ordered? (TRUE expected) => " + binH.testRec(0)+"\n");
    }

}
