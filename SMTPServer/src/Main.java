public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        ServerMain serverMain = new ServerMain(8029);
        serverMain.start();

    }
}