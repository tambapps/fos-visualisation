package com.tambapps.papernet.configuration;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

  private static final java.util.Properties PROPERTIES = new java.util.Properties();

  static {
    try (InputStream is = Properties.class.getResourceAsStream("/config.properties")) {
      PROPERTIES.load(is);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load properties", e);
    }
  }

  public String get(String key) {
    return PROPERTIES.getProperty(key);
  }

  public Integer getInt(String key) {
    return Integer.parseInt(PROPERTIES.getProperty(key));
  }

  public Integer getIntOrDefault(String key, int defaultValue) {
    if (!PROPERTIES.containsKey(key)) {
      return defaultValue;
    }
    return Integer.parseInt(PROPERTIES.getProperty(key));
  }

}
