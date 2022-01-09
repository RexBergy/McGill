import java.util.*;
import java.io.File;

public class FordFulkerson {



        public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
                ArrayList<Integer> path = new ArrayList<Integer>();
                /* YOUR CODE GOES HERE*/
                int nodesNb = graph.getNbNodes();
                boolean visited[] = new boolean[nodesNb];
                for (int i=0; i<nodesNb; i++) {
                        visited[i] = false;
                }

                ArrayList<Edge> edgeList = graph.getEdges();

                Stack<Integer> stack = new Stack<Integer>();
                stack.push(source);
                visited[source] = true;


                while(stack.peek() != destination) {
                        int s = stack.peek();
                        ArrayList<Edge> neighbors = new ArrayList<Edge>();
                        ArrayList<Integer> neighNodes = new ArrayList<Integer>();
                        for (Edge e: edgeList) {
                                if((e.nodes[0]==s) && (e.weight>0) && (visited[e.nodes[1]] == false)) {
                                        neighbors.add(e);
                                        neighNodes.add(e.nodes[1]);
                                        //stack.push(e.nodes[1]);
                                }
                        }
                        if (neighNodes.contains(destination)) {
                        	stack.push(destination);
                        } else {
                        
                        	for (int n: neighNodes) {
                        		if (visited[n] == false) {
                        			visited[n] = true;
                        			stack.push(n);
                        			break;
                        		}
                        	}
                        }
                         if (neighNodes.isEmpty()) {
                                 stack.pop();

                            }
                         if (stack.isEmpty()) {
                        	 break;
                         }
                                
                        
                        /*if (neighNodes.isEmpty() != true) {
                                if (((neighNodes.size() == 1) && (neighNodes.contains(s)))){

                                } else {
                                        path.add(s);
                                }

                        }*/

                }
                while(stack.isEmpty()==false) {
                	int s = stack.pop();
                	path.add(0, s);
                }
                

                if (path.contains(destination) == false) {
                        path.clear();
                }

                return path;
        }



        public static String fordfulkerson( WGraph graph){
                String answer="";
                int maxFlow = 0;

                /* YOUR CODE GOES HERE          */
                graph = new WGraph(graph);
                ArrayList<Integer> path = pathDFS(graph.getSource(),graph.getDestination(),graph);

                while (path.isEmpty() == false) {
                        //path = pathDFS(graph.getSource(),graph.getDestination(),graph);
                        /*System.out.println(path);
                        for (Edge e: graph.getEdges()) {
                        	System.out.println(e.weight);
                        	System.out.println(e.nodes[0]+"  "+e.nodes[1]);
                        }*/
                        int flow = 1000000;
                        for (int i=0;i<path.size()-1;i++) {
                                int node1 = path.get(i);
                                int node2 = path.get(i+1);
                                int capacity = graph.getEdge(node1, node2).weight;
                                if (flow>capacity) {
                                        flow = capacity;
                                }

                        }


                        for (int i=0;i<path.size()-1;i++) {
                                int node1 = path.get(i);
                                int node2 = path.get(i+1);
                                int capacity = graph.getEdge(node1, node2).weight-flow;
                                graph.setEdge(node1, node2, capacity);
                                Edge edgecpy = null;
                                for (Edge e: graph.getEdges()) {
                                	if ((e.nodes[0] == node2) && (e.nodes[1] == node1)) {
                                		edgecpy = e;
                                	}
                                }
                                
                                if (edgecpy == null) {
                                	Edge edge = new Edge(node2,node1,flow);
                                	graph.addEdge(edge);
                                } else {
                                	int newCap = graph.getEdge(node2,node1).weight+flow;
                                	graph.setEdge(node2, node1, newCap);
                                }
                                //int edgeCap = edgecpy.weight;
                                
                                
                                /*if (graph.getEdges().contains(edgecpy) != true){
                                        graph.addEdge(edge);
                                } else {
                                        graph.setEdge(node2, node1, flow);
                                }*/
                        }
                        maxFlow += flow;
                        path = pathDFS(graph.getSource(),graph.getDestination(),graph);
                }

                answer += maxFlow + "\n" + graph.toString();
                return answer;
        }



        public static void main(String[] args){
                String file = args[0];
                File f = new File(file);
                WGraph g = new WGraph(file);
                System.out.println(fordfulkerson(g));


               /* WGraph graph = new WGraph();
                Edge edge1 = new Edge(0,1,16);
                Edge edge2 = new Edge(0,2,13);
                Edge edge3 = new Edge(1,3,12);
                Edge edge4 = new Edge(2,1,4);
                //Edge edge12 = new Edge(2,6,4);
                Edge edge5 = new Edge(2,4,14);
                Edge edge6 = new Edge(3,2,9);
                Edge edge7 = new Edge(3,5,20);
                Edge edge9 = new Edge(4,3,7);
                Edge edge10 = new Edge(4,5,4);*/
                /*Edge edge11 = new Edge(2,0,2);
                Edge edge12 = new Edge(4,2,7);
                Edge edge13 = new Edge(5,4,4);
                Edge edge14 = new Edge(3,4,7);
                Edge edge15 = new Edge(5,3,2);
                Edge edge16 = new Edge(1,2,2);
                Edge edge17 = new Edge(3,1,2);*/
                //Edge edge9 = new Edge(4,5,0);
                //Edge edge11 = new Edge(5,4,4);
                //Edge edge13 = new Edge(6,5,4);
               /* graph.addEdge(edge1);
                graph.addEdge(edge2);
                graph.addEdge(edge3);
                graph.addEdge(edge4);
                graph.addEdge(edge5);
                graph.addEdge(edge6);
                graph.addEdge(edge7);
                graph.addEdge(edge9);
                graph.addEdge(edge10);*/
                /*graph.addEdge(edge11);
                graph.addEdge(edge12);
                graph.addEdge(edge13);
                graph.addEdge(edge14);
                graph.addEdge(edge15);
                graph.addEdge(edge16);
                graph.addEdge(edge17);*/
               // graph.setDestination(5);
               // graph.setSource(0);
                //graph.addEdge(edge9);
                //graph.addEdge(edge11);
                
                /*graph.addEdge(edge12);
                graph.addEdge(edge13);*/
                //ArrayList<Integer> path = pathDFS(graph.getSource(),graph.getDestination(),graph);
                //String algo = fordfulkerson(graph);

                //System.out.println(graph.getEdges().contains(edge13));
                //System.out.println(path.get(0));
                //System.out.println(path.get(1));
                //System.out.println(graph.getEdge(path.get(0), path.get(1)).weight);
                //System.out.println(path.size());
                //System.out.println(graph.getEdges().contains(edge1));
                /*for (Edge e: graph.getEdges()) {
                        System.out.println(e.weight);
                        System.out.println(e.nodes[0]+"  "+e.nodes[1]);
                }*/
                //System.out.println(path);
                //String g = fordfulkerson(graph);
                //System.out.println(g);
               /* System.out.println(path);
                */

        }
}
        