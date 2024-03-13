import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

public class MSTTest {

    private final static String SEPARATOR = System.getProperty("file.separator");
    private final static String INPUT_FOLDER = System.getProperty("user.dir") + SEPARATOR + "test" + SEPARATOR + "inputs";
    private final static String NORMAL = INPUT_FOLDER + SEPARATOR + "graphTestNormal.in";
    private final static String ALL_DISJOINT = INPUT_FOLDER + SEPARATOR + "graphTestAllDisjoint.in";
    private final static String SOME_DISJOINT = INPUT_FOLDER + SEPARATOR + "graphTestSomeDisjoint.in";
    private final static String MULTI_EDGE = INPUT_FOLDER + SEPARATOR + "graphTestMultiEdge.in";

    @Test
    public void testBasic() {
        Graph g = loadFromText(NORMAL);
        // TODO: test it out!
    }

    @Test
    public void testPrims() {
        Graph g = loadFromText(NORMAL);
        Graph mst = g.prims(0);
        assertThat(isTree(mst)).isTrue();
        assertThat(spans(mst, g)).isTrue();
    }
    @Test
    public void testKruskals() {
        Graph g = loadFromText(NORMAL);
        Graph mst = g.kruskals();
        assertThat(isTree(mst)).isTrue();
        assertThat(spans(mst, g)).isTrue();
    }

    @Test
    public void testMinimalSpanningTree() {
        Graph g = loadFromText(NORMAL);
        Graph mst1 = g.prims(0);
        Graph mst2 = g.kruskals();
        assertThat(totalWeight(mst1)).isEqualTo(totalWeight(mst2));
    }

    @Test
    public void testMultiEdge() {
        Graph g = loadFromText(MULTI_EDGE);
        Graph mst = g.kruskals();
        assertThat(isTree(mst)).isTrue();
        assertThat(spans(mst, g)).isTrue();
    }



    /* Checks if a graph is a tree. */
    private boolean isTree(Graph g) {
        return g.getAllVertices().size() == g.getAllEdges().size() + 1;
    }

    /* Checks if a graph spans another graph. */
    private boolean spans(Graph g1, Graph g2) {
        return g1.spans(g2) && g2.spans(g1);
    }

    /* Returns the total weight of a graph. */
    private int totalWeight(Graph g) {
        int total = 0;
        for (Edge e : g.getAllEdges()) {
            total += e.getWeight();
        }
        return total;
    }

    /* Returns a randomly generated graph with VERTICES number of vertices and
       EDGES number of edges with max weight WEIGHT. */
    public static Graph randomGraph(int vertices, int edges, int weight) {
        Graph g = new Graph();
        Random rng = new Random();
        for (int i = 0; i < vertices; i += 1) {
            g.addVertex(i);
        }
        for (int i = 0; i < edges; i += 1) {
            Edge e = new Edge(rng.nextInt(vertices), rng.nextInt(vertices), rng.nextInt(weight));
            g.addEdge(e);
        }
        return g;
    }

    /* Returns a Graph object with integer edge weights as parsed from
       FILENAME. Talk about the setup of this file. */
    public static Graph loadFromText(String filename) {
        Charset cs = Charset.forName("US-ASCII");
        try (BufferedReader r = Files.newBufferedReader(Paths.get(filename), cs)) {
            Graph g = new Graph();
            String line;
            while ((line = r.readLine()) != null) {
                String[] fields = line.split(", ");
                if (fields.length == 3) {
                    int from = Integer.parseInt(fields[0]);
                    int to = Integer.parseInt(fields[1]);
                    int weight = Integer.parseInt(fields[2]);
                    g.addEdge(from, to, weight);
                } else if (fields.length == 1) {
                    g.addVertex(Integer.parseInt(fields[0]));
                } else {
                    throw new IllegalArgumentException("Bad input file!");
                }
            }
            return g;
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }
}

