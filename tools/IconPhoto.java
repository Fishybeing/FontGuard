import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class IconPhoto {

    public static void main(String[] args) throws Exception {
        String in = args[0];
        String out = args[1];
        int size = 512;

        BufferedImage src = ImageIO.read(new File(in));
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int crop = Math.min(src.getWidth(), src.getHeight());
        int cx = (src.getWidth() - crop) / 2;
        int cy = (src.getHeight() - crop) / 2;
        g.drawImage(src, 0, 0, size, size, cx, cy, cx + crop, cy + crop, null);

        int bandH = 128;
        int bandY = (size - bandH) / 2;
        g.setColor(new Color(0, 0, 0, 238));
        g.fillRect(0, bandY, size, bandH);
        g.setColor(new Color(0x39ff6a));
        g.fillRect(0, bandY, size, 4);
        g.fillRect(0, bandY + bandH - 4, size, 4);

        Font font = fitFont(g, "FONTGUARD", Font.BOLD, 110, size - 60);
        g.setFont(font);
        java.awt.FontMetrics fm = g.getFontMetrics(font);
        int baseline = bandY + (bandH - fm.getHeight()) / 2 + fm.getAscent();
        drawWord(g, "FONTGUARD", font, size, baseline);

        g.dispose();
        ImageIO.write(img, "png", new File(out));
        System.out.println("wrote " + out);
    }

    private static Font fitFont(Graphics2D g, String s, int style, int startSize, int maxWidth) {
        FontRenderContext frc = g.getFontRenderContext();
        for (int pt = startSize; pt > 10; pt--) {
            Font f = new Font("Comic Sans MS", style, pt);
            if (f.getStringBounds(s, frc).getWidth() <= maxWidth) {
                return f;
            }
        }
        return new Font("Comic Sans MS", style, 10);
    }

    private static void drawWord(Graphics2D g, String s, Font font, int size, int baseline) {
        g.setFont(font);
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D b = font.getStringBounds(s, frc);
        int x = (int) ((size - b.getWidth()) / 2.0);
        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(s, x + 4, baseline + 4);
        g.setColor(new Color(0xf4f6f7));
        g.drawString(s, x, baseline);
    }
}
