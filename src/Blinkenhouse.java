import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Blinkenhouse {
    // Relative offsets of windows
    private static final int[] winPosX
            = {23, 36, 49, 62, 75, 88, 101, 114, 127, 140, 153, 166, 179, 192, 205, 218, 231, 244};
    private static final int[] winPosY
            = {46, 70, 94, 118, 142, 166, 190, 214};
    private final boolean[][] state = new boolean[winPosX.length][winPosY.length];
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
                setToolTipText("Right mouse button opens movie file ...");
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JFileChooser fc = new JFileChooser();
                            fc.setDialogTitle("Load and play movie file ...");
                            if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(Blinkenhouse.this.panel)) {
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        playFile(fc.getSelectedFile());
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                });
                            }
                            return;
                        }
                        handleMouseClicks(e);
                    }

                    private void handleMouseClicks(MouseEvent e) {
                        float xscale = (float) houseImg.getWidth() / (float) getWidth();
                        float yscale = (float) houseImg.getHeight() / (float) getHeight();
                        int x = (int) (e.getX() * xscale);
                        int y = (int) (e.getY() * yscale);
                        int ix = -1;
                        int iy = -1;
                        for (int s = 0; s < winPosX.length; s++) {
                            if (Utils.isBetween(x, winPosX[s], winPosX[s] + winImg.getWidth())) {
                                ix = s;
                                break;
                            }
                        }
                        for (int s = 0; s < winPosY.length; s++) {
                            if (Utils.isBetween(y, winPosY[s], winPosY[s] + winImg.getHeight())) {
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

    private void playBlock(String inStr) {
        if (inStr.isEmpty())
            return;
        try {
            inStr = inStr.substring(inStr.indexOf('@', 0));
        } catch (Exception e) {
            return;
        }
        String[] lines = inStr.split(System.lineSeparator());
        if (lines.length != 9)
            return;
        clear();
        for (int i = 0; i < 8; i++) {
            char[] bits = lines[i + 1].toCharArray();
            for (int j = 0; j < bits.length; j++) {
                if (bits[j] == '1') {
                    set(j, i);
                }
            }
        }
        Graphics g = panel.getGraphics();
        panel.paint(g);
        Utils.delay(Integer.parseInt(lines[0].substring(1)));
    }

    private void playFile(File f) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
        String str = new String(encoded, StandardCharsets.UTF_8);
        String[] blocks = str.split("((\\n\\r)|(\\r\\n)){2}|(\\r){2}|(\\n){2}");
        for (String s : blocks) {
            playBlock(s);
        }
        JOptionPane.showMessageDialog(panel, "All done!", "InfoBox:",
                JOptionPane.INFORMATION_MESSAGE);
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
                    offG.drawImage(winImg, winPosX[j], winPosY[i], null);
            }
        }
    }

    public void set(int x, int y) {
        offG.drawImage(winImg, winPosX[x], winPosY[y], null);
    }
}
