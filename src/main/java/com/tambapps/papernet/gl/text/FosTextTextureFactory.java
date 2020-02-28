package com.tambapps.papernet.gl.text;

import com.tambapps.papernet.gl.texture.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FosTextTextureFactory {

  private static final Map<String, Texture> FOS_TEXTURE_MAP = new HashMap<>();
  private static final char SPACE = ' ';
  private static final char LINE_RETURN = '\n';
  private static final Font FONT = new Font("Arial", Font.PLAIN, 64);

  private static String renderedText(String text) {
    StringBuilder builder = new StringBuilder();
    boolean firstSpace = false;
    for (char c : text.toCharArray()) {
      if (c == SPACE) {
        builder.append((firstSpace = !firstSpace) ? LINE_RETURN : c);
      } else {
        builder.append(c);
      }
    }
    return builder.toString();
  }

  // create a bufferedimage of the fos text and then convert it to a texture
  private static Texture createTexture(String fos) {
    String renderedText = renderedText(fos);
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    g2d.setFont(FONT);
    FontMetrics fm = g2d.getFontMetrics();
    String[] lines = renderedText.split("\n");
    int width = Arrays.stream(lines).mapToInt(fm::stringWidth).max().orElse(1);
    int height = fm.getHeight();
    g2d.dispose();

    img = new BufferedImage(width, height * (lines.length + 1), BufferedImage.TYPE_INT_ARGB);
    g2d = img.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
        RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    g2d.setFont(FONT);
    fm = g2d.getFontMetrics();
    g2d.setColor(Color.WHITE);
    float y = fm.getAscent();
    for (String line : lines) {
      float lineWidth = fm.stringWidth(line);
      g2d.drawString(line, (width - lineWidth) / 2f, y);
      y += height;
    }
    g2d.dispose();
    return Texture.fromBufferedImage(img);
  }

  public static Texture getTextureFor(String fos) {
    return FOS_TEXTURE_MAP.computeIfAbsent(fos, FosTextTextureFactory::createTexture);
  }

}
