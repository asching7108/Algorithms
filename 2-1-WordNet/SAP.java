import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/**
 * SAP tasks in a digraph, and finds the distance of the shortest ancestral path
 * and the common ancestor that participates in the path given two vertices or
 * two sets of vertices.
 *
 * @author Esther Lin
 */

public class SAP {

    // stores the computed length and ancestor
    private class SapObj {
        private final int length;
        private final int ancestor;

        SapObj(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }

    private final Digraph G;
    // store the length and ancestor for every vertex v to every vertex w
    private final Map<Integer, Map<Integer, SapObj>> map;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Arguments contain null");
        this.G = new Digraph(G);
        map = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // make sure v <= w
        if (w < v) {
            int temp = v;
            v = w;
            w = temp;
        }

        bfs(v, w);
        return map.get(v).get(w).length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        // make sure v <= w
        if (w < v) {
            int temp = v;
            v = w;
            w = temp;
        }

        bfs(v, w);
        return map.get(v).get(w).ancestor;
    }

    // use bfs to find the length and ancestor for v and w
    private void bfs(int v, int w) {
        Map<Integer, SapObj> mapV = map.getOrDefault(v, new HashMap<>());

        // return directly if already found
        if (mapV.containsKey(w)) return;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        Queue<Integer> curQ = new Queue<>();
        Queue<Integer> nextQ = new Queue<>();
        boolean[] visited = new boolean[G.V()];
        curQ.enqueue(v);
        curQ.enqueue(w);

        int curLen = 0, length = -1, ancestor = -1;

        // perform search of shortest ancestral path by level
        while (!curQ.isEmpty() && (length == -1 || curLen < length)) {
            int cur = curQ.dequeue();
            if (!visited[cur]) {
                // cur is a common ancestor of v and w
                if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)) {
                    int len = bfsV.distTo(cur) + bfsW.distTo(cur);
                    // cur participates in the shortest ancestral path so far
                    if (length == -1 || len < length) {
                        ancestor = cur;
                        length = len;
                    }
                }
                // add vertices of next level to the queue
                for (Integer i : G.adj(cur)) {
                    nextQ.enqueue(i);
                }
                visited[cur] = true;
            }

            // curr level search finished, continue to search the next level
            if (curQ.isEmpty()) {
                Queue<Integer> temp = curQ;
                curQ = nextQ;
                nextQ = temp;
                curLen++;
            }
        }

        mapV.put(w, new SapObj(length, ancestor));
        map.put(v, mapV);
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("arguments contain null");

        // detect zero-length vertices
        if (!v.iterator().hasNext() || !w.iterator().hasNext())
            return -1;

        return bfs(v, w, true);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("arguments contain null");

        // detect zero-length vertices
        if (!v.iterator().hasNext() || !w.iterator().hasNext())
            return -1;

        return bfs(v, w, false);
    }

    // use bfs to find the length and ancestor for v and w
    private int bfs(Iterable<Integer> v, Iterable<Integer> w, boolean getLength) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        Queue<Integer> curQ = new Queue<>();
        Queue<Integer> nextQ = new Queue<>();
        boolean[] visited = new boolean[G.V()];
        for (Integer i : v) {
            curQ.enqueue(i);
        }
        for (Integer i : w) {
            curQ.enqueue(i);
        }

        int curLen = 0, length = -1, ancestor = -1;

        // perform search of shortest ancestral path by level
        while (!curQ.isEmpty() && (length == -1 || curLen < length)) {
            int cur = curQ.dequeue();
            if (!visited[cur]) {
                // cur is a common ancestor of v and w
                if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)) {
                    int len = bfsV.distTo(cur) + bfsW.distTo(cur);
                    // cur participates in the shortest ancestral path so far
                    if (length == -1 || len < length) {
                        ancestor = cur;
                        length = len;
                    }
                }
                // add vertices of next level to the queue
                for (Integer i : G.adj(cur)) {
                    nextQ.enqueue(i);
                }
                visited[cur] = true;
            }

            // curr level search finished, continue to search the next level
            if (curQ.isEmpty()) {
                Queue<Integer> temp = curQ;
                curQ = nextQ;
                nextQ = temp;
                curLen++;
            }
        }

        if (getLength) return length;
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
