import edu.princeton.cs.algs4.In;

import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. That is, adds an edge
       in ONE directions, from v1 to v2. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. That is, adds an edge
       in BOTH directions, from v1 to v2 and from v2 to v1. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        if (!isAdjacent(v1, v2)) {
            LinkedList<Edge> initNeighbor = adjLists[v1];
            initNeighbor.add(new Edge(v1, v2, weight));
        } else {
            LinkedList<Edge> v1Edges = adjLists[v1];
            for (Edge edge : v1Edges) {
                if (edge.to == v2) {
                    edge.weight = weight;
                }
            }
        }
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        if (!isAdjacent(v1, v2)) {
            addEdge(v1, v2, weight);
            addEdge(v2, v1, weight);
        }
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        for (Edge edge : adjLists[from]) {
            if (edge.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        ArrayList<Integer> neighborVertices = new ArrayList<>();
        for (Edge edge : adjLists[v]) {
            neighborVertices.add(edge.to);
        }
        return neighborVertices;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int count = 0;
        for (LinkedList<Edge> list : adjLists) {
            for (Edge edge : list) {
                if (edge.to == v) {
                    count++;
                }
            }
        }
        return count;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        ArrayList<Integer> visited = new ArrayList<>();
        Iterator<Integer> iter = new DFSIterator(start);
        while (iter.hasNext()) {
            visited.add(iter.next());
        }
        return visited.contains(stop);
    }

    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        List<Integer> path = new ArrayList<>();
        HashSet<Integer> visited = new HashSet<>();
        boolean foundPath = dfs(start, stop, visited, path);
        if (!foundPath) {
            return new ArrayList<>(); // No path exists, return an empty list
        }
        return path;
    }

    private boolean dfs(int current, int stop, HashSet<Integer> visited, List<Integer> path) {
        visited.add(current);
        path.add(current);
        if (current == stop) {
            return true;
        }
        List<Integer> neighbors = neighbors(current);
        for (int neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, stop, visited, path)) {
                    return true;
                }
            }
        }
        path.remove(path.size() - 1);
        return false;
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private Integer[] currentInDegree;
        private HashSet<Integer> visited;
        private Integer vCurrent;

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            currentInDegree = new Integer[adjLists.length]; // Number of vertices in the graph.
            visited = new HashSet<>();
            int degree;

            for (int i = 0; i < adjLists.length; i++) {
                degree = inDegree(i);
                currentInDegree[i] = degree;
                if (degree == 0) {
                    fringe.push(i); // It has no incoming edges so added to the fringe stack.
                }
            }
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            // Retrieve the next vertex from the 'fringe' stack (vertex with in-degree 0)
            vCurrent = fringe.pop();

            // Decrement the in-degree of the neighbors of 'vCurrent' since we are visiting 'vCurrent'
            for (Edge edge : adjLists[vCurrent]) {
                int neighbor = edge.to;
                currentInDegree[neighbor]--;
            }

            // Mark the current vertex 'vCurrent' as visited
            visited.add(vCurrent);

            // Add new vertices with in-degree 0 to the 'fringe' stack
            for (int i = 0; i < adjLists.length; i++) {
                // Check if 'i' is not visited, not already in the 'fringe', and has in-degree 0
                if (!visited.contains(i) && !fringe.contains(i) && currentInDegree[i] == 0) {
                    fringe.push(i); // Add 'i' to the 'fringe' stack
                }
            }

            // Return the current vertex 'vCurrent'
            return vCurrent;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }
    }

    public ArrayList<Integer> shortestPath(int start, int stop) {
        PriorityQueue<Vertex> fringe = new PriorityQueue<>();
        boolean[] isVisited = new boolean[vertexCount];

        HashMap<Integer, Integer> routeTable = new HashMap<>();
        int[] distanceTable = new int[vertexCount];
        Arrays.fill(distanceTable, Integer.MAX_VALUE);

        fringe.add(new Vertex(start, 0));
        routeTable.put(start, start);
        distanceTable[start] = 0;

        while (!fringe.isEmpty()) {
            Vertex currVertex = fringe.poll();
            int currNode = currVertex.node;
            for (Edge edges : adjLists[currVertex.getNode()]) {
                if (!isVisited[edges.to]) {
                    int newDistance = edges.weight + distanceTable[currNode];
                    if (newDistance < distanceTable[edges.to]) {
                        fringe.add(new Vertex(edges.to, newDistance));
                        distanceTable[edges.to] = newDistance;
                        routeTable.put(edges.to, currNode);
                    }
                }
            }
            isVisited[currNode] = true;
        }

        if (routeTable.containsKey(stop)) {
            ArrayList<Integer> reversed = new ArrayList<>();
            int pointer = stop;
            while (pointer != start) {
                reversed.add(pointer);
                pointer = routeTable.get(pointer);
            }
            reversed.add(start);
            Collections.reverse(reversed);
            return reversed;
        } else {
            return null;
        }
    }

    class Vertex implements Comparable<Vertex> {
        private int node;
        private int distance;

        public Vertex(int node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(Vertex other) {
            return Integer.compare(this.distance, other.distance);
        }

        public int getNode() {
            return node;
        }
    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();
    }
}
