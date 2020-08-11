import java.util.LinkedList;

public class OrderedQueue<E> implements MazeQueue<E> {
    private LinkedList<E> queue;

    public OrderedQueue() {
        this.queue = new LinkedList<>();
    }

    public void add(E e) {
        this.queue.addLast(e);
    }

    public E remove() {
        return this.queue.removeFirst();
    }

    public int getSize() {
        return this.queue.size();
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }
}
