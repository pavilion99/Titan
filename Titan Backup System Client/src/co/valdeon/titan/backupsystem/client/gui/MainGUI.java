package co.valdeon.titan.backupsystem.client.gui;

import co.valdeon.titan.backupsystem.client.Client;
import co.valdeon.titan.backupsystem.client.socket.Socket;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.List;

public class MainGUI {
    private JPanel panel1;
    private JTree tree1;
    private JButton quitButton;
    private JButton loginButton;
    public static Socket s;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainGUI");
        frame.setContentPane(new MainGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void onClickLogin() {
        new LoginGUI();
    }

    public void populateTree(List<String> info) {
        MutableTreeNode root = new DefaultMutableTreeNode(info.get(0), true);
        tree1 = new JTree(root);
        
    }
}
