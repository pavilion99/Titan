package co.valdeon.titan.backupsystem.server.auth;

import co.valdeon.titan.backupsystem.server.Server;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public class DBConnector {

    private Connection con;

    private HashMap<String, String> dirs = new HashMap<>();

    public DBConnector(String host, int port, String username, String password, String database) {
        String curl = "jdbc:mysql://" + host + ":" + Integer.toString(port) + "/" + database;
        try {
            this.con = DriverManager.getConnection(curl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            con = null;
        }
    }

    public boolean login(String username, String password) throws SQLException {
        Statement st = this.con.createStatement();
        ResultSet res = st.executeQuery("SELECT * FROM `" + Server.settings.getProperty("table") + "` WHERE `username`='" + username + "' AND `password`='" + password  + "'");
        if(res.next()) {
            if(res.getString("username").equals(username) && res.getString("password").equals(password)) {
                System.out.println("Logged as " + res.getString("username"));
                this.dirs.put(username, res.getString("backupdir"));
                return true;
            }
        }
        return false;
    }

    public File getBackupDirectory(String username) {
        return dirs.get(username) != null ? new File(dirs.get(username)) : null;
    }

}
