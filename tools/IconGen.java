import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class IconGen {

    public static void main(String[] args) throws Exception {
        int size = 256;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color bg = new Color(0x1d2733);
        Color panel = new Color(0x27333f);
        Color accent = new Color(0x5ec6a8);
        Color text = new Color(0xf2f4f5);

        g.setColor(bg);
        g.fillRect(0, 0, size, size);

        g.setColor(panel);
        g.fillRect(18, 18, size - 36, size - 36);

        g.setColor(accent);
        g.fillRect(18, 18, size - 36, 14);
        g.fillRect(18, size - 32, size - 36, 14);

        int maxWidth = size - 72;
        drawCentered(g, "FONT", fitFont(g, "FONT", Font.BOLD, 70, maxWidth), text, size, 112);
        drawCentered(g, "GUARD", fitFont(g, "GUARD", Font.BOLD, 70, maxWidth), accent, size, 188);

        g.dispose();

        File out = new File(args.length > 0 ? args[0] : "icon.png");
        ImageIO.write(img, "png", out);
        System.out.println("wrote " + out.getAbsolutePath());
    }

    private static Font fitFont(Graphics2D g, String s, int style, int startSize, int maxWidth) {
        FontRenderContext frc = g.getFontRenderContext();
        int pt = startSize;
        while (pt > 10) {
            Font f = new Font("SansSerif", style, pt);
            if (f.getStringBounds(s, frc).getWidth() <= maxWidth) {
                return f;
            }
            pt--;
        }
        return new Font("SansSerif", style, 10);
    }

    private static void drawCentered(Graphics2D g, String s, Font font, Color color, int size, int baseline) {
        g.setFont(font);
        g.setColor(color);
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(s, frc);
        int x = (int) ((size - bounds.getWidth()) / 2.0);
        g.drawString(s, x, baseline);
    }
}
