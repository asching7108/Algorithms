import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Solve n-by-n slider puzzles.
 *
 * @author Esther Lin
 */

public class Solver {

    private boolean isSolvable = false;
    private int moves = -1;
    private Stack<Board> solution = null;

    private class Node {
        private final Board board;
        private final int manhattan;
        private final int moves;
        private final Node prev;

        Node(Board b, int m, Node p) {
            board = b;
            manhattan = b.manhattan();
            moves = m;
            prev = p;
        }

        int priority() {
            return manhattan + moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Argument not valid");
        }

        MinPQ<Node> pq1 = new MinPQ<>((Node a, Node b) ->
                                          a.priority() - b.priority());
        MinPQ<Node> pq2 = new MinPQ<>((Node a, Node b) ->
                                          a.priority() - b.priority());
        pq1.insert(new Node(initial, 0, null));
        pq2.insert(new Node(initial.twin(), 0, null));
        while (!pq1.isEmpty() || !pq2.isEmpty()) {
            Node curr1 = pq1.delMin();
            Node curr2 = pq2.delMin();
            // initial board is solved
            if (curr1.board.isGoal()) {
                isSolvable = true;
                moves = curr1.moves;
                generateSolution(curr1);
                break;
            }
            // twin board is solved, initial board is not solvable
            else if (curr2.board.isGoal()) {
                break;
            }
            // add all neighboring nodes to the priority queues
            for (Board nb : curr1.board.neighbors()) {
                if (curr1.prev == null || !nb.equals(curr1.prev.board)) {
                    pq1.insert(new Node(nb, curr1.moves + 1, curr1));
                }
            }
            for (Board nb : curr2.board.neighbors()) {
                if (curr2.prev == null || !nb.equals(curr2.prev.board)) {
                    pq2.insert(new Node(nb, curr2.moves + 1, curr2));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private void generateSolution(Node node) {
        solution = new Stack<>();
        Node curr = node;
        while (curr != null) {
            solution.push(curr.board);
            curr = curr.prev;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        Board initial = new Board(new int[][] {
            new int[] { 1, 0, 3 },
            new int[] { 4, 2, 5 },
            new int[] { 7, 8, 6 }
        });

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
