package co.valdeon.titan.backupsystem.client.gui;

import co.valdeon.titan.backupsystem.client.Client;
import co.valdeon.titan.backupsystem.client.socket.Socket;

import javax.swing.*;
import java.awt.event.*;

public class LoginGUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;

    public LoginGUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        MainGUI.s = new Socket(Client.settings.getProperty("host"), Integer.parseInt(Client.settings.getProperty("port")), usernameTextField.getText(), new String(passwordPasswordField.getPassword()));
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        LoginGUI dialog = new LoginGUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
