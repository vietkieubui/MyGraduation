/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main;

import Controller.Main.UpdateForm.SubmitDocumentController;
import Model.DocumentModel;
import Model.ProjectTopicModel;
import Model.ReportSimilarModel;
import Services.CheckSimilarServices;
import static Services.ConnectSolr.solrClient;
import Services.Services;
import View.Main.CalculateSimilarForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;

/**
 *
 * @author BVKieu
 */
public class CalculateSimilarController {

    String[] text;
    String[] ids;
    String currentDocumentId;
    ArrayList<ReportSimilarModel> tableData = new ArrayList<>();

    CalculateSimilarForm calculateSimilarForm = new CalculateSimilarForm();
    DefaultTableModel calculateTable = (DefaultTableModel) calculateSimilarForm.calculateTable.getModel();

    public CalculateSimilarController(String currentDocumentId, String[] text, String[] ids) {
        this.text = text;
        this.ids = ids;
        this.currentDocumentId = currentDocumentId;

        calculateTable.setRowCount(0);
        calculate();

        
        Services.addActionListener(calculateSimilarForm.saveButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ReportSimilarModel data : tableData) {
                    String[] columnsName = {"document1", "document2", "similarPercent"};
                    String[] values = {
                        Services.toSQLString(data.document1),
                        Services.toSQLString(data.document2),
                        Services.toSQLString(String.valueOf(data.similarPercent))
                    };
                    if (!Services.insertIntoDatabase("Similar", columnsName, values)) {
                        Services.showMess("Có lỗi xảy ra");
                    }
                }
                Services.showMess("Xong");
            }
        });

    }
    
    private void calculate(){
        SolrQuery solrQuery = new SolrQuery();
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Print the current date and time
        System.out.println("Current Date and Time: " + currentDateTime);
        

        for (String id : ids) {
            if (id == null ? currentDocumentId == null : id.equals(currentDocumentId)) {
                continue;
            }
            DocumentModel documentModel = Services.getDocumentModelById(id);
            ProjectTopicModel projectTopicModel = Services.getProjectTopicInfo(documentModel.topic);
            solrQuery.set(CommonParams.Q, "id:" + id);

            // Thực hiện truy vấn
            QueryResponse response;
            try {
                response = solrClient.query(solrQuery);
                LocalDateTime queryTime = LocalDateTime.now();
                System.out.println("QueryTime: " + Duration.between(currentDateTime, queryTime));
                SolrDocumentList results = response.getResults();
                for (SolrDocument doc : results) {
                    ArrayList<String> contentArray = (ArrayList<String>) doc.getFieldValue("content");
                    String[] contentStrings = contentArray.toArray(new String[contentArray.size()]);
                    double result = CheckSimilarServices.calculateSimilar(text, contentStrings) * 100;
                    LocalDateTime calculateTime = LocalDateTime.now();
                    System.out.println("CalculateTime: " + Duration.between(currentDateTime, calculateTime));
                    calculateTable.addRow(new Object[]{id, documentModel.topic, documentModel.type, documentModel.path, result});
                    ReportSimilarModel reportSimilarModel = new ReportSimilarModel();
                    reportSimilarModel.document1 = currentDocumentId;
                    reportSimilarModel.document2 = id;
                    reportSimilarModel.similarPercent = result;
                    tableData.add(reportSimilarModel);
                }
            } catch (SolrServerException | IOException ex) {
                Logger.getLogger(SubmitDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                Services.showMess("Có lỗi xảy ra");
                return;
            }

        }
        Services.showMess("Xong");
        
    }

}
