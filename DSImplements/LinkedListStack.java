import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements Stack with linked list.
 * Time complexity:
 * construct    : O(1)
 * push         : O(1)
 * pop          : O(1)
 * isEmpty      : O(1)
 *
 * @param <T> generic type
 * @author Esther Lin
 */

public class LinkedListStack<T> implements Stack<T> {

    private Node first;
    private int size;

    private class Node {
        T item;
        Node next;
    }

    public LinkedListStack() {
        first = null;
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void push(T item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException();
        T item = first.item;
        first = first.next;
        size--;
        return item;
    }

    public Iterator<T> iterator() { return new LLStackIterator(); }

    private class LLStackIterator implements Iterator<T> {
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
