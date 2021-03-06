package com.tambapps.papernet.gl.pool;

import com.tambapps.papernet.visualisation.drawable.Node;
import com.tambapps.papernet.visualisation.drawable.Link;

import java.util.LinkedList;
import java.util.Queue;

public class LinkPool {

  private static final Queue<Link> FREE_LINKS = new LinkedList<>();

  public static Link get(Node b1, Node b2, float width) {
    if (FREE_LINKS.isEmpty()) {
      return Link.newLink(b1, b2, width);
    }
    Link link = FREE_LINKS.remove();
    link.init(b1, b2, width);
    return link;
  }

  public static void free(Link link) {
    FREE_LINKS.add(link);
  }
}
