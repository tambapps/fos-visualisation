package com.tambapps.papernet.gl;

import com.tambapps.papernet.gl.input.InputHandler;
import com.tambapps.papernet.gl.input.InputListener;
import com.tambapps.papernet.gl.input.MouseInputHandler;
import com.tambapps.papernet.gl.input.MouseListener;
import com.tambapps.papernet.gl.view.Camera;
import com.tambapps.papernet.visualisation.animation.Animation;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class GlWindow implements InputListener, MouseListener {

  public static final int WINDOW_WIDTH = 1000;
  public static final int WINDOW_HEIGHT = 720;

  // The window handle
  private long window;
  protected final Camera camera = new Camera(WINDOW_WIDTH, WINDOW_HEIGHT);
  private InputHandler inputHandler;
  private MouseInputHandler mouseInputHandler;
  private final List<Animation> animations = new ArrayList<>();

  public void run() {
    init();
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();
    // enable transparency
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glEnable(GL_TEXTURE_2D);
    // Set the clear color
    glClearColor(.0f, 0.0f, 0.0f, 0.0f);

    try {
      onGlContextInitialized();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if ( !glfwInit() )
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Research Paper FOS network", NULL, NULL);
    if ( window == NULL )
      throw new RuntimeException("Failed to create the GLFW window");
    glfwSetKeyCallback(window, inputHandler = new InputHandler(this));
    mouseInputHandler = new MouseInputHandler(this);
    mouseInputHandler.addInput(window);

    // Get the thread stack and push a new frame
    try ( MemoryStack stack = stackPush() ) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
        window,
        (vidmode.width() - pWidth.get(0)) / 2,
        (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  private void loop() {

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( !glfwWindowShouldClose(window) ) {
      Matrix4f projection = camera.getProjection();
      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();

      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

      update();
      onDraw(projection);
      glfwSwapBuffers(window); // swap the color buffers
    }
  }

  private void update() {
    float delta = 0.025f; // TODO handle better delta (use real time)
    Character pressedCharacter = null; // z, l, b and ctrl are pressable
    if (glfwGetKey(window, GLFW_KEY_W) == GL_TRUE) { // z
      pressedCharacter = 'z';
    } else if (glfwGetKey(window, GLFW_KEY_L) == GL_TRUE) { // z
      pressedCharacter = 'l';
    } else if (glfwGetKey(window, GLFW_KEY_B) == GL_TRUE) { // z
      pressedCharacter = 'b';
    }  else if (glfwGetKey(window, GLFW_KEY_Y) == GL_TRUE) { // z
      pressedCharacter = 'y';
    }
    inputHandler.update(pressedCharacter);
    Iterator<Animation> animationIterator =  animations.iterator();
    while (animationIterator.hasNext()) {
      Animation animation = animationIterator.next();
      if (animation.isComplete()) {
        animationIterator.remove();
      } else {
        animation.act(delta);
      }
    }
    update(delta);
  }

  public void update(float delta) { }

  public abstract void onDraw(Matrix4f projection);
  public abstract void onGlContextInitialized() throws IOException;

  protected void close() {
    glfwSetWindowShouldClose(window, true);
  }

  protected void addAnimation(Animation animation) {
    this.animations.add(animation);
  }

  protected void clearAnimations() {
    animations.clear();
  }

  protected boolean isOneAnimationRunning() {
    return !animations.isEmpty();
  }

  public void finishAnimations() {
    Iterator<Animation> animationIterator =  animations.iterator();
    while (animationIterator.hasNext()) {
      Animation animation = animationIterator.next();
      animation.finish();
      animationIterator.remove();
    }
  }

}