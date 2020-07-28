import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Given a list of WordNet nouns x1, x2, ..., xn, finds the outcast - the noun
 * that is the least related to the others, by computing the sum of the distances
 * between each noun and every other one:
 * di = distance(xi, x1) + distance(xi, x2) + ... + distance(xi, xn)
 * and return noun xt for which dt is maximum.
 *
 * @author Esther Lin
 */

public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int n = nouns.length;
        int[] dist = new int[n];
        // compute the sum of the distances between each noun and every other one
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int len = wordnet.distance(nouns[i], nouns[j]);
                dist[i] += len;
                dist[j] += len;
            }
        }

        // find the outcast
        int outcast = 0;
        for (int i = 1; i < n; i++) {
            if (dist[i] > dist[outcast])
                outcast = i;
        }
        return nouns[outcast];
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
