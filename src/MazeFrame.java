import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MazeFrame extends JFrame {

    public static int blockSize = 10;
    private int windowHeight;
    private int windowWidth;
    private MazeControl mazeControl;
    private MazeCanvas mazeCanvas;
    private MazeData mazeData;
    private int brickWidth;
    private int brickHeight;

    public MazeFrame(String title, int windowWidth, int windowHeight) {
        super(title);
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.setSize(windowWidth, windowHeight);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        // control panel width 351, height 101
        this.mazeControl = new MazeControl();
        //add(mazeControl, BorderLayout.NORTH);
        this.mazeCanvas = new MazeCanvas();
        //add(mazeCanvas, BorderLayout.SOUTH);
        this.add(mazeControl);
        this.add(mazeCanvas);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        this.windowWidth = (int) this.getSize().getWidth();
        this.windowHeight = (int) this.getSize().getHeight();

//        System.out.println("Control height:");
//        System.out.println(mazeControl.getHeight());
//        System.out.println("Control width:");
//        System.out.println(mazeControl.getWidth());

//        System.out.println("ori width: ");
//        System.out.println(this.getSize().getWidth());
//        System.out.println("ori height: ");
//        System.out.println(this.getSize().getHeight());

    }

    public MazeFrame(String title) {
        this(title, MazeFrame.blockSize*49, 49*MazeFrame.blockSize+101);
    }

    public void render(MazeData data) {
        this.mazeData = data;
        repaint();
    }

    public void resizeWindow(int width, int height){
        if (width <= 410 && height <= 140) {
            this.windowWidth = 410;
            this.windowHeight = 140;
        }
        else if (width <= 410) {
            this.windowWidth = 410;
            this.windowHeight = height;
        } else if (height <= 140) {
            this.windowWidth = width;
            this.windowHeight = 140;
        } else {
            this.windowWidth = width;
            this.windowHeight = height;
        }

//        System.out.println("new width: ");
//        System.out.println(this.windowWidth);
//        System.out.println("new height: ");
//        System.out.println(this.windowHeight);

//        this.remove(mazeControl);
//        this.remove(mazeCanvas);
        this.setSize(windowWidth, windowHeight);
//        this.add(mazeControl);
//        this.add(mazeCanvas);
    }

    public void showSuccess() {
        String msg = "Congratulations, you got out off the maze!";
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public int getInputWidth() {
        return Integer.parseInt(this.mazeControl.getWidthInput());
    }

    public int getInputHeight() {
        return Integer.parseInt(this.mazeControl.getHeightInput());
    }

    public String getDifficulty() {
        return (String) this.mazeControl.getDifficulty();
    }

    public String getMapOption() {
        return (String) this.mazeControl.getMapOption();
    }

    public JComboBox getLevelBox() {
        return this.mazeControl.getLevelBox();
    }

    public JComboBox getMapBox() {
        return this.mazeControl.getMapBox();
    }

    public JButton getGenerateMazeButton() {
        return this.mazeControl.getGenerateMazeButton();
    }

    public JButton getDFSButton() {
        return this.mazeControl.getDFSButton();
    }

    public JButton getBSFButton() {
        return this.mazeControl.getBSFButton();
    }

    public JButton getWallButton() {return this.mazeControl.getWallButton(); }

    public int getBlockSize() {
        return blockSize;
    }

    private class MazeControl extends JPanel {
        private JLabel widthLabel = new JLabel("Width: ");
        private JTextField widthField = new JTextField("12");
        private JLabel heightLabel = new JLabel("Height:");
        private JTextField heightField = new JTextField("12");
        private JButton generateMazeButton = new JButton("Generate Maze");
        private JButton DFSButton = new JButton("Solve by DFS");
        private JButton BSFButton = new JButton("Solve by BFS");
        private JButton WallButton = new JButton("Solve by Wall Follower");
        private JLabel levelText = new JLabel("Difficulty: ");
        private JComboBox levelBox;
        private JLabel mapText = new JLabel("Open Map: ");
        private JComboBox mapBox;


        public MazeControl(){
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel control1 = new JPanel();
            control1.add(widthLabel);
            widthField.setColumns(4);
            widthField.setHorizontalAlignment(JTextField.CENTER);
            control1.add(widthField);
            control1.add(heightLabel);
            heightField.setColumns(4);
            heightField.setHorizontalAlignment(JTextField.CENTER);
            control1.add(heightField);
            control1.add(generateMazeButton);
            this.add(control1);

            JPanel control2 = new JPanel();
            String[] levels = {"Easy", "Hard"};
            this.levelBox = new JComboBox(levels);
            String[] mapChoice = {"on", "off"};
            this.mapBox = new JComboBox(mapChoice);
            control2.add(levelText);
            control2.add(levelBox);
            JLabel placeHolder = new JLabel("      ");
            control2.add(placeHolder);
            control2.add(mapText);
            control2.add(mapBox);
            this.add(control2);

            JPanel control3 = new JPanel();
            //control3.add(generateMazeButton);
            control3.add(DFSButton);
            control3.add(BSFButton);
            control3.add(WallButton);
            this.add(control3);

        }

        public String getWidthInput() {
            return this.widthField.getText();
        }

        public String getHeightInput() {
            return this.heightField.getText();
        }

        public String getDifficulty() {
            return (String) this.levelBox.getSelectedItem();
        }

        public String getMapOption() {
            return (String) this.mapBox.getSelectedItem();
        }

        public JComboBox getLevelBox() {
            return levelBox;
        }

        public JComboBox getMapBox() {
            return mapBox;
        }

        public JButton getGenerateMazeButton() {
            return this.generateMazeButton;
        }

        public JButton getDFSButton() {
            return this.DFSButton;
        }

        public JButton getBSFButton() {
            return this.BSFButton;
        }

        public JButton getWallButton() {
            return WallButton;
        }
    }

    private class MazeCanvas extends JPanel {

        public MazeCanvas() {
            //double buffer
            super(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D)g;

            for (int i = 0; i < mazeData.getM(); i ++) {
                for (int j = 0; j < mazeData.getN(); j ++) {

                    if (mazeData.inMist[i][j] == true) {
                        MazeVisHelper.setColor(g2d, MazeVisHelper.Grey);
                    } else {
                        if (mazeData.maze[i][j] == MazeData.WALL) {
                            // set wall color to light blue
                            MazeVisHelper.setColor(g2d, MazeVisHelper.LightBlue);
                        } else { // set wall color to white
                            MazeVisHelper.setColor(g2d, MazeVisHelper.White);
                        }
                        // set exit color to green
                        if (i == mazeData.getExitRow() && j == mazeData.getExitCol()) {
                            MazeVisHelper.setColor(g2d, MazeVisHelper.Green);
                        }

                    }

                    // set start color to Yellow
                    if (i == mazeData.getEntranceRow() && j == mazeData.getEntranceCol()) {
                        MazeVisHelper.setColor(g2d, MazeVisHelper.Yellow);
                    }

                    // set DFS path to Yellow
                    if (mazeData.inDFSPath[i][j]) {
                        MazeVisHelper.setColor(g2d, MazeVisHelper.Yellow);
                    }

                    // set BFS path to Orange
                    if (mazeData.inBFSPath[i][j]) {
                        MazeVisHelper.setColor(g2d, MazeVisHelper.Orange);
                    }

                    // set wall follower path to Chocolate
                    if (mazeData.inWallPath[i][j]) {
                        MazeVisHelper.setColor(g2d, MazeVisHelper.Chocolate);
                    }

                    // set Player to a black rectangle
                    if (i == mazeData.getPlayer().getPosition().getRow() && j == mazeData.getPlayer().getPosition().getCol()) {
                        MazeVisHelper.setColor(g2d, MazeVisHelper.Black);
                    }

                    // draw the rectangle
                    MazeVisHelper.fillRectangle(g2d, j*blockSize, i*blockSize, blockSize, blockSize);

                }
            }
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(windowWidth, windowHeight-101);
        }

    }

}
