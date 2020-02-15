package com.tambapps.papernet;

import com.tambapps.papernet.data.ResearchPaperData;
import com.tambapps.papernet.data.ResearchPaperDataParser;
import com.tambapps.papernet.gl.GlWindow;

import com.tambapps.papernet.gl.shader.Color;
import com.tambapps.papernet.gl.shader.ColorShader;
import com.tambapps.papernet.gl.shader.ShaderFactory;
import com.tambapps.papernet.gl.text.Text;
import com.tambapps.papernet.gl.texture.Texture;
import com.tambapps.papernet.visualisation.drawable.Bubble;
import com.tambapps.papernet.visualisation.drawable.Bubbles;
import com.tambapps.papernet.visualisation.drawable.FosNet;
import com.tambapps.papernet.visualisation.drawable.Link;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.Scanner;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Main extends GlWindow {

  private static final Color SELECTED_COLOR = new Color(0, 1, 0);

  private static final int NAVIGATION_OFFSET = 8;
  private static final float THRESHOLD_OFFSET = 0.1f;
  private static final float NAVIGATION_ZOOM_OFFSET = 0.0333333f;

  private static final Vector3f tempVec = new Vector3f();

  private final int initialYear;
  private final FosNet fosNet;
  private Color selectedBubbleColor = null;

  private Texture background;
  private float linkThreshold = Bubbles.MIN_LINK_WIDTH;
  private float bubbleThreshold = Bubbles.MIN_RADIUS;


  public Main(ResearchPaperData data, int year) {
    this.fosNet = new FosNet(data);
    this.initialYear = year;
  }

  @Override
  public void onGlContextInitialized() throws IOException {
    System.out.println("Started initializing OpenGL...");
    long startTime = System.currentTimeMillis();
    ShaderFactory.setLinkShader(ShaderFactory.rgbaShader(0.5f, 0.5f, 0.5f, Link.MAX_ALPHA));

    fosNet.loadYear(initialYear, this::addAnimation, bubbleThreshold, linkThreshold);
    shuffle(false);
    moveLinkThreshold(0); // to update links visibility
    background = Texture.newTexture("background.png");
    background.setWidth(1f);
    background.setHeight(1f);

    System.out.format("Finished initialization of OpenGL (in %ds)\n\n", (System.currentTimeMillis() - startTime) / 1000L);
    System.out.println("Press the arrow keys to move on the screen");
    System.out.println("pressed 'z' with up/down to zoom/unzoom from the screen");
    System.out.println("pressed 'l' with up/down to modify the threshold of links that will be displayed");
    System.out.println("pressed 'b' with up/down to modify the threshold of bubbles that will be displayed");
    System.out.println("click y/h to move through the years");
    System.out.println("Use S to shuffle the bubbles");
    System.out.println("You can also touch and drag a bubble to move it");
    System.out.println("Use ESCAPE to exit");
  }

  // TODO implement bubble search through user input?
  @Override
  public void onDraw(Matrix4f projection) {
    background.bind();
    background.draw();
    glEnable(GL_BLEND);
    fosNet.draw(projection);
    glDisable(GL_BLEND);

    glUseProgram(0);
    int year = fosNet.getYear();
    Text.drawString(year == FosNet.ALL_YEARS ? "All years" : String.valueOf(year), -7.25f, 6.75f, 0.5f, 10);
  }

  @Override
  public void onLeftPressed(Character pressedCharacter) {
    camera.addPosition(tempVec.set(NAVIGATION_OFFSET, 0, 0));
  }

  @Override
  public void onRightPressed(Character pressedCharacter) {
    camera.addPosition(tempVec.set(- NAVIGATION_OFFSET, 0, 0));
  }

  @Override
  public void onUpPressed(Character pressedCharacter) {
    upDownPressed(pressedCharacter, 1f);
  }

  private void upDownPressed(Character pressedCharacter, float factor) {
    if (pressedCharacter == null) {
      camera.addPosition(tempVec.set(0, -factor * NAVIGATION_OFFSET, 0));
    } else {
      switch (pressedCharacter) {
        case 'z':
          camera.zoomBy(-factor * NAVIGATION_ZOOM_OFFSET);
          break;
        case 'l':
          moveLinkThreshold(factor * THRESHOLD_OFFSET);
          break;
        case 'b':
          moveBubbleThreshold(factor * THRESHOLD_OFFSET);
          break;
      }
    }
  }

  private void moveYear(int offset) {
    int year = fosNet.getYear();
    if (year == 1957 && offset < 0 || year == 2019 && offset > 0) {
      return;
    }
    if (year == FosNet.ALL_YEARS) {
      year = 2019;
    } else {
      year += offset;
    }
    finishAnimations();
    fosNet.loadYear(year, this::addAnimation, bubbleThreshold, linkThreshold);
  }

  @Override
  public void onDownPressed(Character pressedCharacter) {
    upDownPressed(pressedCharacter, -1f);
  }

  @Override
  public void onEscapePressed() {
    close();
  }
  private void moveLinkThreshold(float offset) {
    if (linkThreshold <= Bubbles.MIN_LINK_WIDTH && offset < 0 ||
      linkThreshold >= Bubbles.MAX_LINK_WIDTH && offset > 0) {
      return;
    }
    linkThreshold += offset;
    fosNet.setLinksThreshold(linkThreshold);
    System.out.format("updated link threshold: %f.1\n", (linkThreshold - Bubbles.MIN_LINK_WIDTH) / (Bubbles.MAX_LINK_WIDTH - Bubbles.MIN_LINK_WIDTH));
  }

  private void moveBubbleThreshold(float offset) {
    if (bubbleThreshold <= Bubbles.MIN_RADIUS && offset < 0 ||
      bubbleThreshold >= Bubbles.MAX_RADIUS && offset > 0) {
      return;
    }
    bubbleThreshold += offset;
    fosNet.setBubblesThreshold(bubbleThreshold, linkThreshold);
    System.out.format("updated bubble threshold: %f.1\n", (linkThreshold - Bubbles.MIN_LINK_WIDTH) / (Bubbles.MAX_LINK_WIDTH - Bubbles.MIN_LINK_WIDTH));
  }

  @Override
  public void onKeyClicked(char c) {
    switch (c) {
      case 's': // shuffle
        shuffle(true);
        break;
      case 'y':
        moveYear(1);
        break;
      case 'h':
        moveYear(-1);
        break;

    }
  }

  @Override
  public void update(float delta) {
    if (isOneAnimationRunning()) {
      fosNet.updateLinksPos();
    }
  }

  private void shuffle(boolean withAnimation) {
    if (withAnimation) {
      clearAnimations();
      fosNet.shuffle(true, this::addAnimation);
    }  else {
      fosNet.shuffle();
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Started loading...");
    long startTime = System.currentTimeMillis();
    ResearchPaperData data =  ResearchPaperDataParser.parseData();
    System.out.format("Loaded all data (in %ds)\n", (System.currentTimeMillis() - startTime) / 1000L);
    System.out.println("Here is the number of research paper by year:");
    data.getPapersByYear().forEach((key, value) ->
      System.out.format("%d -> %d papers", key, value.size()).println());
    System.out.println("Enter the year or 'all' for all papers");
    try (Scanner scanner = new Scanner(System.in)) {
      int year;
      String s =  args.length > 0 ? args[0] : scanner.nextLine();
      if (s.equals("all") || s.isEmpty()) {
        year = FosNet.ALL_YEARS;
      } else {
        year = Integer.parseInt(s);
      }
      new Main(data, year).run();
    }
  }

  @Override
  public void onTouchDown(float x, float y) {
    Bubble selectedBubble = fosNet.select(camera, x, y);
    if (selectedBubble != null) {
      ColorShader shader = selectedBubble.getShader();
      selectedBubbleColor = shader.getColor();
      shader.setColor(SELECTED_COLOR);
    }
  }

  @Override
  public void onTouchUp() {
    Bubble selectedBubble = fosNet.getSelectedBubble();
    if (selectedBubble == null) {
      return;
    }
    selectedBubble.getShader().setColor(selectedBubbleColor);
  }

  @Override
  public void onMouseDragged(float x, float y) {
    Bubble selectedBubble = fosNet.getSelectedBubble();
    if (selectedBubble == null) {
      return;
    }
    Vector3f projectPoint = camera.projectPoint(x, y);
    selectedBubble.setPosition(projectPoint.x, projectPoint.y);
    fosNet.updateLinksPos();
  }
}