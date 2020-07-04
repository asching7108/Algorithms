import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * The client class takes an integer k as a command-line argument; reads a
 * sequence of strings from standard input using StdIn.readString(); and
 * prints exactly k of them, uniformly at random. Print each item from the
 * sequence at most once.
 * Time Complexity: O(n) where n is the number of string on standard input
 * Space Complexity: O(k)
 *
 * @author Esther Lin
 */

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]), i = 0;
        if (k > 0) {
            RandomizedQueue<String> rq = new RandomizedQueue<>();
            while (!StdIn.isEmpty()) {
                String next = StdIn.readString();
                if (i < k) rq.enqueue(next);
                else if (StdRandom.uniform(i + 1) < k) {
                    rq.dequeue();
                    rq.enqueue(next);
                }
                i++;
            }
            for (int j = 0; j < k; j++) {
                StdOut.println(rq.dequeue());
            }
        }
    }
}
