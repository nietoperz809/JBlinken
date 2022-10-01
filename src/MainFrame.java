import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setIconImage (Utils.getImg("bm_play.jpg"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout (new BorderLayout());
        add (BorderLayout.CENTER, new Blinkenhouse());
        setSize (500,500);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame mf = new MainFrame();

    }
}
