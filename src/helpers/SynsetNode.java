package helpers;
import java.util.HashSet;
import java.util.Set;

public class SynsetNode {
    private int id;
    private Set<String> nouns;

    public SynsetNode(int id) {
        this.id = id;
        this.nouns = new HashSet<>();
    }

    public void addNoun(String noun) {
        nouns.add(noun);
    }

    public Set<String> getNouns() {
        return nouns;
    }

    public int getId() {
        return id;
    }
}
