package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.gl.view.Camera;
import com.tambapps.papernet.visualisation.animation.Animation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FosNet {

  public static final int ALL_YEARS = -1;

  private final ResearchPaperData data;
  private int year;
  private Map<String, Bubble> fosBubbles = new HashMap<>();
  private List<Bubble> currentBubbles;
  private final List<Link> links = new ArrayList<>();

  private Bubble selectedBubble = null;

  public FosNet(ResearchPaperData data) {
    this.data = data;
  }

  public void loadYear(int year) {
    this.year = year;
    Collection<ResearchPaper> papers = year == ALL_YEARS ? data.getAllPapers() : data.getAllByYear(year);
    fosBubbles = Bubbles.toBubbles(fosBubbles, papers, links);
    currentBubbles = fosBubbles.values()
      .stream()
      .sorted(Comparator.comparing(Bubble::getRadius).reversed()) // in decroissant order to draw big bubbles first
      .collect(Collectors.toList());
  }

  public List<Bubble> getCurrentBubbles() {
    return currentBubbles;
  }

  public void setSelectedBubble(Bubble selectedBubble) {
    this.selectedBubble = selectedBubble;
  }

  public Bubble getSelectedBubble() {
    return selectedBubble;
  }

  // shuffle without animation
  public void shuffle() {
    shuffle(false, null);
  }

  public void shuffle(boolean withAnimation, Consumer<Animation> animationConsumer) {
    if (withAnimation) {
      BubblesArranger.arrangeWithAnimation(fosBubbles.values(), animationConsumer);
    } else {
      BubblesArranger.arrange(fosBubbles.values());
      links.forEach(Link::updatePos);
    }
  }

  public void updateLinksPos() {
    links.forEach(Link::updatePos);
  }

  public void draw(Matrix4f projection) {
    links.forEach(l -> l.draw(projection));
    currentBubbles.forEach(b -> b.doDraw(projection));
  }

  public void setLinksThreshold(float threshold) {
    links.forEach(l -> l.setVisible(l.getWidth() >= threshold));
  }

  public void setBubblesThreshold(float bubblesThreshold, float linkThreshold) {
    currentBubbles.forEach(bubble -> bubble.setVisible(bubble.getRadius() >= bubblesThreshold));
    links.forEach(Link::update);
    links.forEach(l -> l.setVisible(l.getWidth() >= linkThreshold)); // re update links visibility
  }

  public Bubble select(Camera camera, float x, float y) {
    Vector3f projectPoint = camera.projectPoint(x, y);
    selectedBubble = currentBubbles.stream()
      .filter(b -> intersect(b, projectPoint.x, projectPoint.y))
      .findFirst().orElse(null);
    return selectedBubble;
  }

  private boolean intersect(Bubble bubble, float x, float y) {
    return pow2(x - bubble.getX()) + pow2(y - bubble.getY() - 5) // - 10 is for the app bar TODO there is still an offset
      < pow2(bubble.getRadius());
  }

  private float pow2(float x) {
    return x * x;
  }

}
