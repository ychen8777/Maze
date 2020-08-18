public  class PlayerControl{
    private Player player;
    private MazeData mazeData;

    public PlayerControl(MazeData maze) {
        this.mazeData = maze;
        this.player = maze.getPlayer();
    }

    public void moveUp() {
        int newRow = this.player.getPosition().getRow() - 1;
        int newCol = this.player.getPosition().getCol();
        if (mazeData.inArea(newRow, newCol) && mazeData.maze[newRow][newCol] == MazeData.ROAD) {
            this.player.moveTo(new Position(newRow, newCol));
        }
    }

    public void moveDown() {
        int newRow = this.player.getPosition().getRow() + 1;
        int newCol = this.player.getPosition().getCol();
        if (mazeData.inArea(newRow, newCol) && mazeData.maze[newRow][newCol] == MazeData.ROAD) {
            this.player.moveTo(new Position(newRow, newCol));
        }
    }

    public void moveLeft() {
        int newRow = this.player.getPosition().getRow();
        int newCol = this.player.getPosition().getCol() - 1;
        if (mazeData.inArea(newRow, newCol) && mazeData.maze[newRow][newCol] == MazeData.ROAD) {
            this.player.moveTo(new Position(newRow, newCol));
        }
    }

    public void moveRight() {
        int newRow = this.player.getPosition().getRow();
        int newCol = this.player.getPosition().getCol() + 1;
        if (mazeData.inArea(newRow, newCol) && mazeData.maze[newRow][newCol] == MazeData.ROAD) {
            this.player.moveTo(new Position(newRow, newCol));
        }
    }


}