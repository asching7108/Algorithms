import java.util.Iterator;

public interface Queue<T> extends Iterable<T> {

    public boolean isEmpty();

    public int size();

    public void enqueue(T item);

    public T dequeue();

    public Iterator<T> iterator();

}
