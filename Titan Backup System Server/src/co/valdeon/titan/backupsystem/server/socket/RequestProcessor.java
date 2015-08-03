package co.valdeon.titan.backupsystem.server.socket;

import co.valdeon.titan.backupsystem.server.Server;
import co.valdeon.titan.backupsystem.server.auth.DBConnector;
import co.valdeon.titan.backupsystem.server.protocol.ConnectionStates;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestProcessor extends Thread {

    private java.net.Socket context;
    private ConnectionStates state = ConnectionStates.WAITING;
    private String username;
    private String password;
    private DBConnector dbc;

    private String currentRelDir = "";

    private boolean custom = false;
    private boolean downloadDisabled = false;

    private ArrayList<File> fileList = null;

    public RequestProcessor(java.net.Socket s) {
        super("RequestProcessorThread");
        this.context = s;
    }

    public void run() {
        try (
        PrintWriter out = new PrintWriter(this.context.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(context.getInputStream()));
        ) {
            System.out.println("Client connected from IP " + context.getRemoteSocketAddress());

            String input = null, output;

            do {
                System.out.println("Received " + input + " from client " + context.getRemoteSocketAddress());
                switch(state) {
                    case USERNAME_REREQUESTED:
                    case WAITING:
                        output = "USR";
                        switch (this.state) {
                            case USERNAME_REREQUESTED:
                                this.state = ConnectionStates.PASSWORD_REREQUESTED;
                                break;
                            case WAITING:
                                this.state = ConnectionStates.USERNAME_REQUESTED;
                                break;
                        }
                        break;
                    case PASSWORD_REREQUESTED:
                    case USERNAME_REQUESTED:
                        this.username = input;
                        output = "PSS";
                        switch (this.state) {
                            case PASSWORD_REREQUESTED:
                                this.state = ConnectionStates.RETRY;
                                break;
                            case USERNAME_REQUESTED:
                                this.state = ConnectionStates.PASSWORD_REQUESTED;
                                break;
                        }
                        break;
                    case RETRY:
                    case PASSWORD_REQUESTED:
                        this.password = input;
                        try {
                            this.dbc = new DBConnector(Server.settings.getProperty("host"), Integer.parseInt(Server.settings.getProperty("portDB")), Server.settings.getProperty("username"), Server.settings.getProperty("password"), Server.settings.getProperty("database"));
                            output = this.dbc.login(this.username, this.password) ? "OK" : "BAD";
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return;
                        }
                        switch (output) {
                            case "OK":
                                this.state = ConnectionStates.DIR;
                                break;
                            case "BAD":
                                this.state = ConnectionStates.USERNAME_REREQUESTED;
                                break;
                        }
                        break;
                    case DIR:
                        File a;
                        if (custom) {
                            System.out.println("Waiting for next directory...");
                            String nextDir = in.readLine();
                            if(nextDir.contains("\"")) {
                                downloadDisabled = true;
                                nextDir = nextDir.replace("\"", "");
                            }
                            if(!downloadDisabled) {
                                if(nextDir.toLowerCase().startsWith("download")) {
                                    File c = new File(dbc.getBackupDirectory(username).getAbsolutePath() + "\\" + currentRelDir + "\\" + nextDir.replace("download ", "") + ".enc");
                                    if(c.exists() && !c.isDirectory()) {
                                        out.println("FILE");
                                        System.out.println("Serving file " + c.getAbsolutePath().replaceAll("\\.enc$", ""));
                                        out.println(c.getName().replaceAll("\\.enc$", ""));
                                        out.println(c.length());
                                        byte[] file = Files.readAllBytes(c.toPath());
                                        this.context.getOutputStream().write(file);
                                        this.context.getOutputStream().flush();
                                        out.println("FILE DONE");
                                        output = "DIR";
                                        break;
                                    } else {
                                        System.out.println("File " + dbc.getBackupDirectory(username).getAbsolutePath() + "\\" + currentRelDir + "\\" + nextDir.replace("download ", "") + " does not exist or is directory.");
                                        output = ("BAD FILE");
                                        break;
                                    }
                                }
                            }
                            a = new File(dbc.getBackupDirectory(this.username).getAbsolutePath() + "\\" + currentRelDir + "\\" + nextDir);
                            currentRelDir += "\\" + nextDir;
                            currentRelDir = Paths.get(currentRelDir).normalize().toString();
                            while(! a.isDirectory()) {
                                System.out.println("Error! " + a.getAbsolutePath() + " is not a directory.");
                                System.out.println("Enter a directory name.");
                                a = new File(dbc.getBackupDirectory(this.username).getAbsolutePath() + in.readLine());
                            }
                        } else {
                            a = dbc.getBackupDirectory(this.username);
                        }
                        this.fileList = getAllFiles(a);
                        out.println("DIR");
                        out.println(currentRelDir.equals("") ? "\\" : currentRelDir);
                        System.out.println("Sent " + (currentRelDir.equals("") ? "\\" : currentRelDir) + " as main directory to client " + context.getRemoteSocketAddress());
                        for(File b : fileList) {

                            if(!b.isDirectory()) {
                                out.println(b.getAbsolutePath().replace(a.getAbsolutePath()+"\\", "").replaceAll("\\.enc$", "") + "?" + (b.isDirectory() ? "directory" : "file"));
                            } else {
                                out.println(b.getAbsolutePath().replace(a.getAbsolutePath()+"\\", "") + "?" + (b.isDirectory() ? "directory" : "file"));
                            }
                            System.out.println("Send message \"" + b.getAbsolutePath().replace(a.getAbsolutePath()+"\\", "") + "?" + (b.isDirectory() ? "directory" : "file") + " to client " + context.getRemoteSocketAddress());
                        }
                        System.out.println("Done reading files.");
                        if(!custom) {
                            custom = true;
                        }
                        out.println("DONE");
                        output = "SOD";
                        break;
                    case WAIT:
                        output = "SOD";
                        this.state = ConnectionStates.DIR;
                        break;
                    case DONE:
                        output = "USR";
                        break;
                    default:
                        output = "ERR";
                        break;
                }
                if(output != null) {
                    System.out.println("Sending message \"" + output + "\" to client " + context.getRemoteSocketAddress());
                    out.println(output);
                    if(output.equals("END"))
                        break;
                }
            } while((input = in.readLine()) != null);

            context.close();
            System.out.println("Client at " + context.getRemoteSocketAddress() + " disconnected.");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<File> getAllFiles(File f) {
        ArrayList<File> files =  new ArrayList<>();
        System.out.println("Adding files...");
        for(File b : f.listFiles()) {
            try {
                System.out.println("Added " + b.getCanonicalPath());
            }catch (IOException e) {
                e.printStackTrace();
            }
            files.add(b);
        }
        System.out.println("Complete.");
        return files;
    }

}
