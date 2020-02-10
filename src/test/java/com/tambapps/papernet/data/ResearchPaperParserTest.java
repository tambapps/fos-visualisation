package com.tambapps.papernet.data;

import org.junit.Test;

import static com.tambapps.papernet.data.FosParser.AUTHORS;
import static com.tambapps.papernet.data.FosParser.FOS;
import static com.tambapps.papernet.data.FosParser.ID;
import static com.tambapps.papernet.data.FosParser.N_CITATION;
import static com.tambapps.papernet.data.FosParser.TITLE;
import static com.tambapps.papernet.data.FosParser.YEAR;
import static org.junit.Assert.assertEquals;

public class ResearchPaperParserTest {

  private static final int FOS_ID = 103547078;
  private static final int FOS_YEAR = 2011;
  private static final int FOS_CITATIONS = 0;

  private static final String TEST_LINE = "\"103547078\",\"implementation of, visualization analysis software ,for evaluating the ,changes of permafrost on qinghaitibet plateau\",\"jiuyuan huo:2704164559;yaonan zhang:2707035280\",\"2011\",\"0\",\"visualization:0.5529252;visual basic:0.493305981;contour line:0.5314303;subgrade:0.464531273;software:0.521496356;computer graphics (images):0.440734267;permafrost:0.512904465;heat transfer:0.400024116;computer science:0.422157228;plateau:0.390280515\"";

  @Test
  public void parseFieldsTest() {
    String[] fields = FosParser.parseFields(TEST_LINE);

    assertEquals(String.valueOf(FOS_ID), fields[ID]);
    assertEquals(FOS_ID, FosParser.parseId(fields));
    assertEquals("implementation of, visualization analysis software ,for evaluating the ,changes of permafrost on qinghaitibet plateau", fields[TITLE]);
    assertEquals("jiuyuan huo:2704164559;yaonan zhang:2707035280", fields[AUTHORS]);
    assertEquals(String.valueOf(FOS_YEAR), fields[YEAR]);
    assertEquals(String.valueOf(FOS_CITATIONS), fields[N_CITATION]);
    assertEquals("visualization:0.5529252;visual basic:0.493305981;contour line:0.5314303;subgrade:0.464531273;software:0.521496356;computer graphics (images):0.440734267;permafrost:0.512904465;heat transfer:0.400024116;computer science:0.422157228;plateau:0.390280515", fields[FOS]);
  }

  @Test
  public void parseFosFromFields() {
    String[] fields = FosParser.parseFields(TEST_LINE);
    ResearchPaper researchPaper = FosParser.parse(fields);
    assertEquals(FOS_YEAR, researchPaper.getYear());
  }

}
