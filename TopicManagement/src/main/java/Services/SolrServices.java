/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import static Services.ConnectSolr.solrClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;

/**
 *
 * @author BVKieu
 */
public final class SolrServices {

    public static String[] getAllDocumentId() throws SolrServerException, IOException {
        String[] ids = null;
        ArrayList<String> list = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set(CommonParams.Q, "*:*");
        QueryResponse response = solrClient.query(solrQuery);
        SolrDocumentList results = response.getResults();
        for (SolrDocument doc : results) {
            String similarDocId = (String) doc.getFieldValue("id");
            list.add(similarDocId);
            ids = list.toArray(new String[list.size()]);
        }
        return ids;
    }

    public static String[] getAllSummaryDocumentId() throws SolrServerException, IOException {
        String[] ids = null;
        ArrayList<String> list = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set(CommonParams.Q, "type:0");
        QueryResponse response = solrClient.query(solrQuery);
        SolrDocumentList results = response.getResults();
        for (SolrDocument doc : results) {
            String similarDocId = (String) doc.getFieldValue("id");
            list.add(similarDocId);
            ids = list.toArray(new String[list.size()]);
        }
        return ids;
    }

    public static String[] getAllFinalDocumentId() throws SolrServerException, IOException {
        String[] ids = null;
        ArrayList<String> list = new ArrayList<String>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set(CommonParams.Q, "type:1");
        QueryResponse response = solrClient.query(solrQuery);
        SolrDocumentList results = response.getResults();
        for (SolrDocument doc : results) {
            String similarDocId = (String) doc.getFieldValue("id");
            list.add(similarDocId);
            ids = list.toArray(new String[list.size()]);
        }
        return ids;
    }

    public static boolean indexToSolr(SolrInputDocument document) throws SolrServerException, IOException {
        solrClient.add(document);
        solrClient.commit();
        return true;
    }

    public static boolean deleteDocument(String documentId) throws SolrServerException, IOException {
        UpdateResponse response = solrClient.deleteById(documentId);
        System.out.println("Delete status: " + response.getStatus());
        solrClient.commit();
        return true;
    }

}
