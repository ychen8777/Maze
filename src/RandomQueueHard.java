import java.util.LinkedList;
import java.util.Random;

public class RandomQueueHard<E> implements MazeQueue<E> {
    private LinkedList<E> queue;

    public RandomQueueHard() {
        this.queue = new LinkedList<>();
    }

    public void add(E e) {
        // randomly add e to first or last of queue
        double value = Math.random();
        if (value < 0.5) {
            this.queue.addLast(e);
        } else {
            this.queue.addFirst(e);
        }
    }

    public E remove() {
        if (this.queue.size() == 0) {
            throw new IllegalArgumentException("There's no element to remove in Random Queue");
        }
        // randomly remove the first or last e in queue
        double value = Math.random();
        if (value < 0.5) {
            return this.queue.removeFirst();
        } else {
            return this.queue.removeLast();
        }
    }

    public int getSize() {
        return this.queue.size();
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }
}
