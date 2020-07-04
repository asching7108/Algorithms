import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements Queue with resizing array.
 * Time complexity:
 * construct    : O(1)
 * enqueue      : O(1) amortized
 * dequeue      : O(1) amortized
 * isEmpty      : O(1)
 *
 * @param <T> generic type
 * @author Esther Lin
 */

public class ResizingArrayQueue<T> implements Queue<T> {

    private T[] items;
    private int head;
    private int tail;
    private int size;

    public ResizingArrayQueue(int capacity) {
        items = (T[]) new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    // doubles the array size when it is full
    public void enqueue(T item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == items.length) resize(items.length * 2);
        items[tail] = item;
        tail = (tail + 1) % items.length;
        size++;
    }

    // halves the array when it is one-quarter full
    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        T item = items[head];
        items[head++] = null;
        size--;
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    private void resize(int capacity) {
        T[] copy = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = items[(head + i) % items.length];
        }
        items = copy;
        head = 0;
        tail = size;
    }

    public Iterator<T> iterator() { return new RAQueueIterator(); }

    private class RAQueueIterator implements Iterator<T> {
        private int i = 0;

        public boolean hasNext() { return i < size; }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[(head + i++) % items.length];
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }

}
