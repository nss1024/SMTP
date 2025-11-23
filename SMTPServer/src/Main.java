
import mda.MdaMain;

import resolver.Lookup;
import serverConfigs.LoadConfigs;
import serverConfigs.ServerConfigs;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        ServerConfigs CONFIGS =  LoadConfigs.load(Paths.get("C:/dev/FileStore/smtp.properties"));
        Lookup lookup=new Lookup();
        lookup.start();
        MdaMain mdaMain = new MdaMain(CONFIGS);
        mdaMain.start();
        mdaMain.setLookup(lookup);
        ServerMain serverMain = new ServerMain(mdaMain,CONFIGS);
        serverMain.start();
        System.out.println(
                LoadConfigs.class.getClassLoader().getResource("logback.xml")
        );

    }
}