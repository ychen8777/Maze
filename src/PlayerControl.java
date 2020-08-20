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
            openAround(newRow, newCol);
        }
    }

    public void moveDown() {
        int newRow = this.player.getPosition().getRow() + 1;
        int newCol = this.player.getPosition().getCol();
        if (mazeData.inArea(newRow, newCol) && mazeData.maze[newRow][newCol] == MazeData.ROAD) {
            this.player.moveTo(new Position(newRow, newCol));
            openAround(newRow, newCol);
        }
    }

    public void moveLeft() {
        int newRow = this.player.getPosition().getRow();
        int newCol = this.player.getPosition().getCol() - 1;
        if (mazeData.inArea(newRow, newCol) && mazeData.maze[newRow][newCol] == MazeData.ROAD) {
            this.player.moveTo(new Position(newRow, newCol));
            openAround(newRow, newCol);
        }
    }

    public void moveRight() {
        int newRow = this.player.getPosition().getRow();
        int newCol = this.player.getPosition().getCol() + 1;
        if (mazeData.inArea(newRow, newCol) && mazeData.maze[newRow][newCol] == MazeData.ROAD) {
            this.player.moveTo(new Position(newRow, newCol));
            openAround(newRow, newCol);
        }
    }

    // make surrounding area visible
    public void openAround(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (this.mazeData.inArea(newRow, newCol) && this.mazeData.inMist[newRow][newCol]) {
                    this.mazeData.inMist[newRow][newCol] = false;
                }
            }
        }
    }


}