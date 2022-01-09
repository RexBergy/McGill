import java.util.*;

public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException{
        public PathDoesNotExistException(String str){
            super(str);
        }
    }

    BellmanFord(WGraph g, int source) throws NegativeWeightException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         */
    	int vertices = g.getNbNodes();
    	
    	
    	this.source = source;
    	this.distances = new int[vertices];
    	this.predecessors = new int[vertices];
    	
    	for (int k=0;k<vertices;k++) {
    		this.distances[k] = Integer.MAX_VALUE;
    		this.predecessors[k] = Integer.MAX_VALUE;
    	}
    	distances[source] = 0;
    	
    	for (int i=0;i<vertices-1;i++) {
    		for (Edge e: g.getEdges()) {
    			int distU = distances[e.nodes[0]];
    			int distV = distances[e.nodes[1]];
    			int weight = e.weight;
    			if ((distU + weight < distV) && (distU != Integer.MAX_VALUE)) {
    				distances[e.nodes[1]] = distU + weight;
    				predecessors[e.nodes[1]] = e.nodes[0];
    			}
    		}
    	}
    	
    	for (Edge e: g.getEdges()) {
    		int distU = distances[e.nodes[0]];
			int distV = distances[e.nodes[1]];
			int weight = e.weight;
    		if ((distU != Integer.MAX_VALUE) && (distV > distU + weight)) {
    			throw new NegativeWeightException("BellmanFord constructor detected negative-weight cycles");
    		}
    	}

    }

    public int[] shortestPath(int destination) throws PathDoesNotExistException{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Error is thrown
         */
    	/*int[] path = new int[this.distances.length];
    	if (distances[destination] != Integer.MAX_VALUE) {
    		int curr_node = destination;
    		int i = 0;
    		while (curr_node != this.source) {
    			path[i] = curr_node;
    			curr_node = predecessors[curr_node];
    			i++;
    		}
    		path[i] = curr_node;
    		for (int j = 0; j <path.length/2;j++) {
    			int temp = path[i];
    			path[i] = path[path.length -j -1];
    			path[path.length -j -1] = temp;
    		}
    		
    	} else {
    		throw new PathDoesNotExistException("The destination is unreachable from the source");
    	}*/
    	
    	int[] path;
    	
    	if (this.distances[destination] != Integer.MAX_VALUE) {
    		int curr_node = destination;
    		int size = 1;
    		while (curr_node != this.source){
    			size++;
    			curr_node = this.predecessors[curr_node];
    		}
    		path = new int[size];
    		curr_node = destination;
    		if (curr_node == this.source) {
    			path[0] = destination;
    		} else {
    			for (int j = 0; j<path.length;j++) {
    				path[path.length-j-1] = curr_node;
    				curr_node = this.predecessors[curr_node];
    			}
    		}
    	} else {
    		throw new PathDoesNotExistException("The destination is unreachable from the source");
    	}
    	
    	
    	
    	
        return path;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }
    	
    	//int math[] = new int[0];
    	//int i = 0;
    	//while (i != 5) {
    	//	math[i] = 5;
    	//	i++;
    	//}
    	//System.out.println(math.length);

   } 
}