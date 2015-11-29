package main;


import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Main {

    private static final Graph.Edge[] GRAPH = new Graph.Edge[3734];
    int total;
    
    public static void main(String[] args) {
        try {
            String v1 = "";
            String v2 = "";
            int peso = 0;
            String patch = "dijkstraData.txt";
            String delimiter = "\\||\\n";
            //String delimiter = " ";
            Scanner scanner = new Scanner(new FileReader(patch))
                    .useDelimiter(delimiter);
            int linecont = 1;
            int totalaresta = 0;
            while (scanner.hasNext()) {
                String nome = scanner.next();
                for (int i = 0; i < nome.split(" ").length; i++) {
                    if (i == 0) {
                        v1 = nome.split(" ")[i];
                    } else {
                        v2 = nome.split(" ")[i].split(",")[0];
                        peso = Integer.parseInt(nome.split(" ")[i].split(",")[1]);
                    }
                    if (i > 0) {
                        GRAPH[totalaresta] = new Graph.Edge(v1.trim(), v2.trim(), peso);
                        //System.out.println("linha "+ totalaresta +" -> Vertice:" + GRAPH[totalaresta].v1 +", Vertice:" + GRAPH[totalaresta].v2 + " = peso " + GRAPH[totalaresta].dist);
                        totalaresta++;

                    }
                }
                linecont++;

            }
            //System.out.println(totalaresta);
            //System.out.println(GRAPH.toString());
            
            String procurar = "7,37,59,82,99,115,133,165,188,197";            
            Graph g = new Graph(GRAPH);
            String imptotalcaminho = "";
            for(int i = 0; i < procurar.split(",").length ; i++){
                String START = (String) GRAPH[0].v1;
                g.dijkstra(START);
                g.printPath(procurar.split(",")[i]);
                System.out.println(Graph.caminho);
                //System.out.println(Graph.total);
                imptotalcaminho += String.valueOf(Graph.total);
                if( i < (procurar.split(",").length -1)){
                    imptotalcaminho += ", ";
                }
            }
            System.out.println();
            
            System.out.println("Vertices procurados: "+procurar);
            System.out.println("Pesos: " +imptotalcaminho);
            /*String START = "7";
            //String START = (String) GRAPH[0].v1;
            String END = "37";
            Graph g = new Graph(GRAPH);            
            
            //g.printAllPaths();
            System.out.println(Graph.caminho);
            System.out.println(Graph.total);*/
      
        } catch (Exception ex) {
            
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }

    }
}

class Graph {
    public static int total = 0;
    public static String caminho = "";
    private final Map<String, Vertex> graph; 
    
    public static class Edge {

        public final String v1, v2;
        public final int dist;

        public Edge(String v1, String v2, int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /**
     * One vertex of the graph, complete with mappings to neighbouring vertices
     */
    public static class Vertex implements Comparable<Vertex> {
        

        public final String name;
        public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Integer> neighbours = new HashMap<>();

        public Vertex(String name) {
            this.name = name;
        }

        private void printPath() {
            if (this == this.previous) {
                //System.out.printf("%s", this.name);
                caminho += this.name;
                
            } else if (this.previous == null) {
                System.out.printf("%s(1000000)", this.name);
            } else {
                this.previous.printPath();
                //System.out.printf(" -> %s(%d)", this.name, this.dist);                
                total = this.dist;
                caminho += " ->  "+ this.name+"(" + this.dist +")";
            }
        }

        public int compareTo(Vertex other) {
            return Integer.compare(dist, other.dist);
        }
    }

    
    public Graph(Edge[] edges) {
        graph = new HashMap<>(edges.length);

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) {
                graph.put(e.v1, new Vertex(e.v1));
            }
            if (!graph.containsKey(e.v2)) {
                graph.put(e.v2, new Vertex(e.v2));
            }
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            //graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }
    }

    /**
     * Runs dijkstra using a specified source vertex
     */
    public void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<>();

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /**
     * Implementation of dijkstra's algorithm using a binary heap.
     */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Integer.MAX_VALUE) {
                break; // we can ignore u (and any other remaining vertices) since they are unreachable
            }
            //look at distances to each neighbour
            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final int alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    /**
     * Prints a path from the source to the specified vertex
     */
    public void printPath(String endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
            return;
        }

        graph.get(endName).printPath();
        //System.out.println();
    }

    /**
     * Prints the path from the source to every vertex (output order is not
     * guaranteed)
     */
    public void printAllPaths() {
        for (Vertex v : graph.values()) {
            v.printPath();
            System.out.println();
        }
    }
}
