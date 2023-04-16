/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Services.ConnectDatabase;
import Services.ConnectSolr;
import Services.Services;
import View.ConnectSolrView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author BVKieu
 */
public class ConnectSolrController {
    ConnectSolrView view = new ConnectSolrView();

    public ConnectSolrController() {
        view.solrUrlText.setText("http://localhost:8983/solr/tika");
        view.setVisible(true);
        connectButtonActionListener();
    }
    private void connectButtonActionListener() {
        Services.addActionListener(view.connectButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String solrUrl = view.solrUrlText.getText(); 
                try {
                    if( ConnectSolr.connectSolr(solrUrl)){
                        view.dispose();
                        new AuthController();
                    }
                } catch (SolrServerException | IOException ex) {
                    Services.showMess("Không thể kết nối với Solr. Vui lòng kiểm tra lại.");
                    Logger.getLogger(ConnectSolrController.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        });
    }
    
}
