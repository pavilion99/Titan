package co.valdeon.titan.backupsystem.server.socket;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

public class Socket {

    private ServerSocket titanServer;

    public void open(int port) throws IOException {
        this.titanServer = new ServerSocket(port);
        System.out.println("Bound on port " + Integer.toString(port));
        System.out.println("Waiting for clients...");
        while( true ) {
            new RequestProcessor(this.titanServer.accept()).start();
        }
    }

}
