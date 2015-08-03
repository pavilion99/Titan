package co.valdeon.titan.backupsystem.client.socket;

import jdk.internal.util.xml.impl.Input;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Socket {

    private String[] args;
    private String host;
    private int port;

    private String password;

    java.net.Socket connect;

    PrintWriter out;
    OutputStream bin;

    BufferedReader in;
    InputStream inBin;

    public List<String> currentValue;

    public Socket(String host, int port, String... args) {
        try {
            connect = new java.net.Socket(host, port);
            bin = connect.getOutputStream();
            out = new PrintWriter(connect.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            inBin = connect.getInputStream();
        }catch (IOException e) {
            System.out.println("Something went wrong...");
            e.printStackTrace();
        }
        this.args = args;
    }

    public void run() {
        try {
            String from, to = null;
            while((from = in.readLine()) != null) {
                switch(from) {
                    case "USR":
                        to = args[0];
                        break;
                    case "PSS":
                        to = args[1];
                        break;
                    case "DIR":
                        String ln;
                        out.println("GO");
                        currentValue.add(in.readLine());
                        ln = in.readLine();
                        while(true){
                            currentValue.add(ln);
                            if(ln.equals("DONE"))
                                break;
                        }
                        break;
                    case "SOD":
                        System.out.println("Select a directory or download a file");
                        to = stdIn.readLine();
                        break;
                    case "FILE":
                        String baseName = in.readLine();
                        System.out.println("Downloading file " + baseName);
                        ArrayList<Byte> file = new ArrayList<>();
                        byte[] a = new byte[Integer.parseInt(in.readLine())];
                        int written = connect.getInputStream().read(a);
                        BasicBinaryEncryptor bbe = new BasicBinaryEncryptor();
                        bbe.setPassword(
                        byte[] decrypt = bbe.decrypt(a);
                        System.out.println("Type a directory in which to store the file");
                        String directory = stdIn.readLine();
                        FileOutputStream fos = new FileOutputStream(directory + "\\" + baseName);
                        new File(directory + "\\" + baseName).mkdirs();
                        fos.write(decrypt);
                        fos.close();
                        break;
                    case "FILE DONE":
                        System.out.println("File successfully downloaded.");
                        to = "ACK";
                        break;
                    case "BAD":
                        System.out.println("Username or password incorrect.");
                        to = "ACK";
                        break;
                    case "OK":
                        to = "ACK";
                        break;
                    default:
                        System.out.println("Warning! Unknown server message.");
                        System.out.println(from);
                        break;
                }
                if(to != null) {
                    out.println(to);
                    to = null;
                }
            }

            System.out.println("Disconnecting.");
        } catch(IOException e) {
            System.out.println("Something went wrong...");
            e.printStackTrace();
        }
    }

    public void send(String s) {
        out.
    }

    private static class Eraser extends Thread {
        private boolean running = true;
        public void run() {
            while (running) {
                System.out.print("\b ");
                try {
                    Thread.currentThread().sleep(1);
                }
                catch(InterruptedException e) {
                    break;
                }
            }
        }
        public synchronized void halt() {
            running = false;
        }
    }
}