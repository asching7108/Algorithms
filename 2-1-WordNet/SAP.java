import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class SAP {

    class SapObj {
        int length;
        int ancestor;

        SapObj(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }

    private Digraph G;
    // store the length and ancestor for every vertex v to every vertex w
    private Map<Integer, Map<Integer, SapObj>> map;
    // store the length and ancestor for every vertices set v to every vertices set w
    private Map<Iterable<Integer>, Map<Iterable<Integer>, SapObj>> setMap;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Arguments contain null");
        this.G = new Digraph(G);
        map = new HashMap<>();
        setMap = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException("Argument(s) out of range");

        // make sure v < w
        if (v > w) {
            int temp = v;
            v = w;
            w = temp;
        }

        bfs(v, w);
        return map.get(v).get(w).length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException("Argument(s) out of range");

        // make sure v < w
        if (v > w) {
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

        Queue<Integer> curQ = new ArrayDeque<>();
        Queue<Integer> nextQ = new ArrayDeque<>();
        boolean[] visited = new boolean[G.V()];
        curQ.offer(v);
        curQ.offer(w);

        int curLen = 0, length = -1, ancestor = -1;

        // perform search of shortest ancestral path by level
        while (!curQ.isEmpty() && (length == -1 || curLen < length)) {
            int cur = curQ.poll();
            if (visited[cur]) continue;
            // cur is a common ancestor of v and w
            if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)) {
                int len = bfsV.distTo(cur) + bfsW.distTo(cur);
                // cur participates in the shortest ancestral path so far
                if (length == -1 || len < length) {
                    ancestor = cur;
                    length = len;
                }
            }
            visited[cur] = true;
            // add vertices of next level to the queue
            for (Integer i : G.adj(cur)) {
                nextQ.offer(i);
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

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Arguments contain null");
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, v);
        int length = Integer.MAX_VALUE;
        for (Integer i : w) {
            length = Math.max(length, bfs.distTo(i));
        }
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Arguments contain null");

        return 0;
    }

    // use bfs to find the length and ancestor for v and w
    private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
        Map<Iterable<Integer>, SapObj> mapV = setMap.getOrDefault(v, new HashMap<>());

        // return directly if already found
        if (mapV.containsKey(w)) return;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        Queue<Integer> curQ = new ArrayDeque<>();
        Queue<Integer> nextQ = new ArrayDeque<>();
        boolean[] visited = new boolean[G.V()];
        for (Integer i : v) {
            curQ.offer(i);
        }
        for (Integer i : w) {
            curQ.offer(i);
        }

        int curLen = 0, length = -1, ancestor = -1;

        // perform search of shortest ancestral path by level
        while (!curQ.isEmpty() && (length == -1 || curLen < length)) {
            int cur = curQ.poll();
            if (visited[cur]) continue;
            // cur is a common ancestor of v and w
            if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)) {
                int len = bfsV.distTo(cur) + bfsW.distTo(cur);
                // cur participates in the shortest ancestral path so far
                if (length == -1 || len < length) {
                    ancestor = cur;
                    length = len;
                }
            }
            visited[cur] = true;
            // add vertices of next level to the queue
            for (Integer i : G.adj(cur)) {
                nextQ.offer(i);
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
        setMap.put(v, mapV);
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
