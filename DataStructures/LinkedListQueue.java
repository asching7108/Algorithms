import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements Queue with linked list.
 * Time complexity:
 * construct    : O(1)
 * enqueue      : O(1)
 * dequeue      : O(1)
 * isEmpty      : O(1)
 */

public class LinkedListQueue<T> implements Queue<T> {

    private Node first;
    private Node last;
    private int size;

    private class Node {
        T item;
        Node next;
    }

    public LinkedListQueue() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void enqueue(T item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        size++;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        T item = first.item;
        first = first.next;
        size--;
        if (isEmpty()) last = null;
        return item;
    }

    public Iterator<T> iterator() { return new LLQueueIterator(); }

    private class LLQueueIterator implements Iterator<T> {
        private Node curr = first;

        public boolean hasNext() { return curr != null; }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item = curr.item;
            curr = curr.next;
            return item;
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }

}
