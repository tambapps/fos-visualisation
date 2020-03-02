package com.tambapps.papernet.visualisation.drawable;

import com.tambapps.papernet.data.ResearchPaper;
import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.gl.GlWindow;
import com.tambapps.papernet.gl.pool.LinkPool;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.view.Camera;
import com.tambapps.papernet.visualisation.animation.AlphaAnimation;
import com.tambapps.papernet.visualisation.animation.Animation;
import com.tambapps.papernet.visualisation.animation.MoveAnimation;
import com.tambapps.papernet.visualisation.animation.ShaderAlphaAnimation;
import com.tambapps.papernet.visualisation.animation.interpolation.Interpolation;
import com.tambapps.papernet.visualisation.drawable.arranger.NodesArranger;
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
  private static final float EXPAND_TIGHTEN_LENGTH = 150f;

  private final ResearchPaperData data;
  private final List<Link> links = new ArrayList<>();
  private int year;
  private Map<String, Node> cachedNodes = new HashMap<>();
  private Map<String, Node> fosNodes = new HashMap<>();
  private List<Node> currentNodes;
  private List<Node> removedNodes;
  private Node selectedNode = null;

  public FosNet(ResearchPaperData data) {
    this.data = data;
  }

  public void loadYear(int year, Consumer<Animation> animationConsumer, float nodesThreshold,
      float linkThreshold) {
    this.year = year;
    Collection<ResearchPaper> papers =
        year == ALL_YEARS ? data.getAllPapers() : data.getAllByYear(year);
    links.forEach(LinkPool::free);
    links.clear();
    Map<String, Node> newFosNodes = FosNetUtils.generate(cachedNodes, papers, links);
    removedNodes = findRemovedNodes(fosNodes, newFosNodes);
    List<Node> addedNodes = findAddedNodes(fosNodes, newFosNodes);

    this.fosNodes = newFosNodes;
    cachedNodes.putAll(this.fosNodes);
    currentNodes = fosNodes.values()
        .stream()
        .sorted(Comparator.comparing(Node::getRadius)
            .reversed()) // in descending order to draw big nodes first
        .collect(Collectors.toList());
    setLinksThreshold(linkThreshold);
    setNodesThreshold(nodesThreshold, linkThreshold);

    addedNodes.stream()
        .filter(Drawable::isVisible)
        .map(this::showAnimation)
        .forEach(animationConsumer);

    removedNodes.stream()
        .filter(Drawable::isVisible)
        .map(this::hideAnimation)
        .forEach(animationConsumer);

    arrangeNewNodes(currentNodes, addedNodes);
    animationConsumer.accept(
        new ShaderAlphaAnimation(ShaderFactory.linksShader(), ALPHA_ANIMATION_DURATION,
            Link.MAX_ALPHA));
  }

  private void arrangeNewNodes(List<Node> currentNodes, List<Node> addedNodes) {
    List<Node> notAddedNodes = currentNodes.stream()
        .filter(b -> !addedNodes.contains(b))
        .collect(Collectors.toList());
    NodesArranger.arrange(addedNodes, notAddedNodes);
  }

  private AlphaAnimation hideAnimation(Drawable drawable) {
    return new AlphaAnimation(drawable, ALPHA_ANIMATION_DURATION, true,
        () -> drawable.setVisible(false));
  }

  private AlphaAnimation showAnimation(Drawable drawable) {
    return new AlphaAnimation(drawable, ALPHA_ANIMATION_DURATION);
  }

  private List<Node> findAddedNodes(Map<String, Node> fosNodes,
                                    Map<String, Node> newFosNodes) {
    Collection<Node> oldNodes = fosNodes.values();
    return newFosNodes.values().stream()
        .filter(b -> !oldNodes.contains(b))
        .collect(Collectors.toList());
  }

  private List<Node> findRemovedNodes(Map<String, Node> fosNodes,
                                      Map<String, Node> newFosNodes) {
    Collection<Node> newNodes = newFosNodes.values();
    return fosNodes.values().stream()
        .filter(b -> !newNodes.contains(b))
        .collect(Collectors.toList());
  }

  public Node getSelectedNode() {
    return selectedNode;
  }

  // shuffle without animation
  public void shuffle() {
    shuffle(false, null);
  }

  public void shuffle(boolean withAnimation, Consumer<Animation> animationConsumer) {
    if (withAnimation) {
      NodesArranger.arrangeWithAnimation(currentNodes, animationConsumer);
    } else {
      NodesArranger.arrange(currentNodes);
      links.forEach(Link::updatePos);
    }
  }

  public void updateLinksPos() {
    links.forEach(Link::updatePos);
  }

  public void draw(Matrix4f projection) {
    links.forEach(l -> l.draw(projection));
    currentNodes.forEach(b -> b.draw(projection));
    removedNodes.forEach(l -> l.draw(projection));
    if (!removedNodes.isEmpty() && removedNodes.get(0).getAlpha() <= 0) {
      removedNodes.clear();
    }
  }

  public void setLinksThreshold(float threshold) {
    links.forEach(l -> l.setVisible(l.getWidth() >= threshold));
  }

  public void setNodesThreshold(float nodesThreshold, float linkThreshold) {
    currentNodes.forEach(node -> node.setVisible(node.getRadius() >= nodesThreshold));
    links.forEach(Link::update);
    links.forEach(l -> l.setVisible(l.getWidth() >= linkThreshold)); // re update links visibility
  }

  public Node select(Camera camera, float x, float y) {
    Vector3f projectPoint = camera.projectPoint(x, y);
    selectedNode = currentNodes.stream()
        .filter(
            b -> b.isVisible() && intersect(b, projectPoint.x, projectPoint.y, camera.getZoom()))
        .findFirst().orElse(null);
    return selectedNode;
  }

  private boolean intersect(Node node, float x, float y, float zoom) {
    return pow2(x - node.getX()) + pow2(y - node.getY() - 5) // little offset?
        < pow2(node.getRadius() / zoom);
  }

  private float pow2(float x) {
    return x * x;
  }

  public void stretch(Consumer<Animation> animationConsumer) {
    stretchOrTighten(animationConsumer, (Float::sum));
  }

  public void tighten(Consumer<Animation> animationConsumer) {
    stretchOrTighten(animationConsumer, (f1, f2) -> f1 - f2);
  }

  private void stretchOrTighten(Consumer<Animation> animationConsumer,
      BinaryOperator<Float> operator) {
    Vector2f tempVec = new Vector2f();
    for (Node node : getNodes()) {
      tempVec = tempVec.set(node.getX(), node.getY());
      float length = tempVec.length();
      tempVec = tempVec.set(node.getX(), node.getY())
          .normalize(EXPAND_TIGHTEN_LENGTH * length / GlWindow.WINDOW_HEIGHT);
      animationConsumer.accept(
          new MoveAnimation(node, operator.apply(node.getX(), tempVec.x),
              operator.apply(node.getY(), tempVec.y),
              1f, Interpolation.POW2_FAST_THEN_SLOW));
    }
  }

  public int getYear() {
    return year;
  }

  public List<Node> getNodes() {
    return currentNodes;
  }
}
