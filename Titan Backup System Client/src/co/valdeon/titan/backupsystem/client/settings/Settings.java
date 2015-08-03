package co.valdeon.titan.backupsystem.client.settings;

import java.io.IOException;
import java.util.Properties;

public class Settings {

    private Properties settings = new Properties();

    public Settings() {
        try {
            settings.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        }catch(IOException e){

        }
    }

    public Properties getProperties() {
        return this.settings;
    }

}
