import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // write your code here

        EventQueue.invokeLater(() -> {
                    MazeVisualizer mazeVis = new MazeVisualizer();
                    mazeVis.run();
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
