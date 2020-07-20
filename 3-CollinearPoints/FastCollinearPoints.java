import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a set of n distinct points in the plane, find every (maximal) line
 * segment that connects a subset of 4 or more of the points.
 * <p>
 * Faster approach:
 * 1. For each other point q, determine the slope it makes with p.
 * 2. Sort the points according to the slopes they makes with p.
 * 3. Check if any 3 or more adjacent points have equal slopes with respect
 * to p. If so, these points together with p are collinear.
 * <p>
 * Time Complexity: O(n2log(n))
 * Space Complexity: O(n2)
 *
 * @author Esther Lin
 */

public class FastCollinearPoints {

    private final List<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // input array is null
        if (points == null) throw new IllegalArgumentException("null array");

        // any point in the array is null
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("null point");
        }

        int n = points.length;
        Point[] sorted = points.clone();
        Arrays.sort(sorted);    // sort by position
        Point prev = null;

        // the argument to the constructor contains a repeated point
        for (Point p : sorted) {
            if (prev != null && p.compareTo(prev) == 0) {
                throw new IllegalArgumentException("repeated points");
            }
            prev = p;
        }

        segments = new ArrayList<>();
        // for each point i, sort the points by slope to i, and check if
        // any 3 or more adjacent points have equal slopes.
        for (int i = 0; i < n; i++) {
            Point p1 = sorted[i];
            Point[] bySlope = sorted.clone();
            Arrays.sort(bySlope, p1.slopeOrder());  // sort by slope to i
            int j = 1;
            while (j < n - 2) {
                // find how many adjacent points have equal slopes
                int k = j + 1;
                while (k < n && p1.slopeTo(bySlope[j]) == p1.slopeTo(bySlope[k])) {
                    k++;
                }
                // 3 or more adjacent points have equal slopes, and the segment
                // hasn't been previously found
                if (k - j >= 3 && p1.compareTo(bySlope[j]) < 0) {
                    segments.add(new LineSegment(p1, bySlope[k - 1]));
                }
                j = k;
            }
        }
    }

    // return the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // return the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        Point[] ps = {
            new Point(1, 2),
            new Point(3, 6),
            new Point(10, 20),
            new Point(5, 20),
            new Point(-2, -4)
        };
        FastCollinearPoints bcp = new FastCollinearPoints(ps);
        for (LineSegment seg : bcp.segments()) {
            StdOut.println(seg);
        }
        StdOut.println(bcp.numberOfSegments());
    }

}
