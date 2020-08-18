public class Player {
    private Position position;

    public Player(Position start) {
        this.position = start;
    }

    public void moveTo(Position newPosition) {
        this.position = newPosition;
    }

    public Position getPosition() {
        return this.position;
    }
}
