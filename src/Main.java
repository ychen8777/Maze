import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        MazeFrame window = new MazeFrame("Maze Demo");
        JButton generateMazeButton = window.getGenerateMazeButton();
        generateMazeButton.addActionListener((e) -> {
            StringBuilder res = new StringBuilder();
            res.append("Width ");
            res.append(window.getInputWidth());
            res.append(", height: ");
            res.append(window.getInputHeight());
            System.out.println(res.toString());});
    }
}
