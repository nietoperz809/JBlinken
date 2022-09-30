import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    Blinkenhouse blink = new Blinkenhouse();

    public MainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout (new BorderLayout());
        add (BorderLayout.CENTER, blink.gePanel());
        setSize (500,500);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame mf = new MainFrame();

    }
}
