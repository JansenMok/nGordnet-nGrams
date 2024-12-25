package helpers;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DirectedGraph {
    private Map<Integer, SynsetNode> nodes;
    private Map<Integer, Set<Integer>> edges;

    public DirectedGraph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    public void addNode(SynsetNode node) {
        nodes.put(node.getId(), node);
        edges.putIfAbsent(node.getId(), new HashSet<>());
    }

    public void addEdge(int from, int to) {
        if (!edges.containsKey(from)) {
            edges.put(from, new HashSet<>());
        }
        edges.get(from).add(to);
    }

    public SynsetNode getNode(int id) {
        return nodes.get(id);
    }

    public Set<Integer> getNeighbors(int id) {
        return edges.getOrDefault(id, new HashSet<>());
    }
}
