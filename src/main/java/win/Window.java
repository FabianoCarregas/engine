package win;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width;
    private int height;
    private String title;
    private Long glfwWindow;
    public float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window= null;

    private static Scene currentScene = null;

    public Window() {
        width = 1920;
        height = 1080;
        title = "R&M";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
            currentScene = new LevelEditorScene();
//            currentScene.init()
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene ' " +newScene + "'";
                break;
        }
    }

    public static Window get() {
        if(Window.window ==  null) {
            Window window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("LWJGL" +  Version.getVersion());
        init();
        loop();

//        free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

//        Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
//        err callback
        GLFWErrorCallback.createPrint(System.err).set();

//        initialize GLFW
        if(!glfwInit()) {
            throw new IllegalStateException("unable to initialize GLFW");
        }

//        configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

//        create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL) {
            throw new IllegalStateException("The GLFW window could not be created");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

//        Make the OpenGl context current
        glfwMakeContextCurrent(glfwWindow);
//        Enable v-sync
        glfwSwapInterval(1);

//        Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = 1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
//            Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt > 0) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
