package com.tambapps.papernet.gl.texture;


import lombok.Value;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

@Value
public class Texture {

  private int id;
  private int width;
  private int height;


  public void bind() {
    glBindTexture(GL_TEXTURE_2D, id);
  }

  public static Texture newTexture(String path) throws IOException {
    BufferedImage image = ImageIO.read(Texture.class.getResourceAsStream("/textures/" + path));
    int width = image.getWidth();
    int height = image.getHeight();
    int[] pixelsRaws;// = new int[width * height * 4];
    pixelsRaws = image.getRGB(0, 0, width, height, null ,0, width);

    ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int pixel = pixelsRaws[i*width + j];
        pixels.put((byte)((pixel >> 16) & 0xFF)); // red
        pixels.put((byte)((pixel >> 8) & 0xFF)); //green
        pixels.put((byte)((pixel) & 0xFF)); // blue
        pixels.put((byte)((pixel >> 24) & 0xFF));// alpha
      }
    }
    pixels.flip();
    int id = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, id);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
      0, GL_RGBA, GL_BYTE, pixels);
    //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height,
    //  0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

    return new Texture(id, width, height);
  }

  public void draw() {
    glUseProgram(0); // penser a reset le shader quand drawing quelquechose
    glBegin(GL_QUADS);
    glTexCoord2f(0, 0);
    glVertex2f(-0.5f, 0.5f);

    glTexCoord2f(1, 0);
    glVertex2f(0.5f, 0.5f);

    glTexCoord2f(1, 1);
    glVertex2f(0.5f, -0.5f);

    glTexCoord2f(0, 1);
    glVertex2f(-0.5f, -0.5f);
    glEnd();
  }
}
