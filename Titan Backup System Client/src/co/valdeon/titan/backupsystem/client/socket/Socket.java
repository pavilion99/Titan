package co.valdeon.titan.backupsystem.client.socket;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Socket {

    private String password;

    java.net.Socket connect;

    PrintWriter out;
    OutputStream bin;

    BufferedReader in;
    InputStream inBin;

    public List<String> currentValue = new ArrayList<>();

    boolean error = false;
    String errorMessage = "";

    public Socket(String host, int port) {
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
    }

    public List<String> run(String... too) {
        try {
            String from = in.readLine();
            switch(from) {
                case "USR":
                    System.out.println("Submitting username...");
                    out.println(too[0]);
                    return run(too[1]);
                case "PSS":
                    System.out.println("Submitting password...");
                    String password = too[0];
                    try {
                        System.out.println(password);
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(password.getBytes());
                        byte digest[] = md.digest();
                        password = "";
                        for(byte b : digest) {
                            password += String.format("%02x", b & 0xff);
                        }
                        System.out.println(password);
                    } catch (NoSuchAlgorithmException e) {
                        this.error = true;
                        this.errorMessage = e.getMessage();
                        return null;
                    }
                    out.println(password);
                    this.currentValue.clear();
                    this.currentValue.add(in.readLine());
                    return this.currentValue;
                case "DIR":
                    // Acknowledge Directory request
                    out.println("GO");

                    // Fetch Directory name
                    currentValue.add(in.readLine());

                    // Fetch Directory Contents
                    while(true){
                        String ln = in.readLine();
                        currentValue.add(ln);
                        if(ln.equals("DONE"))
                            break;
                    }
                    break;
                case "SOD":
                    break;
                case "FILE":
                    /* String baseName = in.readLine();
                    System.out.println("Downloading file " + baseName);
                    ArrayList<Byte> file = new ArrayList<>();
                    byte[] a = new byte[Integer.parseInt(in.readLine())];
                    int written = connect.getInputStream().read(a);
                    BasicBinaryEncryptor bbe = new BasicBinaryEncryptor();
                    bbe.setPassword(password);
                    byte[] decrypt = bbe.decrypt(a);
                    FileOutputStream fos = new FileOutputStream(directory + "\\" + baseName);
                    new File(directory + "\\" + baseName).mkdirs();
                    fos.write(decrypt);
                    fos.close();
                    break; */
                case "FILE DONE":
                    System.out.println("File successfully downloaded.");
                    out.println("ACK");
                    break;
                case "BAD":
                    System.out.println("Username or password incorrect.");
                    out.println("ACK");
                    break;
                case "OK":
                    out.println("ACK");
                    break;
            }
        } catch(IOException e) {
            System.out.println("Something went wrong...");
            e.printStackTrace();
        }
        return currentValue;
    }

    public boolean hasError() {
        return this.error;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}