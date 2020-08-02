import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture pic;
    private double[][] energy;
    private boolean isTransposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("argument is null");
        pic = new Picture(picture);
        isTransposed = false;

        // initialize energies for all pixels
        energy = new double[height()][width()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                // the given pixel is at the border
                if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
                    energy[y][x] = 1000;
                    continue;
                }
                int x1 = pic.getRGB(x - 1, y);
                int x2 = pic.getRGB(x + 1, y);
                int y1 = pic.getRGB(x, y - 1);
                int y2 = pic.getRGB(x, y + 1);
                energy[y][x] = Math.sqrt(colorDiff(x2, x1) + colorDiff(y2, y1));
            }
        }
    }

    // compute the rgd difference between two colors
    private double colorDiff(int c1, int c2) {
        return Math.pow(((c2 >> 16) & 0xFF) - ((c1 >> 16) & 0xFF), 2) +
                Math.pow(((c2 >> 8) & 0xFF) - ((c1 >> 8) & 0xFF), 2) +
                Math.pow(((c2 >> 0) & 0xFF) - ((c1 >> 0) & 0xFF), 2);
    }

    // current picture
    public Picture picture() {
        return isTransposed ? transposePicture(pic) : new Picture(pic);
    }

    // width of current picture
    public int width() {
        return isTransposed ? pic.height() : pic.width();
    }

    // height of current picture
    public int height() {
        return isTransposed ? pic.width() : pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new IllegalArgumentException("arguments out of bound");
        return isTransposed ? energy[x][y] : energy[y][x];
    }

    private void transpose() {
        Picture transposed = transposePicture(pic);
        energy = transposeEnergy(pic);
        pic = transposed;
        isTransposed = !isTransposed;
    }

    private Picture transposePicture(Picture original) {
        Picture transposed = new Picture(original.height(), original.width());
        for (int y = 0; y < original.height(); y++) {
            for (int x = 0; x < original.width(); x++) {
                transposed.setRGB(y, x, original.getRGB(x, y));
            }
        }
        return transposed;
    }

    private double[][] transposeEnergy(Picture original) {
        double[][] newEnergy = new double[original.width()][original.height()];
        for (int y = 0; y < original.height(); y++) {
            for (int x = 0; x < original.width(); x++) {
                newEnergy[x][y] = energy[y][x];
            }
        }
        return newEnergy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!isTransposed) transpose();
        return findVerticalSeamHelper();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (isTransposed) transpose();
        return findVerticalSeamHelper();
    }

    // sequence of indices for vertical seam, using given width and height
    private int[] findVerticalSeamHelper() {
        int w = pic.width(), h = pic.height();
        int[] edgeTo = new int[w * h + 2];
        double[] distTo = new double[w * h + 2];
        int s = w * h;      // virtual start vertex
        int e = w * h + 1;  // virtual end vertex

        // initialize distTo[s] = 0 and distTo[v] = ∞ for all other vertices
        for (int v = 0; v < distTo.length; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0;

        // relax all vertices in the first row
        for (int v = 0; v < w; v++) {
            relax(edgeTo, distTo, s, v, energy[v / w][v % w]);
        }

        // for each vertex from the second to the second last row, relax
        // the adjacent 3 vertices
        for (int i = 0; i < h - 1; i++) {
            for (int j = 0; j < w; j++) {
                int v = i * w + j;
                for (int k = -1; k <= 1; k++) {
                    if (j + k >= 0 && j + k < w)
                        relax(edgeTo, distTo, v, v + w + k, energy[i + 1][j + k]);
                }
            }
        }

        // for each vertex in the last row, relax the end vertex
        for (int j = 0; j < w; j++) {
            relax(edgeTo, distTo, (h - 1) * w + j, e, 0);
        }

        // find the shortest path from the end vertex to the start vertex
        int[] seam = new int[h];
        int cur = e;
        for (int i = h - 1; i >= 0; i--) {
            seam[i] = edgeTo[cur] % w;
            cur = edgeTo[cur];
        }
        return seam;
    }

    private void relax(int[] edgeTo, double[] distTo, int v, int w, double weight) {
        if (distTo[w] > distTo[v] + weight) {
            distTo[w] = distTo[v] + weight;
            edgeTo[w] = v;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("argument is null");
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("argument is null");
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
