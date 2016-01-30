package co.valdeon.titan.backupsystem.server.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Properties settings = new Properties();

    public Settings() {
        File f = new File("config.properties");
        if (f.exists()) {
            try {
                settings.load(new FileInputStream("config.properties"));
            }catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            int port = 8000;
            Properties p = new Properties();
            p.setProperty("port", Integer.toString(port));
            try {
                if (f.createNewFile()) {
                    p.store(new FileOutputStream("config.properties"), null);
                    settings.load(new FileInputStream("config.properties"));
                } else {
                    System.out.println("Error: couldn't create properties file.");
                }
            } catch(IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static String get(String s) {
        return settings.getProperty(s);
    }

}
