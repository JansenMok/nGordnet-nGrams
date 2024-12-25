package helpers;
import edu.princeton.cs.algs4.In;
import java.util.*;

public class WordNet {
    private DirectedGraph graph;
    private Map<String, Set<Integer>> wordToIdMap;

    public WordNet(String synsetsFile, String hyponymsFile) {
        graph = new DirectedGraph();
        wordToIdMap = new HashMap<>();
        In synsetsIn = new In(synsetsFile);
        while (synsetsIn.hasNextLine()) {
            String synsetsFileData = synsetsIn.readLine();
            parseSynsets(synsetsFileData);
        }

        In hyponymsIn = new In(hyponymsFile);
        while (hyponymsIn.hasNextLine()) {
            String hyponymsFileData = hyponymsIn.readLine();
            parseHyponyms(hyponymsFileData);
        }
    }

    private void parseSynsets(String line) {
        String[] parsed = line.split(",");
        int idNum = Integer.parseInt(parsed[0]);
        String[] nouns = parsed[1].split(" ");
        String dictDef = parsed[2]; // dont need this?

        SynsetNode newNode = new SynsetNode(idNum);
        for (String noun : nouns) {
            newNode.addNoun(noun);
            wordToIdMap.putIfAbsent(noun, new HashSet<>());
            wordToIdMap.get(noun).add(idNum);
        }
        graph.addNode(newNode);
    }

    private void parseHyponyms(String line) {
        // parse a line from hyponymsFile and add edges to the graph
        String[] parsed = line.split(",");
        int synsetID = Integer.parseInt(parsed[0]);
        String[] directHyponyms = Arrays.copyOfRange(parsed, 1, parsed.length);
        for (String id : directHyponyms) {
            graph.addEdge(synsetID, Integer.parseInt((id)));
        }
    }

    public Set<String> getHyponyms(String word) {
        Set<String> hyponyms = new HashSet<>();
        if (wordToIdMap.containsKey(word)) {
            for (int id : wordToIdMap.get(word)) {
                hyponyms.addAll(getHyponymsRecursive(id));
            }
        }
        return hyponyms;
    }

    private Set<String> getHyponymsRecursive(int id) {
        Set<String> result = new HashSet<>(graph.getNode(id).getNouns());
        for (int neighbor : graph.getNeighbors(id)) {
            result.addAll(getHyponymsRecursive(neighbor));
        }
        return result;
    }
    public Set<String> getCommonHyponyms(String[] words) {
        List<Set<String>> listOfHyponymSets = new ArrayList<>();
        for (String word : words) {
            listOfHyponymSets.add(getHyponyms(word));
        }
        return intersectSets(listOfHyponymSets);
    }

    private Set<String> intersectSets(List<Set<String>> sets) {
        if (sets.isEmpty()) {
            return new HashSet<>();
        }
        Iterator<Set<String>> it = sets.iterator();
        Set<String> result = new HashSet<>(it.next());
        while (it.hasNext()) {
            result.retainAll(it.next());
        }
        return result;
    }
}
