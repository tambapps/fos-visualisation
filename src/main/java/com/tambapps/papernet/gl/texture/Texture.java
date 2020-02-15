package com.tambapps.papernet.gl.texture;

import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.Data;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL20.glUseProgram;

@Data
public class Texture {

  private static final int BPP = 4; // RGBA

  private final int id;
  private float width;
  private float height;
  private float x;
  private float y;

  public Texture(int id, float width, float height) {
    this.id = id;
    this.width = width;
    this.height = height;
  }

  public void bind() {
    glBindTexture(GL_TEXTURE_2D, id);
  }

  public static Texture newTexture(String path) throws IOException {
    int width;
    int height;
    ByteBuffer buffer;

    try (InputStream is = Texture.class.getResourceAsStream("/textures/" + path)) {
      PNGDecoder decoder = new PNGDecoder(is);
      width = decoder.getWidth();
      height = decoder.getHeight();
      //we will decode to RGBA format, i.e. 4 components or "bytes per pixel"
      buffer = BufferUtils.createByteBuffer(BPP * width * height);
      decoder.decode(buffer, width * BPP, PNGDecoder.Format.RGBA);
      buffer.flip();
    }

    int id = glGenTextures();
    //bind the texture
    glBindTexture(GL_TEXTURE_2D, id);

    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

//upload our ByteBuffer to GL
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

    return new Texture(id, width, height);
  }

  public void draw() {
    glUseProgram(0); // penser a reset le shader quand drawing quelquechose
    glBegin(GL_QUADS);
    glTexCoord2f(0, 0);
    glVertex2f(x - width, y + -height);

    glTexCoord2f(1, 0);
    glVertex2f(x + width, y - height);

    glTexCoord2f(1, 1);
    glVertex2f(x + width, y + height);

    glTexCoord2f(0, 1);
    glVertex2f(x - width, y + height);
    glEnd();
  }
}
