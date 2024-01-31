import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class ShinglineWindow {

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    private String title;
    private int width, height;
    private long window;
    
    private boolean max, vSync, resizeable;

    private int frames;
    private static long time;

    private final Matrix4f projectionMatrix;

    public ShinglineWindow(int width, int height, String title, boolean vSync){
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    public void init(){
        //Initialize the window.
        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Cannot start");
        }
        setGLFWHints();

        //Create the window.
        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if(window == MemoryUtil.NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        setBindings();
        setMax();
        //Enable vSync if user wants vSync enabled.
        if(vSync){
            GLFW.glfwSwapInterval(1);
        }

        //Show window and create capabilities.
        GLFW.glfwShowWindow(window);
        GL.createCapabilities();
    }

    public void update(){
        GLFW.glfwSwapBuffers(window);
        //Tells openGL to start rendering all objects we have placed in a queue.
        GLFW.glfwPollEvents();
    }

    public void cleanUp(){
        GLFW.glfwDestroyWindow(window);
    }

    public boolean windowShouldClose(){
        return GLFW.glfwWindowShouldClose(window);
    }

    public void setMax(){
        max = false;
        //Determine if start maximised.
        if(width == 0 || height == 0){
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            max = true;
        }

        //If max, make max, otherwise put the window in the centre of primary monitor.
        if(max){
            GLFW.glfwMaximizeWindow(window);
        }else{
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);
    }

    public void setBindings(){
        //Every time window is resized, update width and height of window.
        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResizeable(true);
        });

        //Bind escape key to closing the window.
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE){
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });
    }

    public void setGLFWHints(){
        GLFW.glfwDefaultWindowHints();
        //We do not want the window to be visible to start with.
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        //Set resizeable
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);

        //Setting versions
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        //Set Profile
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
    }

    /*
     * Getters and setters.
     * 
     */

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    public void setClearColour(float r, float g, float b, float a){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(r,g,b,a);
    }

    public void setResizeable(boolean b){
        resizeable = b;
    }

    public boolean getResizeable(){
        return resizeable;
    }

    public void setVSync(boolean b){
        vSync = b;
    }

    public boolean getVSync(){
        return vSync;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public long getWindow(){
        return window;
    }

    public boolean isKeyPressed(int keycode){
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }
    public Matrix4f updateProjectionMatrix(){
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height){
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

}
