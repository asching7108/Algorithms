import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Use Monte Carlo simulation to estimate the percolation threshold.
 *
 * @author Hsingyi Lin
 */

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final double[] thresholds; // array of thresholds of Percolation objects
    private final int trials;       // the number of trials

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
        thresholds = new double[trials];
        this.trials = trials;

        // performs experiments and record threshold
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int r = StdRandom.uniform(n * n);
                while (p.isOpen(r / n + 1, r % n + 1)) {
                    r = StdRandom.uniform(n * n);
                }
                p.open(r / n + 1, r % n + 1);
                thresholds[i]++;
            }
            thresholds[i] /= (n * n);
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
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    /**
     * Returns the high endpoint of 95% confidence interval.
     *
     * @return the high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        String conf = "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]";
        System.out.printf("%-23s %s %s%n", "mean", "=", ps.mean());
        System.out.printf("%-23s %s %s%n", "stddev", "=", ps.stddev());
        System.out.println("95% confidence interval = " + conf);
    }
}
