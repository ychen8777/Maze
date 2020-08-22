public class Position {
    private int row, col;
    private Position prevPos;

    public Position(int row, int col, Position prevPos) {
        this.row = row;
        this.col = col;
        this.prevPos = prevPos;
    }

    public Position(int row, int col) {
        this(row, col, null);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position getPrevPos() {
        return prevPos;
    }
}
