import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // write your code here

        EventQueue.invokeLater(() -> {
                    MazeData data = new MazeData(101, 75);
                    MazeFrame window = new MazeFrame("Maze Demo");


                    JButton generateMazeButton = window.getGenerateMazeButton();

                    new Thread(() -> {
                        window.render(data);
                    }).start();
                }
                );




//        generateMazeButton.addActionListener((e) -> {
//            StringBuilder res = new StringBuilder();
//            res.append("Width ");
//            res.append(window.getInputWidth());
//            res.append(", height: ");
//            res.append(window.getInputHeight());
//            System.out.println(res.toString());});
    }
}
