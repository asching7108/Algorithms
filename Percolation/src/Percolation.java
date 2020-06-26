import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * This class represents a percolation system. A system is an N-by-N
 * grid of sites. Each site is either open or blocked. A full site
 * is an open site that can be connected to an open site in the top
 * row via a chain of neighboring open sites. The system percolates
 * if there is a full site in the bottom row.
 *
 * @author Esther Lin
 */

public class Percolation {

	private boolean[][] grid;
	// represents connections of open sites including the virtual top
	// and bottom sites
	private WeightedQuickUnionUF sites;
	// represents connections of open sites including only the virtual
	// top site to prevent backwash
	private WeightedQuickUnionUF sitesNoBackWash;
	private int virtualTopSite;
	private int virtualBottomSite;
	private int numOpenSites;
	private int N;

	/**
	 * Creates n-by-n grid, with all sites initially blocked.
	 *
	 * @param n the side length of the grid
	 */
	public Percolation(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("n must be greater than 0");
		}
		grid = new boolean[n + 1][n + 1];
		sites = new WeightedQuickUnionUF(n * n + 2);
		sitesNoBackWash = new WeightedQuickUnionUF(n * n + 1);
		virtualTopSite = 0;
		virtualBottomSite = n * n + 1;
		numOpenSites = 0;
		N = n;
	}

	//

	/**
	 * Opens the site of the given indices if it is not open already.
	 *
	 * @param row the row index
	 * @param col the column index
	 */
	public void open(int row, int col) {
		validate(row, col);
		if (grid[row][col]) return;

		// opens the site
		grid[row][col] = true;
		numOpenSites++;
		// connects both site unions to the virtual top site
		if (row == 1) {
			sites.union(serial(row, col), virtualTopSite);
			sitesNoBackWash.union(serial(row, col), virtualTopSite);
		}
		// connects only the regular site union to the virtual bottom site
		if (row == N) {
			sites.union(serial(row, col), virtualBottomSite);
		}
		// connects both site unions to the site at top
		if (row > 1 && isOpen(row - 1, col)) {
			sites.union(serial(row, col), serial(row - 1, col));
			sitesNoBackWash.union(serial(row, col), serial(row - 1, col));
		}
		// connects both site unions to the site at bottom
		if (row < N && isOpen(row + 1, col)) {
			sites.union(serial(row, col), serial(row + 1, col));
			sitesNoBackWash.union(serial(row, col), serial(row + 1, col));
		}
		// connects both site unions to the site at left
		if (col > 1 && isOpen(row, col - 1)) {
			sites.union(serial(row, col), serial(row, col - 1));
			sitesNoBackWash.union(serial(row, col), serial(row, col - 1));
		}
		// connects both site unions to the site at right
		if (col < N && isOpen(row, col + 1)) {
			sites.union(serial(row, col), serial(row, col + 1));
			sitesNoBackWash.union(serial(row, col), serial(row, col + 1));
		}
	}

	/**
	 * Returns true / false if the site of the given indices is open.
	 *
	 * @param row the row index
	 * @param col the column index
	 * @return true if the site is open, otherwise returns false
	 */
	public boolean isOpen(int row, int col) {
		validate(row, col);
		return grid[row][col];
	}

	/**
	 * Returns true / false if the site of the given indices is full.
	 *
	 * @param row the row index
	 * @param col the column index
	 * @return true if the site is full, otherwise returns false
	 */
	public boolean isFull(int row, int col) {
		validate(row, col);
		return sitesNoBackWash.connected(serial(row, col), virtualTopSite);
	}

	/**
	 * Returns the number of open sites.
	 *
	 * @return the number of open sites
	 */
	public int numberOfOpenSites() {
		return numOpenSites;
	}

	/**
	 * Returns true / false if the system percolates.
	 *
	 * @return true if the system percolates, otherwise returns false
	 */
	public boolean percolates() {
		return sites.connected(virtualTopSite, virtualBottomSite);
	}

	/**
	 * Returns the 1D index of the given indices.
	 *
	 * @param row the row index
	 * @param col the column index
	 * @return the 1D index of the given indices
	 */
	private int serial(int row, int col) {
		validate(row, col);
		return (row - 1) * N + col;
	}

	/**
	 * Validates that the given indices are between 1 and N.
	 *
	 * @param row the row index
	 * @param col the column index
	 */
	private void validate(int row, int col) {
		if (row < 1 || row > N) {
			throw new IndexOutOfBoundsException("row index out of bound");
		}
		if (col < 1 || col > N) {
			throw new IndexOutOfBoundsException("column index out of bound");
		}
	}
}
