package co.valdeon.titan.backupsystem.server;

import co.valdeon.titan.backupsystem.server.settings.Settings;
import co.valdeon.titan.backupsystem.server.socket.Socket;

import java.io.IOException;
import java.util.Properties;

public class Server {

    public static Properties settings;

    public static void main(String[] args) {
        settings = new Settings().getProperties();
        try {
            new Socket().open(Integer.parseInt(settings.getProperty("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
