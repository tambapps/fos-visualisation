package com.tambapps.papernet.gl.text;

import com.tambapps.papernet.gl.texture.Texture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FosTextTextureFactory {

  private static final Map<String, Texture> FOS_TEXTURE_MAP = new HashMap<>();

  private static Texture createTexture(String fos) {
    String textureName = fos.hashCode() + ".png";
    try {
      return Texture.newTexture("fos_images/" + textureName);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load texture " + textureName, e);
    }
  }

  public static Texture getTextureFor(String fos) {
    return FOS_TEXTURE_MAP.computeIfAbsent(fos, FosTextTextureFactory::createTexture);
  }
}
