package Controller.Main;


import Services.Services;
import View.Main.ReportSimilarForm;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author BVKieu
 */
public class ReportSimilarController {

    ReportSimilarForm reportSimilarForm = new ReportSimilarForm();
    String documentId;
    DefaultTableModel reportTable = (DefaultTableModel) reportSimilarForm.reportTable.getModel();

    public ReportSimilarController(String documentId) {
        this.documentId = documentId;
        initValues();
    }

    public void initValues() {
        reportSimilarForm.documentInfo.setText(documentId);
        Services.getReportSimilar(reportTable, documentId);
    }

}
