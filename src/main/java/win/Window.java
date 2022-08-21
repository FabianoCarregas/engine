package win;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width;
    private int height;
    private String title;
    private Long glfwWindow;

    private static Window window= null;

    public Window() {
        width = 1920;
        height = 1080;
        title = "R&M";
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

//        Make the OpenGl context current
        glfwMakeContextCurrent(glfwWindow);
//        Enable v-sync
        glfwSwapInterval(1);

//        Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    public void loop(){
        while (!glfwWindowShouldClose(glfwWindow)) {
//            Poll events
            glfwPollEvents();

            glClearColor(1.0f, 0.0f,0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }
}
