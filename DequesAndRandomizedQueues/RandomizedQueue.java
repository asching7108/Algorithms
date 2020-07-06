import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic data type RandomizedQueue, similar to a stack or queue, except
 * that the item removed is chosen uniformly at random among items in the
 * data structure. Used resizing array.
 *
 * @author Esther Lin
 */

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[8];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() { return size == 0; }

    // return the number of items on the randomized queue
    public int size() { return size; }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == items.length) resize(items.length * 2);
        items[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int randomIndex = StdRandom.uniform(size);
        Item item = items[randomIndex];
        items[randomIndex] = items[size - 1];
        items[size - 1] = null;
        size--;
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    // resize items array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        if (size >= 0) System.arraycopy(items, 0, copy, 0, size);
        items = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int randomIndex = StdRandom.uniform(size);
        return items[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new RandomizedQueueIterator(); }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] suffled = (Item[]) new Object[size];
        private int i = 0;

        public RandomizedQueueIterator() {
            int[] indices = StdRandom.permutation(size);
            for (int j = 0; j < size; j++) {
                suffled[j] = items[indices[j]];
            }
        }

        public boolean hasNext() { return i < size; }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return suffled[i++];
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        StdOut.println(rq.isEmpty());
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        StdOut.println(rq.isEmpty());
        StdOut.println("sample: " + rq.sample());
        StdOut.println("dequeue: " + rq.dequeue());
        StdOut.println("size = " + rq.size());
        for (int a : rq) {
            for (int b : rq)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
