import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a set of n distinct points in the plane, find every (maximal) line
 * segment that connects a subset of 4 or more of the points.
 * <p>
 * Brute approach: examines every 4 points and checks whether they all lie on
 * the same line segment, returning all such line segments.
 * <p>
 * Time Complexity: O(n4)
 * Space Complexity: O(n2)
 *
 * @author Esther Lin
 */

public class BruteCollinearPoints {

    private final List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // input array is null
        if (points == null) throw new IllegalArgumentException("null array");

        // any point in the array is null
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("null point");
        }

        int n = points.length;
        Point[] sorted = points.clone();
        Arrays.sort(sorted);
        Point prev = null;

        // the argument to the constructor contains a repeated point
        for (Point p : sorted) {
            if (prev != null && p.compareTo(prev) == 0) {
                throw new IllegalArgumentException("repeated points");
            }
            prev = p;
        }

        segments = new ArrayList<>();
        // examines 4 points at a time and checks whether they all lie on the
        // same line segment
        for (int a = 0; a < n; a++) {
            for (int b = a + 1; b < n; b++) {
                double s1 = sorted[a].slopeTo(sorted[b]);
                for (int c = b + 1; c < n; c++) {
                    double s2 = sorted[a].slopeTo(sorted[c]);
                    if (s1 != s2) continue;
                    for (int d = c + 1; d < n; d++) {
                        double s3 = sorted[a].slopeTo(sorted[d]);
                        if (s2 != s3) continue;
                        segments.add(new LineSegment(sorted[a], sorted[d]));
                    }
                }
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
        BruteCollinearPoints bcp = new BruteCollinearPoints(ps);
        for (LineSegment seg : bcp.segments()) {
            StdOut.println(seg);
        }
        StdOut.println(bcp.numberOfSegments());
    }

}
