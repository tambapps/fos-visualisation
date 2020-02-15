package com.tambapps.papernet.gl.pool;

import com.tambapps.papernet.visualisation.drawable.Bubble;
import com.tambapps.papernet.visualisation.drawable.Link;

import java.util.LinkedList;
import java.util.Queue;

public class LinkPool {

  private static final Queue<Link> FREE_LINKS = new LinkedList<>();

  public static Link get(Bubble b1, Bubble b2, float width) {
    if (FREE_LINKS.isEmpty()) {
      return Link.newLink(b1, b2, width);
    }
    return FREE_LINKS.remove();
  }

  public static void free(Link link) {
    FREE_LINKS.add(link);
  }
}
