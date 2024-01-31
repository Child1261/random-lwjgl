import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class ShinglineManager {
    private boolean isRunning;

    private ShinglineWindow window;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private static final long NANOSECOND = 1000000000L;
    private static final float FRAMERATE = 1000;
    private static int fps;

    private static float frameTime = 1.0f / FRAMERATE;

    private void init() throws Exception{
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = ShinglineLauncher.getWindow();
        gameLogic = ShinglineLauncher.getGame();
        window.init();
        gameLogic.init();

    }

    public void start() throws Exception{
        init();
        if(isRunning){
            return;
        }
        run();
    }

    public void run(){
        isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while(unprocessedTime > frameTime){
                render = true;
                unprocessedTime -= frameTime;

                if(window.windowShouldClose()){
                    stop();
                }

                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    window.setTitle(window.getTitle() + " ~~~ Current FPS: " +  getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render){
                update();
                render();
                frames++;
            }
        }
        cleanUp();
    }

    public void stop(){
        if(!isRunning){
            return;
        }
        isRunning = false;
    }

    public void input(){
        gameLogic.input();
    }

    public void render(){
        gameLogic.render();
        window.update();
    }
    public void update(){
        gameLogic.update();
    }

    public void cleanUp(){
        window.cleanUp();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    /*
     * Getters and setters
     */

     private static int getFps(){
        return fps;
     }

     public static void setFps(int fps){
        ShinglineManager.fps = fps;
     }
}
