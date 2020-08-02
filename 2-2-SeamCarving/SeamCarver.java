import edu.princeton.cs.algs4.Picture;

/**
 * Seam-carving is a content-aware image resizing technique where the image is
 * reduced in size by one pixel of height (or width) at a time. A vertical seam
 * is a path of pixels connected from the top to the bottom with one pixel in
 * each row. Technique:
 * <p>
 * 1. Calculate the dual-gradient energy of each pixel.
 * 2. Find a vertical (or horizontal) seam of minimum total energy.
 * 3. Remove from the image all of the pixels along the seam.
 *
 * @author Esther Lin
 */

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

        // initialize energy matrix for all pixels
        energy = new double[height()][width()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[y][x] = calcEnergy(x, y);
            }
        }
    }

    // calculate energy of the given pixel
    private double calcEnergy(int x, int y) {
        // the given pixel is at the border
        if (x == 0 || y == 0 || x == pic.width() - 1 || y == pic.height() - 1)
            return 1000;
        int x1 = pic.getRGB(x - 1, y);
        int x2 = pic.getRGB(x + 1, y);
        int y1 = pic.getRGB(x, y - 1);
        int y2 = pic.getRGB(x, y + 1);
        return Math.sqrt(colorDiff(x2, x1) + colorDiff(y2, y1));
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

    // flip the picture over its diagonal (switches row and column indices)
    private void transpose() {
        Picture transposed = transposePicture(pic);
        energy = transposeEnergy(pic);
        pic = transposed;
        isTransposed = !isTransposed;
    }

    // transpose the picture and return it
    private Picture transposePicture(Picture original) {
        Picture transposed = new Picture(original.height(), original.width());
        for (int y = 0; y < original.height(); y++) {
            for (int x = 0; x < original.width(); x++) {
                transposed.setRGB(y, x, original.getRGB(x, y));
            }
        }
        return transposed;
    }

    // transpose the energy matrix and return it
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

    // sequence of indices for vertical seam, using width and height of the
    // member variable pic
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

        // relax the edges from s to all vertices in the first row
        for (int v = 0; v < w; v++) {
            relax(edgeTo, distTo, s, v, energy[v / w][v % w]);
        }

        // for each vertex v from the second to the second last row, relax
        // the edges from v to its 3 adjacent vertices
        for (int i = 0; i < h - 1; i++) {
            for (int j = 0; j < w; j++) {
                int v = i * w + j;
                for (int k = -1; k <= 1; k++) {
                    if (j + k >= 0 && j + k < w)
                        relax(edgeTo, distTo, v, v + w + k, energy[i + 1][j + k]);
                }
            }
        }

        // for each vertex v in the last row, relax the edge v → e
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

    // relax edge e = v → w : if e gives shorter path to w through v, update
    // both distTo[w] and edgeTo[w]
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

        if (!isTransposed) transpose();
        removeVerticalSeamHelper(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("argument is null");

        if (isTransposed) transpose();
        removeVerticalSeamHelper(seam);
    }

    // remove vertical seam from current picture
    private void removeVerticalSeamHelper(int[] seam) {
        int w = pic.width(), h = pic.height();

        // cannot remove seam when picture width <= 1
        if (w <= 1)
            throw new IllegalArgumentException("cannot remove seam");

        // seam[] length is different from picture height
        if (seam.length != h)
            throw new IllegalArgumentException("invalid seam length");

        // successive entries in seam[] differ by 2 or more
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException("invalid seam entries");
        }

        // remove seam from picture
        Picture newPic = new Picture(w - 1, h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (x < seam[y])
                    newPic.setRGB(x, y, pic.getRGB(x, y));
                else if (x > seam[y])
                    newPic.setRGB(x - 1, y, pic.getRGB(x, y));
            }
        }
        pic = newPic;

        // update energy matrix
        for (int y = 0; y < h; y++) {
            double[] newRow = new double[w - 1];
            int x = seam[y];
            if (x > 0) {
                System.arraycopy(energy[y], 0, newRow, 0, x);
                newRow[x - 1] = calcEnergy(x - 1, y);
            }
            if (x < w - 1) {
                System.arraycopy(energy[y], x + 1, newRow, x, w - x - 1);
                newRow[x] = calcEnergy(x, y);
            }
            energy[y] = newRow;
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // use other test classes
    }

}
