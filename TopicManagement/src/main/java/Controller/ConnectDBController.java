/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Services.ConnectDatabase;
import Services.Services;
import View.ConnectDBView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author BVKieu
 */
public class ConnectDBController {

    ConnectDBView view = new ConnectDBView();
    

    public ConnectDBController() {
        view.ipAddressText.setText(" 192.168.62.103");
        view.usernameText.setText("sa");
        view.passwordText.setText("123");
        loginButtonActionListener();
    }

    private void loginButtonActionListener() {
        Services.addActionListener(view.connectButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ipAddress = view.ipAddressText.getText();
                String username = view.usernameText.getText();
                String password = view.passwordText.getText();
                var connectDB = new ConnectDatabase();
                if (connectDB.connectDatabase(ipAddress, username, password)) {
                    view.dispose();
                    new ConnectSolrController();
                }else{
                    Services.showMess("Không thể kết nối SQL Server");
                }

            }
        });
    }

}
