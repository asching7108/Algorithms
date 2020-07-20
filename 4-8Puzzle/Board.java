import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Models an n-by-n board with sliding tiles.
 *
 * @author Esther Lin
 */

public class Board {

    private int[][] board;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%2d ", board[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int humming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && !right(i, j)) humming++;
            }
        }
        return humming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0) {
                    manhattan += Math.abs(i - (board[i][j] - 1) / n);
                    manhattan += Math.abs(j - (board[i][j] - 1) % n);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!right(i, j)) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // self check
        if (y == this) return true;

        // null check
        if (y == null) return false;

        // type check
        if (y.getClass() != this.getClass()) return false;

        // type cast
        Board other = (Board) y;

        // board size check
        if (other.dimension() != this.dimension()) return false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != other.board[i][j]) return false;
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[][] neighborBlanks = new int[4][2];
        int num = 0, r = -1, c = -1;

        // find the blank tile
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    r = i;
                    c = j;
                }
            }
        }

        // get array of valid indices of the blank tile in neighbors
        if (r != 0) neighborBlanks[num++] = new int[] { r - 1, c };
        if (r != n - 1) neighborBlanks[num++] = new int[] { r + 1, c };
        if (c != 0) neighborBlanks[num++] = new int[] { r, c - 1 };
        if (c != n - 1) neighborBlanks[num++] = new int[] { r, c + 1 };

        // get neighboring boards
        Stack<Board> neighbors = new Stack<>();
        for (int k = 0; k < num; k++) {
            Board nbr = new Board(board);
            nbr.exch(r, c, neighborBlanks[k][0], neighborBlanks[k][1]);
            neighbors.push(nbr);
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int i = 0; i < n * n; i++) {
            Board twin = new Board(board);
            int row = i / n, col = i % n;
            if (board[row][col] != 0 && board[row + 1][col] != 0) {
                twin.exch(row, col, row + 1, col);
                return twin;
            }
        }
        return null;
    }

    // is the tile at (i, j) the right tile?
    private boolean right(int i, int j) {
        if (board[i][j] == 0) return i == n - 1 && j == n - 1;
        return board[i][j] == i * n + j + 1;
    }

    // exchange the two tiles at the given indices
    private void exch(int ar, int ac, int br, int bc) {
        int t = board[ar][ac];
        board[ar][ac] = board[br][bc];
        board[br][bc] = t;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][] {
            new int[] { 1, 0, 3 },
            new int[] { 4, 2, 5 },
            new int[] { 7, 8, 6 }
        });
        StdOut.println(board.toString());
        StdOut.println(board.twin().toString());
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
    }

}
