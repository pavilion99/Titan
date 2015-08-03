package co.valdeon.titan.backupsystem.client;

import co.valdeon.titan.backupsystem.client.gui.MainGUI;
import co.valdeon.titan.backupsystem.client.settings.Settings;
import co.valdeon.titan.backupsystem.client.socket.Socket;

import java.util.Properties;

public class Client {

    public static Properties settings;

    public static void main(String[] args) {
        settings = new Settings().getProperties();
        MainGUI mainGUI = new MainGUI();
        new Socket(settings.getProperty("host"), Integer.parseInt(settings.getProperty("port"))).start();
    }

}
