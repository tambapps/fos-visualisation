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

  public static String get(String key) {
    return PROPERTIES.getProperty(key);
  }

  public static Integer getInt(String key) {
    String number = PROPERTIES.getProperty(key);
    if (number == null) {
      return null;
    }
    return Integer.parseInt(number);
  }

  public  static Integer getIntOrDefault(String key, int defaultValue) {
    if (!PROPERTIES.containsKey(key)) {
      return defaultValue;
    }
    return Integer.parseInt(PROPERTIES.getProperty(key));
  }

}
