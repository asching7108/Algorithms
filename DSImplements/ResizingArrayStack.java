import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements Stack with resizing array.
 * Time complexity:
 * construct    : O(1)
 * push         : O(1) amortized
 * pop          : O(1) amortized
 * isEmpty      : O(1)
 *
 * @param <T> generic type
 * @author Esther Lin
 */

public class ResizingArrayStack<T> implements Stack<T> {

    private T[] items;
    private int size;

    public ResizingArrayStack(int capacity) {
        items = (T[]) new Object[capacity];
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    // doubles the array size when it is full
    public void push(T item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == items.length) resize(items.length * 2);
        items[size++] = item;
    }

    // halves the array when it is one-quarter full
    public T pop() {
        if (isEmpty()) throw new NoSuchElementException();
        T item = items[--size];
        items[size] = null;
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    private void resize(int capacity) {
        T[] copy = (T[]) new Object[capacity];
        if (size >= 0) System.arraycopy(items, 0, copy, 0, size);
        items = copy;
    }

    public Iterator<T> iterator() { return new RAStackIterator(); }

    private class RAStackIterator implements Iterator<T> {
        private int i = 0;

        public boolean hasNext() { return i < size; }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[i++];
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }
}
