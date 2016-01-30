package co.valdeon.titan.backupsystem.server;

import co.valdeon.titan.backupsystem.server.settings.Settings;
import co.valdeon.titan.backupsystem.server.socket.Socket;

import java.io.IOException;
import java.util.Properties;

public class Server {

    public static void main(String[] args) {
        new Settings();
        try {
            new Socket().open(Integer.parseInt(Settings.get("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
