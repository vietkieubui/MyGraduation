/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.UpdateForm;

import Controller.Main.ReportSimilarController;
import Controller.Main.CalculateSimilarController;
import Model.DocumentModel;
import Model.ProjectTopicModel;
import Services.CheckSimilarServices;
import static Services.ConnectSolr.solrClient;
import Services.FileServices;
import Services.Services;
import Services.SolrServices;
import Services.VietnameseAnalyzerServices;
import View.Main.UpdateForm.SubmitDocumentForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.tika.exception.TikaException;

/**
 *
 * @author BVKieu
 */
public class SubmitDocumentController {

    SubmitDocumentForm submitDocumentForm;
    ProjectTopicModel projectTopicModel;
    DocumentModel documentModel;
    File file;

    public SubmitDocumentController(ProjectTopicModel projectTopicModel) {
        submitDocumentForm = new SubmitDocumentForm();
        this.projectTopicModel = projectTopicModel;
        initValues();
        submitDocumentFormButtonController();
    }

    private void submitDocumentFormButtonController() {
        Services.addActionListener(submitDocumentForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitDocumentForm.dispose();
            }
        });

        Services.addActionListener(submitDocumentForm.chooseFileButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file = FileServices.pdfFilePicker();
                submitDocumentForm.fileNameLabel.setText(file.getName());
                String text = null;
                try {
                    text = FileServices.fileToTextWithoutSpecialCharacter(file);
                } catch (IOException ex) {
                    Logger.getLogger(SubmitDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TikaException ex) {
                    Logger.getLogger(SubmitDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Services.addActionListener(submitDocumentForm.submitDocumentButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (submitDocumentForm.fileNameLabel.getText().equals("")) {
                    Services.showMess("Bạn chưa chọn file");
                    return;
                }
                SolrInputDocument document = new SolrInputDocument();
                String documentType = String.valueOf(submitDocumentForm.documentType.getSelectedIndex());
                String documentId = submitDocumentForm.documentId.getText();
                String createdAt = Services.getCurrentTime();
                document.addField("type", documentType);
                document.addField("id", documentId);
                document.addField("createdAt", createdAt);
                String text = null;
                try {
                    text = FileServices.fileToTextWithoutSpecialCharacter(file);
                } catch (IOException | TikaException ex) {
                    Logger.getLogger(SubmitDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                String[] strings;
                try {
                    strings = VietnameseAnalyzerServices.textToStrings(text);
                    for (String st : strings) {
                        document.addField("content", st);
                    }
                    String path = null;
                    if (submitDocumentForm.documentType.getSelectedIndex() == 0) {
                        if (FileServices.saveSummaryDocument(file)) {
                            path = "documents/summary/" + file.getName();
                        } else {
                            Services.showMess("Có lỗi xảy ra");
                            return;
                        }
                    } else if (submitDocumentForm.documentType.getSelectedIndex() == 1) {
                        if (FileServices.saveFinalDocument(file)) {
                            path = "documents/final/" + file.getName();

                        } else {
                            Services.showMess("Có lỗi xảy ra");
                            return;
                        }
                    }
                    SolrServices.indexToSolr(document);
                    if (Services.checkDocumentExist(documentModel.id)) {
                        String[] columnsName = {"topic", "path", "type", "createdAt"};
                        String[] values = {
                            Services.toSQLString(projectTopicModel.id),
                            Services.toSQLString(path),
                            Services.toSQLString(documentType),
                            Services.toSQLString(createdAt)
                        };
                        if (Services.updateData("Documents", columnsName, values, "id=" + Services.toSQLString(documentId))) {
                            Services.showMess("Nộp tài liệu thành công");
                            submitDocumentForm.dispose();
                        } else {
                            Services.showMess("Có lỗi xảy ra");
                        }

                    } else {
                        String[] columnsName = {"id", "topic", "path", "type", "createdAt"};
                        String[] values = {
                            Services.toSQLString(documentId),
                            Services.toSQLString(projectTopicModel.id),
                            Services.toSQLString(path),
                            Services.toSQLString(documentType),
                            Services.toSQLString(createdAt)
                        };
                        if (Services.insertIntoDatabase("Documents", columnsName, values)) {
                            Services.showMess("Nộp tài liệu thành công");
                            submitDocumentForm.dispose();
                        } else {
                            Services.showMess("Có lỗi xảy ra");
                        }
                    }
                } catch (IOException | SolrServerException ex) {
                    Logger.getLogger(SubmitDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    Services.showMess("Có lỗi xảy ra");
                }
            }
        });

        Services.addActionListener(submitDocumentForm.checkSimilarButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = null;
                if (!Services.checkDocumentExist(submitDocumentForm.documentId.getText())) {
                    Services.showMess("Tài liệu chưa được nộp. Vui lòng nộp tài liệu trước khi thực hiện chức năng này.");
                    return;
                }
                String[] textStrings = null;
                String[] ids = null;
                String currentDocumentId = submitDocumentForm.documentId.getText();
                SolrQuery solrQuery = new SolrQuery();
                solrQuery.set(CommonParams.Q, "id:" + currentDocumentId);

                QueryResponse response = null;
                try {
                    response = solrClient.query(solrQuery);
                    SolrDocumentList results = response.getResults();
                    for (SolrDocument doc : results) {
                        ArrayList<String> contentArray = (ArrayList<String>) doc.getFieldValue("content");
                        textStrings = contentArray.toArray(new String[contentArray.size()]);
                    }
                } catch (SolrServerException | IOException ex) {
                    Logger.getLogger(SubmitDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }

                int documentType = submitDocumentForm.documentType.getSelectedIndex();
                if (documentType == 0) {
                    ids = Services.getSummaryDocumentIdArray();
                } else if (documentType == 1) {
                    ids = Services.getFinalDocumentIdArray();
                }

                new CalculateSimilarController(currentDocumentId, textStrings, ids);
            }
        });

        Services.addActionListener(submitDocumentForm.viewReport, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Services.checkExistSimilarPercent(documentModel.id)) {
                    new ReportSimilarController(documentModel.id);
                } else {
                    Services.showMess("Không có báo cáo về tại liệu này, vui lòng bấm Tính trùng lặp rồi lưu lại");
                }
            }
        });

    }

    private void initValues() {
        Random random = new Random();
        String randomString = String.valueOf(random.nextInt(999999));
        documentModel = Services.getDocumentModel(projectTopicModel.id, "0");
        if (documentModel.id == null) {
            submitDocumentForm.documentId.setText(randomString);
            file = null;
            submitDocumentForm.fileNameLabel.setText("");
        } else {
            submitDocumentForm.documentId.setText(documentModel.id);
            if (Services.checkDocumentExist(documentModel.id)) {
                file = new File(documentModel.path);
                submitDocumentForm.fileNameLabel.setText(file.getName());
            }
        }

        Services.addActionListener(submitDocumentForm.documentType, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (submitDocumentForm.documentType.getSelectedIndex() == 0) {
                    documentModel = Services.getDocumentModel(projectTopicModel.id, "0");
                    if (documentModel.id == null) {
                        submitDocumentForm.documentId.setText(randomString);
                        file = null;
                        submitDocumentForm.fileNameLabel.setText("");
                    } else {
                        submitDocumentForm.documentId.setText(documentModel.id);
                        if (Services.checkDocumentExist(documentModel.id)) {
                            file = new File(documentModel.path);
                            submitDocumentForm.fileNameLabel.setText(file.getName());
                        }
                    }
                } else if (submitDocumentForm.documentType.getSelectedIndex() == 1) {
                    documentModel = Services.getDocumentModel(projectTopicModel.id, "1");
                    if (documentModel.id == null) {
                        submitDocumentForm.documentId.setText(randomString);
                        file = null;
                        submitDocumentForm.fileNameLabel.setText("");
                    } else {
                        submitDocumentForm.documentId.setText(documentModel.id);
                        if (Services.checkDocumentExist(documentModel.id)) {
                            file = new File(documentModel.path);
                            submitDocumentForm.fileNameLabel.setText(file.getName());
                        }
                    }
                }
            }
        });

        submitDocumentForm.topicInfor.setText(projectTopicModel.id + " - " + projectTopicModel.name);
        submitDocumentForm.studentInfo.setText(projectTopicModel.student.id + " - " + projectTopicModel.student.name);
    }
}
