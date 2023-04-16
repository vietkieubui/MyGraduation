/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 *
 * @author BVKieu
 */
public class ConnectSolr {

    public static SolrClient solrClient;

    public ConnectSolr() {
    }

    public static SolrClient getSolrConnection() {
        return solrClient;
    }

    public static boolean connectSolr(String baseSolrUrl) throws SolrServerException, IOException {
        solrClient = new HttpSolrClient.Builder(baseSolrUrl).build();
        SolrServices.getAllDocumentId();
        return true;

    }
}
