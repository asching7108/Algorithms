import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Brute-force implementation for range search and nearest-neighbor search.
 *
 * @author Esther Lin
 */

public class PointSET {

    private final SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid argument");
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid argument");
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Invalid argument");
        Queue<Point2D> iterable = new Queue<>();
        for (Point2D p : set) {
            if (rect.contains(p)) iterable.enqueue(p);
        }
        return iterable;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid argument");
        double minDist = Double.MAX_EXPONENT;
        Point2D nearest = null;
        for (Point2D point : set) {
            if (p.distanceSquaredTo(point) < minDist) {
                minDist = p.distanceSquaredTo(point);
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        PointSET brute = new PointSET();
        brute.insert(new Point2D(0.2, 0.1));
        brute.insert(new Point2D(0.1, 0.4));
        brute.insert(new Point2D(0.6, 0.5));
        brute.insert(new Point2D(0.4, 0.4));

        Point2D q = new Point2D(0.2, 0.4);
        RectHV r = new RectHV(0.4, 0.3, 0.8, 0.6);

        // draw all of the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();

        // draw in red the nearest neighbor
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.RED);
        brute.nearest(q).draw();
        StdDraw.setPenRadius(0.02);

        // draw in blue the range search results
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point2D p : brute.range(r)) {
            p.draw();
        }
        StdDraw.show();
    }
}
