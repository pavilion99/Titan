package co.valdeon.titan.backupsystem.client.gui;

import co.valdeon.titan.backupsystem.client.socket.Socket;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainGUI implements ActionListener {
    private JPanel panel1;
    private JTree tree1;
    private JButton quitButton;
    private JButton loginButton;
    private JLabel loggedLabel;
    private JLabel backupLabel;
    private JLabel backupSystemText;
    private JLabel statusLabel;
    private JFileChooser fileChooser;
    public Socket s;

    public MainGUI() {
        loginButton.addActionListener(this);
    }

    public void make() {
        JFrame frame = new JFrame("MainGUI");
        frame.setContentPane(new MainGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        onClickLogin();
    }

    public void onClickLogin() {
        System.out.println("Opening LoginGUI");
        LoginGUI d = new LoginGUI();
        d.make();
    }

    public void populateTree(List<String> info) {
        MutableTreeNode root = new DefaultMutableTreeNode(info.get(0), true);
    }

    public void setLoggedText(String s, boolean good) {
        this.loggedLabel.setText(s);
        if (good) {
            this.loggedLabel.setForeground(new Color(0, 255, 0));
        } else {
            this.loggedLabel.setForeground(new Color(255, 0, 0));
        }
    }
}
