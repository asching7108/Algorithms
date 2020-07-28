import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The WordNet digraph: each vertex v is an integer that represents a synset,
 * and each directed edge v → w represents that w is a hypernym of v. The WordNet
 * digraph is a rooted DAG (acyclic with one root vertex that is an ancestor of
 * every other vertex).
 *
 * @author Esther Lin
 */

public class WordNet {

    // store nouns with related indexes of synsets
    private final Map<String, List<Integer>> nounMap;
    private final String[] synsets;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("arguments contain null");

        nounMap = new HashMap<>();
        List<String> synsetList = new ArrayList<>();
        int size = 0;

        // read the synsets file
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            synsetList.add(fields[1]);
            String[] nouns = fields[1].split(" ");
            for (String noun : nouns) {
                List<Integer> ids = nounMap.getOrDefault(noun, new ArrayList<>());
                ids.add(id);
                nounMap.put(noun, ids);
            }
            size++;
        }

        this.synsets = synsetList.toArray(new String[size]);
        Digraph G = new Digraph(size);
        boolean[] notRoot = new boolean[size];

        // read the hypernyms file
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] fields = in.readLine().split(",");
            if (fields.length > 1) {
                int v = Integer.parseInt(fields[0]);
                notRoot[v] = true;
                for (int i = 1; i < fields.length; i++) {
                    G.addEdge(v, Integer.parseInt(fields[i]));
                }
            }
        }

        // check if the input is a rooted DAG
        int rootCount = 0;
        for (int i = 0; i < size; i++) {
            if (!notRoot[i]) {
                rootCount++;
                if (rootCount > 1)
                    throw new IllegalArgumentException("input has multiple root vertices");
            }
        }
        if (rootCount == 0)
            throw new IllegalArgumentException("input has no root vertex");

        // System.out.println(G.V());
        // System.out.println(G.E());

        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("arguments contain null");
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("arguments contain null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("arguments contain non-WordNet noun");
        return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("arguments contain null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("arguments contain non-WordNet noun");
        return synsets[(sap.ancestor(nounMap.get(nounA), nounMap.get(nounB)))];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("tests/synsets.txt", "tests/hypernyms.txt");
        System.out.println(wordnet.distance("cat", "table"));
        System.out.println(wordnet.sap("cat", "table"));
    }
}
