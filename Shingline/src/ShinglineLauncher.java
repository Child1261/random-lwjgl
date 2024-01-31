public class ShinglineLauncher {
    public static ShinglineWindow window;
    public static ShinglineManager controller;
    public static testGame game;

    public static void main(String[] args) {
        window = new ShinglineWindow(1600, 900, "Test", true);
        controller = new ShinglineManager();
        game = new testGame();
        try {
            controller.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ShinglineWindow getWindow(){
        return window;
    }

    public static ShinglineManager getController(){
        return controller;
    }

    public static testGame getGame(){
        return game;
    }
}
