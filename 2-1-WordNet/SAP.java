import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayDeque;
import java.util.Queue;

public class SAP {

    private Digraph G;
    int[][][] sap;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Arguments contain null");
        this.G = new Digraph(G);
        sap = new int[G.V()][][];
    }

    private void bfs(int v, int w) {
        if (sap[v] == null) {
            sap[v] = new int[G.V()][];
            for (int i = 0; i < G.V(); i++) {
                sap[v][i] = new int[2];
                sap[v][i][0] = -2;
                sap[v][i][1] = -2;
            }
        }
        else if (sap[v][w][0] > -2) return;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        Queue<Integer> curQ = new ArrayDeque<>();
        Queue<Integer> nextQ = new ArrayDeque<>();
        boolean[] visited = new boolean[G.V()];
        curQ.offer(v);
        curQ.offer(w);
        int curLen = 0;

        while (!curQ.isEmpty() && (sap[v][w][1] == -2 || curLen < sap[v][w][1])) {
            int cur = curQ.poll();
            if (visited[cur]) continue;
            if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)) {
                int len = bfsV.distTo(cur) + bfsW.distTo(cur);
                if (sap[v][w][1] == -2 || len < sap[v][w][1]) {
                    sap[v][w][0] = cur;
                    sap[v][w][1] = len;
                }
            }
            for (Integer i : G.adj(cur)) {
                nextQ.offer(i);
            }
            visited[cur] = true;
            if (curQ.isEmpty()) {
                Queue<Integer> temp = curQ;
                curQ = nextQ;
                nextQ = curQ;
                curLen++;
            }
        }

        if (sap[v][w][0] == -2) {
            sap[v][w][0] = -1;
            sap[v][w][1] = -1;
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException("Argument(s) out of range");

        if (v > w) {
            int temp = v;
            v = w;
            w = temp;
        }

        bfs(v, w);
        return sap[v][w][1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException("Argument(s) out of range");

        if (v > w) {
            int temp = v;
            v = w;
            w = temp;
        }

        bfs(v, w);
        return sap[v][w][0];
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
