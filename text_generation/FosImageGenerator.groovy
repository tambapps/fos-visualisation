// https://stackoverflow.com/questions/18800717/convert-text-content-to-image

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.Future;

void saveTextToImage(String text) {
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    Font font = new Font("Arial", Font.PLAIN, 48);
    g2d.setFont(font);
    FontMetrics fm = g2d.getFontMetrics();
    int width = fm.stringWidth(text);
    int height = fm.getHeight();
    g2d.dispose();

    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    g2d = img.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    g2d.setFont(font);
    fm = g2d.getFontMetrics();
    g2d.setColor(Color.WHITE);
    g2d.drawString(text, 0, fm.getAscent());
    g2d.dispose();
    def imagesDir = new File("fos_images")
    if (!imagesDir.exists()) {
        imagesDir.mkdir()
    }
    ImageIO.write(img, "png", new File(imagesDir, "${text.hashCode()}.png"))
}

int nbThreads = Runtime.getRuntime().availableProcessors()
println("Using $nbThreads threads")

Executor executor = Executors.newFixedThreadPool(nbThreads)
List<Future> futures = []

long startTime = System.currentTimeMillis()
new File('../src/main/resources/data/fos.csv').withReader { BufferedReader reader ->
    reader.readLine() // skip first line
    String line
    while ((line = reader.readLine()) != null) {
        int commaIndex = line.lastIndexOf(',');
        String fos = line.substring(0, commaIndex).replaceAll("\"", ""); // copied from ResearchPaperParser
        int occ = Integer.parseInt(line.substring(commaIndex + 1));
        if (true || occ >= 100) {
            futures += executor.execute {
                saveTextToImage(fos)
            }
        }
    }
}
futures*.get()
executor.shutdownNow()
println("it took ${(System.currentTimeMillis() - startTime) / 1000L}s to save all fos images")