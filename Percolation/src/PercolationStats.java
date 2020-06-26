import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Use Monte Carlo simulation to estimate the percolation threshold.
 *
 * @author Hsingyi Lin
 */

public class PercolationStats {

	private Percolation[] P;    // array of Percolation objects
	private int[] thresholds;    // array of thresholds of Percolation objects
	private int T;                // the number of trials

	/**
	 * Perform independent trials on an n-by-n grid.
	 *
	 * @param n      the side length of the grid
	 * @param trials the number of trials
	 */
	public PercolationStats(int n, int trials) {
		if (n < 1) {
			throw new IllegalArgumentException("n must be greater than 0");
		}
		if (trials < 1) {
			throw new IllegalArgumentException("trials must be greater than 0");
		}
		// initializes variables
		T = trials;
		P = new Percolation[T];
		thresholds = new int[T];

		// performs experiments and record threshold
		for (int i = 0; i < T; i++) {
			P[i] = new Percolation(n);
			while (!P[i].percolates()) {
				int r = StdRandom.uniform(n * n);
				while (P[i].isOpen(r / n + 1, r % n + 1)) {
					r = StdRandom.uniform(n * n);
				}
				P[i].open(r / n + 1, r % n + 1);
				thresholds[i]++;
			}
		}
	}

	/**
	 * Returns the sample mean of percolation threshold.
	 *
	 * @return the sample mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(thresholds);
	}

	/**
	 * Returns the sample standard deviation of percolation threshold.
	 *
	 * @return the sample standard deviation of percolation threshold
	 */
	public double stddev() {
		return StdStats.stddev(thresholds);
	}

	/**
	 * Returns the low endpoint of 95% confidence interval.
	 *
	 * @return the low endpoint of 95% confidence interval
	 */
	public double confidenceLo() {
		return mean() - 1.96 * stddev() / Math.sqrt(T);
	}

	/**
	 * Returns the high endpoint of 95% confidence interval.
	 *
	 * @return the high endpoint of 95% confidence interval
	 */
	public double confidenceHi() {
		return mean() + 1.96 * stddev() / Math.sqrt(T);
	}

	// test client
	public static void main(String[] args) {
		PercolationStats ps = new PercolationStats(20, 10);
		System.out.println(ps.mean());
		System.out.println(ps.stddev());
		System.out.println(ps.confidenceLo());
		System.out.println(ps.confidenceHi());
	}
}
