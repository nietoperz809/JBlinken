import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Blinkenhouse {
    // Relative offsets of windows
    private static final int[] xp
            = {23, 36, 49, 62, 75, 88, 101, 114, 127, 140, 153, 166, 179, 192, 205, 218, 231, 244};
    private static final int[] yp
            = {46, 70, 94, 118, 142, 166, 190, 214};
    private final boolean[][] state = new boolean[xp.length][yp.length];
    private final BufferedImage houseImg;
    private final BufferedImage winImg;
    private final BufferedImage offImg;
    private final Graphics2D offG;
    private final JPanel panel;

    public Blinkenhouse() {
        houseImg = Utils.getImg("HOUSE.bmp");
        winImg = Utils.getImg("WINDOW.bmp");
        offImg = new BufferedImage
                (houseImg.getWidth(), houseImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        offG = (Graphics2D) offImg.getGraphics();
        clear();
        panel = new JPanel() {
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setDialogTitle("Load and play movie file ...");
                            fileChooser.showOpenDialog(Blinkenhouse.this.panel);
                            return;
                        }
                        float xscale = (float) houseImg.getWidth() / (float) getWidth();
                        float yscale = (float) houseImg.getHeight() / (float) getHeight();
                        int x = (int) (e.getX() * xscale);
                        int y = (int) (e.getY() * yscale);
                        int ix = -1;
                        int iy = -1;
                        for (int s = 0; s < xp.length; s++) {
                            if (Utils.isBetween(x, xp[s], xp[s] + winImg.getWidth())) {
                                ix = s;
                                break;
                            }
                        }
                        for (int s = 0; s < yp.length; s++) {
                            if (Utils.isBetween(y, yp[s], yp[s] + winImg.getHeight())) {
                                iy = s;
                                break;
                            }
                        }
                        if (ix == -1 || iy == -1)
                            return;
                        state[ix][iy] = !state[ix][iy];
                        clear();
                        drawWindows();
                        repaint();
                    }
                });
            }

            @Override
            public void paint(Graphics g) {
                g.drawImage(offImg, 0, 0, getWidth(), getHeight(), null);
            }
        };
    }

    public BufferedImage getImg() {
        return offImg;
    }

    public JPanel gePanel() {
        return panel;
    }

    public void clear() {
        offG.drawImage(houseImg, 0, 0, null);
    }

    public void drawWindows() {
        for (int i = 0; i < state[0].length; i++) {
            for (int j = 0; j < state.length; j++) {
                if (state[j][i])
                    offG.drawImage(winImg, xp[j], yp[i], null);
            }
        }
    }

    public void set(int x, int y) {
        offG.drawImage(winImg, xp[x], yp[y], null);
    }
}
