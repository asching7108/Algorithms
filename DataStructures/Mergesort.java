import edu.princeton.cs.algs4.Insertion;

public class Mergesort {

    private static final int CUTOFF = 7;

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        System.arraycopy(a, 0, aux, 0, a.length);
        sort(aux, a, 0, a.length - 1);
    }

    // sort subarrays
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        // uses insertion sort for small subarrays
        if (hi <= lo + CUTOFF - 1) {
            Insertion.sort(a, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(aux, a, lo, mid);
        sort(aux, a, mid + 1, hi);
        if (!less(a[mid + 1], a[mid])) return;  // stop if already sorted
        merge(a, aux, lo, mid, hi); // switch roles of aux[] and a[] to eliminate copies
    }

    // merge two subarrays
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) aux[k] = a[j++];
            else if (j > hi) aux[k] = a[i++];
            else if (less(a[j], a[i])) aux[k] = a[j++];
            else aux[k] = a[i++];
        }
    }

    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

}
