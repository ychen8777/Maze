import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MazeVisHelper {

    private MazeVisHelper() {} // no instance

    public static final Color Red = new Color(0xF44336);
    public static final Color Blue = new Color(0x2196F3);
    public static final Color LightBlue = new Color(0x03A9F4);
    public static final Color Green = new Color(0x4CAF50);
    public static final Color Black = new Color(0x000000);
    public static final Color White = new Color(0xFFFFFF);
    public static final Color Grey = new Color(0x9E9E9E);
    public static final Color BlueGrey = new Color(0x607D8B);
    public static final Color Yellow = new Color(0xFFEB3B);
    public static final Color Orange = new Color(0xFF9800);
    public static final Color Chocolate = new Color(0xD2691E);

    public static void fillRectangle(Graphics2D g, int x, int y, int width, int height) {
        Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
        g.fill(rect);
    }

    public static void setColor(Graphics2D g, Color color) {
        g.setColor(color);
    }

    public static void pause(int t) {
        try{
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

}
