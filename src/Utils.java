import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils
{
    private static final Random rnd = new Random ();

    public static int random (int min, int max) {
        return rnd.nextInt(max - min + 1) + min;
    }

    public static void delay(int ms)
    {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public static String getResourceText (String name) {
        InputStream is = Utils.getResource (name);
        return new BufferedReader (new InputStreamReader (Objects.requireNonNull (is)))
                .lines ().collect (Collectors.joining ("\n"));
    }

    public static BufferedImage getImg (String name) {
        try {
            return ImageIO.read(Utils.getResource(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isBetween (int v, int left, int right) {
        if (v<=left || v>=right)
            return false;
        return true;
    }

    public static InputStream getResource (String name)
    {
        InputStream is = ClassLoader.getSystemResourceAsStream (name);
        if (is == null)
        {
            System.out.println ("could not load: "+name);
            return null;
        }
        return new BufferedInputStream (is);
    }
}
