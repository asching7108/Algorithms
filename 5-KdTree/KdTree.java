import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Kd-Tree implementation for efficient range search and nearest-neighbor search.
 *
 * @author Esther Lin
 */

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.lb = null;
            this.rt = null;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid argument");
        if (isEmpty()) {
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            size++;
        }
        else insert(root, p, true);
    }

    // helper method for insert
    private void insert(Node n, Point2D p, boolean lvIsEven) {
        if (p.equals(n.p)) return;  // p already exists

        double cmp = lvIsEven ? p.x() - n.p.x() : p.y() - n.p.y();
        if (cmp <= 0) { // sink to left/bottom subtree
            if (n.lb == null) {
                RectHV rect = lvIsEven
                              ? new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax())
                              : new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
                n.lb = new Node(p, rect);
                size++;
            }
            else insert(n.lb, p, !lvIsEven);
        }
        else {          // sink to right/top subtree
            if (n.rt == null) {
                RectHV rect = lvIsEven
                              ? new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax())
                              : new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
                n.rt = new Node(p, rect);
                size++;
            }
            else insert(n.rt, p, !lvIsEven);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid argument");
        return contains(root, p, true);
    }

    // helper method for contains
    private boolean contains(Node n, Point2D p, boolean lvIsEven) {
        if (n == null) return false;
        if (p.equals(n.p)) return true;  // found p

        double cmp = lvIsEven ? p.x() - n.p.x() : p.y() - n.p.y();
        // search left/bottom subtree
        if (cmp <= 0) return contains(n.lb, p, !lvIsEven);
            // search right/top subtree
        else return contains(n.rt, p, !lvIsEven);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node n) {
        if (n == null) return;
        StdDraw.point(n.p.x(), n.p.y());
        draw(n.lb);
        draw(n.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Invalid argument");
        Queue<Point2D> iterable = new Queue<>();
        range(rect, root, iterable);
        return iterable;
    }

    // helper method for range
    private void range(RectHV rect, Node n, Queue<Point2D> queue) {
        if (n == null) return;

        // point p of node n is inside the given rectangle
        if (rect.contains(n.p)) queue.enqueue(n.p);

        // the rect of the left/bottom node intersects with the given rect
        if (n.lb != null && rect.intersects(n.lb.rect))
            range(rect, n.lb, queue);

        // the rect of the right/top node intersects with the given rect
        if (n.rt != null && rect.intersects(n.rt.rect))
            range(rect, n.rt, queue);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid argument");
        if (isEmpty()) return null;
        return nearest(p, root, root.p, true);
    }

    private Point2D nearest(Point2D p, Node n, Point2D nearestP, boolean lvIsEven) {
        if (n == null) return nearestP;

        // check the point of the current node
        if (p.distanceSquaredTo(n.p) < p.distanceSquaredTo(nearestP))
            nearestP = n.p;

        double cmp = lvIsEven ? p.x() - n.p.x() : p.y() - n.p.y();
        if (cmp <= 0) {
            // search left/bottom subtree
            nearestP = nearest(p, n.lb, nearestP, !lvIsEven);
            // search right/top subtree only if the distance between p and the
            // rect of the subtree is closer than the current nearest point to p
            if (n.rt != null && n.rt.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestP))
                nearestP = nearest(p, n.rt, nearestP, !lvIsEven);
        }
        else {
            // search right/top subtree
            nearestP = nearest(p, n.rt, nearestP, !lvIsEven);
            // search left/bottom subtree only if the distance between p and the
            // rect of the subtree is closer than the current nearest point to p
            if (n.lb != null && n.lb.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestP))
                nearestP = nearest(p, n.lb, nearestP, !lvIsEven);
        }

        return nearestP;
    }

    public static void main(String[] args) {
        PointSET kdTree = new PointSET();
        kdTree.insert(new Point2D(0.2, 0.1));
        kdTree.insert(new Point2D(0.1, 0.4));
        kdTree.insert(new Point2D(0.6, 0.5));
        kdTree.insert(new Point2D(0.4, 0.4));

        Point2D q = new Point2D(0.2, 0.4);
        RectHV r = new RectHV(0.4, 0.3, 0.8, 0.6);

        // draw all of the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdTree.draw();

        // draw in red the nearest neighbor
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.RED);
        kdTree.nearest(q).draw();
        StdDraw.setPenRadius(0.02);

        // draw in blue the range search results
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point2D p : kdTree.range(r)) {
            p.draw();
        }
        StdDraw.show();
    }
}
