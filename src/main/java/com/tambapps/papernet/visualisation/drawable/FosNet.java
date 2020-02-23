package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.gl.pool.LinkPool;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.view.Camera;
import com.tambapps.papernet.visualisation.animation.AlphaAnimation;
import com.tambapps.papernet.visualisation.animation.Animation;
import com.tambapps.papernet.visualisation.animation.MoveAnimation;
import com.tambapps.papernet.visualisation.animation.ShaderAlphaAnimation;
import com.tambapps.papernet.visualisation.animation.interpolation.Interpolation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FosNet {

  public static final int ALL_YEARS = -1;
  public static final float ALPHA_ANIMATION_DURATION = 1.5f;
  private static final float EXPAND_TIGHTEN_LENGTH = 30f;

  private final ResearchPaperData data;
  private int year;
  private Map<String, Bubble> cachedBubbles = new HashMap<>();
  private Map<String, Bubble> fosBubbles = new HashMap<>();
  private List<Bubble> currentBubbles;
  private List<Bubble> removedBubbles;
  private final List<Link> links = new ArrayList<>();

  private Bubble selectedBubble = null;

  public FosNet(ResearchPaperData data) {
    this.data = data;
  }

  public void loadYear(int year, Consumer<Animation> animationConsumer, float bubblesThreshold, float linkThreshold) {
    this.year = year;
    Collection<ResearchPaper> papers = year == ALL_YEARS ? data.getAllPapers() : data.getAllByYear(year);
    links.forEach(LinkPool::free);
    links.clear();
    Map<String, Bubble> newFosBubbles = BubblesNLink.generate(cachedBubbles, papers, links);
    removedBubbles = findRemovedBubbles(fosBubbles, newFosBubbles);
    List<Bubble> addedBubbles = findAddedBubbles(fosBubbles, newFosBubbles);

    this.fosBubbles = newFosBubbles;
    cachedBubbles.putAll(this.fosBubbles);
    currentBubbles = fosBubbles.values()
      .stream()
      .sorted(Comparator.comparing(Bubble::getRadius).reversed()) // in decroissant order to draw big bubbles first
      .collect(Collectors.toList());
    setLinksThreshold(linkThreshold);
    setBubblesThreshold(bubblesThreshold, linkThreshold);

    addedBubbles.stream()
      .filter(Drawable::isVisible)
      .map(this::showAnimation)
      .forEach(animationConsumer);

    removedBubbles.stream()
      .filter(Drawable::isVisible)
      .map(this::hideAnimation)
      .forEach(animationConsumer);

    arrangeNewBubbles(currentBubbles, addedBubbles);
    animationConsumer.accept(new ShaderAlphaAnimation(ShaderFactory.linksShader(), ALPHA_ANIMATION_DURATION, Link.MAX_ALPHA));
  }

  private void arrangeNewBubbles(List<Bubble> currentBubbles, List<Bubble> addedBubbles) {
    List<Bubble> notAddedBubbles = currentBubbles.stream()
      .filter(b -> !addedBubbles.contains(b))
      .collect(Collectors.toList());
    BubblesArranger.arrange(addedBubbles, notAddedBubbles);
  }

  private AlphaAnimation hideAnimation(Drawable drawable) {
    return new AlphaAnimation(drawable, ALPHA_ANIMATION_DURATION, true, () -> drawable.setVisible(false));
  }

  private AlphaAnimation showAnimation(Drawable drawable) {
    return new AlphaAnimation(drawable, ALPHA_ANIMATION_DURATION);
  }

  private List<Bubble> findAddedBubbles(Map<String, Bubble> fosBubbles, Map<String, Bubble> newFosBubbles) {
    Collection<Bubble> oldBubbles = fosBubbles.values();
    return newFosBubbles.values().stream()
      .filter(b -> !oldBubbles.contains(b))
      .collect(Collectors.toList());
  }

  private List<Bubble> findRemovedBubbles(Map<String, Bubble> fosBubbles, Map<String, Bubble> newFosBubbles) {
    Collection<Bubble> newBubbles = newFosBubbles.values();
    return fosBubbles.values().stream()
      .filter(b -> !newBubbles.contains(b))
      .collect(Collectors.toList());
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
      BubblesArranger.arrangeWithAnimation(currentBubbles, animationConsumer);
    } else {
      BubblesArranger.arrange(currentBubbles);
      links.forEach(Link::updatePos);
    }
  }

  public void updateLinksPos() {
    links.forEach(Link::updatePos);
  }

  public void draw(Matrix4f projection) {
    links.forEach(l -> l.draw(projection));
    currentBubbles.forEach(b -> b.draw(projection));
    removedBubbles.forEach(l -> l.draw(projection));
    if (!removedBubbles.isEmpty() && removedBubbles.get(0).getAlpha() <= 0) {
      removedBubbles.clear();
    }
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
      .filter(b -> b.isVisible() && intersect(b, projectPoint.x, projectPoint.y, camera.getZoom()))
      .findFirst().orElse(null);
    return selectedBubble;
  }

  private boolean intersect(Bubble bubble, float x, float y, float zoom) {
    return pow2(x - bubble.getX()) + pow2(y - bubble.getY() - 5) // little offset?
      < pow2(bubble.getRadius() / zoom);
  }

  private float pow2(float x) {
    return x * x;
  }

  public void stretch(Consumer<Animation> animationConsumer) {
    stretch(animationConsumer, (Float::sum));
  }

  public void tighten(Consumer<Animation> animationConsumer) {
    stretch(animationConsumer, (f1, f2) -> f1 - f2);
  }

  private void stretch(Consumer<Animation> animationConsumer, BinaryOperator<Float> operator) {
    Vector2f tempVec = new Vector2f();
    for (Bubble bubble : getBubbles()) {
      tempVec = tempVec.set(bubble.getX(), bubble.getY())
        .normalize(EXPAND_TIGHTEN_LENGTH);
      animationConsumer.accept(
        new MoveAnimation(bubble, operator.apply(bubble.getX(), tempVec.x), operator.apply(bubble.getY(), tempVec.y),
          1f, Interpolation.pow2Out()));
    }
  }

  public int getYear() {
    return year;
  }

  public List<Bubble> getBubbles() {
    return currentBubbles;
  }
}
