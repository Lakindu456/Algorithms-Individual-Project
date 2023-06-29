
//////////////////////////////////////////////////////////////////////////////////////////
/* 5SENG003W Algorithms – Coursework (2022/23)
*  Student Name :- R.G.Lakindu Indipa
*  UOW Student ID :- w1912890
* IIT Student ID :- 20210887 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// Directed Graph
public class Main {
    private final int vertices;
    private final List<List<Integer>> adjacencyList;

    public Main(int vertices) {
        this.vertices = vertices;
        adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
    }

    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }

    public boolean isCyclicUtil(int vertex, boolean[] visited, boolean[] recursionStack, List<Integer> cycle) {
        visited[vertex] = true;
        recursionStack[vertex] = true;

        List<Integer> neighbors = getNeighbors(vertex);
        for (Integer neighbor : neighbors) {
            if (!visited[neighbor]) {
                // Adding the neighbor to the cycle path and check if there is a cycle
                cycle.add(neighbor);
                if (isCyclicUtil(neighbor, visited, recursionStack, cycle)) {
                    return true;
                }
                // If there is no cycle, remove the neighbor by cycle path
                cycle.remove(cycle.size() - 1);
            } else if (recursionStack[neighbor]) {
                cycle.add(neighbor);
                // Adding the current heading to complete the circular path
                cycle.add(vertex);
                return true;
            }
        }

        recursionStack[vertex] = false;
        return false;
    }

    public boolean isCyclic() {
        boolean[] visited = new boolean[vertices];
        boolean[] recursionStack = new boolean[vertices];

        for (int i = 0; i < vertices; i++) {
            if (isCyclicUtil(i, visited, recursionStack, new ArrayList<>())) {
                return true;
            }
        }

        return false;
    }

    public List<Integer> findSinks() {
        List<Integer> sinks = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            if (adjacencyList.get(i).isEmpty()) {
                sinks.add(i);
            }
        }

        return sinks;
    }
    // Removing all edges leading to the sink
    public void removeSink(int sink) {
        for (List<Integer> neighbors : adjacencyList) {
            neighbors.remove(Integer.valueOf(sink));
        }
    }
    public void printGraph() {
        for (int i = 0; i < vertices; i++) {
            List<Integer> neighbors = adjacencyList.get(i);
            System.out.print(i + " -> ");
            if (neighbors.isEmpty()) {
                System.out.println();
            } else {
                for (Integer neighbor : neighbors) {
                    System.out.print(neighbor + " ");
                }
                System.out.println();
            }
        }
    }
    public static void main(String[] args) {
        Main graph = new Main(6);

        // Read graph description from my input file and add edges in the graph
        String inputFile = "/Users/lakinduindipa/Desktop/Algorithms CW/20210887_Lakindu_Indipa/DirectedGraph/src/input.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" -> ");
                int source = Integer.parseInt(parts[0]);
                String[] destinations = parts[1].split(" ");
                for (String destination : destinations) {
                    graph.addEdge(source, Integer.parseInt(destination));
                }
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Initial Graph:");
        graph.printGraph();

        List<Integer> sinks = graph.findSinks();
        System.out.println("Sinks Found: " + sinks.size());

        for (Integer sink : sinks) {
            System.out.println("Removing Sink: " + sink);
            graph.removeSink(sink);
        }

        System.out.println("Graph after removing sinks:");
        graph.printGraph();

        boolean isaCyclic = graph.isCyclic();
        System.out.println("isaCyclic -> " + (isaCyclic ? "Yes" : "No"));

        if (isaCyclic) {
            System.out.println("Cycle found:");
            List<Integer> cycle = new ArrayList<>();
            // Start vertex of the cycle is 0
            int startVertex = 0;
            graph.isCyclicUtil(startVertex, new boolean[graph.vertices], new boolean[graph.vertices], cycle);
            // Remove last two elements from the cycle path
            for (Integer vertex : cycle.subList(0, cycle.size() - 2)) {
                System.out.print(vertex + "->");
            }
            // Print the second-to-last element of the cycle path
            System.out.println(cycle.get(cycle.size() - 2));
        }

        // Doubling Hypothesis
        int initialSize = graph.vertices;
        long startTime, endTime;
        System.out.println("Doubling Hypothesis:");

        for (int i = 0; i < 5; i++) {
            Main doublingGraph = new Main(initialSize * 2);

            // Perform the same operations as before on the doubled input size
            // ...

            // Measure the execution time
            startTime = System.nanoTime();
            boolean doublingIsCyclic = doublingGraph.isCyclic();
            endTime = System.nanoTime();

            long duration = endTime - startTime;

            System.out.println("Input Size: " + doublingGraph.vertices);
            System.out.println("⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟⍟");
            System.out.println("Execution Time: " + duration + " nanoseconds");
            System.out.println("✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡✡");
            System.out.println("isCyclic: " + (doublingIsCyclic ? "Yes" : "No"));

            initialSize *= 2;
        }
    }

}