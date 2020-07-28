import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private Map<String, List<Integer>> map;
    private String[] nouns;
    private Digraph G;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("arguments contain null");

        map = new HashMap<>();
        List<String> list = new ArrayList<>();
        int size = 0;

        // read the synsets file
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            list.add(fields[1]);
            List<Integer> ids = map.getOrDefault(fields[1], new ArrayList<>());
            ids.add(id);
            map.put(fields[1], ids);
            size++;
        }

        nouns = list.toArray(new String[size]);
        G = new Digraph(size);

        // read the hypernyms file
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] fields = in.readLine().split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                G.addEdge(v, Integer.parseInt(fields[i]));
            }
        }

        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("arguments contain null");
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("arguments contain null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("arguments contain non-WordNet noun");
        return sap.length(map.get(nounA), map.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("arguments contain null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("arguments contain non-WordNet noun");
        return nouns[(sap.ancestor(map.get(nounA), map.get(nounB)))];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("tests/synsets.txt", "tests/hypernyms.txt");
        System.out.println(wordnet.G.V());
        System.out.println(wordnet.G.E());
    }
}
