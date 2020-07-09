/**
 * Bottom-up mergesort:
 * Pass through array, merging subarrays of size 1, and repeat for subarrays of size
 * 2, 4, 8, .... Simple but about 10% slower than recursive, top-down mergesort on
 * typical systems.
 */

public class MergesortBU {

    private static final int CUTOFF = 7;

    public static void sort(Comparable[] a) {
        int N = a.length;
        Comparable[] aux = new Comparable[N];
        for (int sz = 1; sz < N; sz = sz + sz) {
            for (int lo = 0; lo < N - sz; lo += sz + sz) {
                merge(a, aux, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
            }
        }
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
        System.arraycopy(aux, lo, a, lo, hi - lo + 1);
    }

    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

}
