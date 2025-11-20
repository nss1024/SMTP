import mda.MdaMain;
import resolver.Lookup;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        MdaMain mdaMain = new MdaMain();
        mdaMain.start();
        ServerMain serverMain = new ServerMain(8029,mdaMain);
        serverMain.start();
    }
}