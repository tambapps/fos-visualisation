package com.tambapps.papernet.data;

import java.util.HashMap;
import java.util.Map;

class FosParser {

  static final char DOUBLE_QUOTE = '"';
  static final char COMMA = ',';
  static final int FIELDS = 6;
  static final int ID = 0;
  static final int TITLE = 1;
  static final int AUTHORS = 2;
  static final int YEAR = 3;
  static final int N_CITATION = 4;
  static final int FOS = 5;

  private FosParser() {}

  public static ResearchPaper parse(String[] fields) {
    return new ResearchPaper(parseFosWeights(fields[FOS]), Integer.parseInt(fields[YEAR]), Integer.parseInt(fields[N_CITATION]));
  }

  private static Map<String, Float> parseFosWeights(String field) {
    Map<String, Float> fosWeights = new HashMap<>();
    for (String entry : field.split(";")) {
      String[] entryFields = entry.trim().split(":");
      fosWeights.put(entryFields[0], Float.parseFloat(entryFields[1]));
    }
    return fosWeights;
  }

  public static String[] parseFields(String line) {
    String[] fields = new String[FIELDS];
    int i = 0;
    int currentField = 0;
    boolean inField = false;
    StringBuilder builder = new StringBuilder();
    while (i < line.length()) {
      char c = line.charAt(i);
      i++;
      if (c == DOUBLE_QUOTE) {
        if (inField) {
          fields[currentField++] = builder.toString();
          builder.setLength(0); // clear builder
        }
        inField = !inField;
        continue;
      }
      if (!inField && c == COMMA) {
        continue;
      }
      if (inField) {
        builder.append(c);
      }
    }
    return fields;
  }

  public static int parseId(String[] fields) {
    return Integer.parseInt(fields[ID]);
  }
}
