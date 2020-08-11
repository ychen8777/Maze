public interface MazeQueue<E> {
    void add(E e);
    E remove();
    boolean isEmpty();
    int getSize();
}
