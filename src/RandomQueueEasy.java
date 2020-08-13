import java.util.ArrayList;

public class RandomQueueEasy<E> implements MazeQueue<E> {
    private ArrayList<E> queue;

    public RandomQueueEasy() {
        this.queue = new ArrayList<>();
    }

    public void add(E e) {
        this.queue.add(e);
    }

    public E remove() {
        if(queue.size() == 0) {
            throw new IllegalArgumentException("There's no element to remove in Random Queue");
        }
        int ranIndex = (int) (Math.random() * this.queue.size());

        System.out.println(ranIndex);

        E item = this.queue.get(ranIndex);
        // replace queue[ranIndex] with the last element
        this.queue.set(ranIndex, this.queue.get(this.queue.size()-1));
        this.queue.remove(this.queue.size()-1);
        return item;
    }

    public int getSize() {
        return this.queue.size();
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }
}
