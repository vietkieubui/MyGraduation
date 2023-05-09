/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.AddForm;

import Model.Auth.User;
import Model.CourseModel;
import Model.MajorsModel;
import Model.ProjectTopicModel;
import Model.SchoolYearModel;
import Model.TeacherModel;
import Services.Services;
import Services.SolrServices;
import Services.VietnameseAnalyzerServices;
import View.Main.AddForm.AddProjectTopicForm;
import View.Main.AddForm.AddProjectTopicFormStep1;
import View.Main.AddForm.AddProjectTopicFormStep2;
import View.Main.AddForm.AddProjectTopicFormStep3;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author BVKieu
 */
public class AddProjectTopicController {

    DefaultTableModel table;
    AddProjectTopicForm addProjectTopicForm;

    ProjectTopicModel projectTopicModel = new ProjectTopicModel();

    AddProjectTopicFormStep1 step1 = new AddProjectTopicFormStep1();
    AddProjectTopicFormStep2 step2 = new AddProjectTopicFormStep2();
    AddProjectTopicFormStep3 step3 = new AddProjectTopicFormStep3();
    DefaultTableModel teacherTable = (DefaultTableModel) step2.teacherTable.getModel();
    JPanel currentPanel;

    public AddProjectTopicController(DefaultTableModel table) {
        this.table = table;
        addProjectTopicForm = new AddProjectTopicForm();
        addProjectTopicForm.mainPanel.setLayout(new BorderLayout());
        addProjectTopicForm.mainPanel.add(step1, BorderLayout.CENTER);
        addProjectTopicForm.mainPanel.add(step1);
        step1.setVisible(true);
        currentPanel = step1;
        addProjectTopicForm.setVisible(true);
        step1ButtonActionListener();
        step2ButtonActionListener();
        step3ButtonActionListener();
        projectTopicModel.createdBy = User.id;
    }

    private void step1ButtonActionListener() {
        Services.addActionListener(step1.nextButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step1.id.getText().equals("")) {
                    Services.showMess("Mã đề tài không được để trống");
                } else if (step1.name.getText().equals("")) {
                    Services.showMess("Tên đề tài không được để trống");
                } else if (step1.courseComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn phải chọn 1 khóa");
                } else if (step1.majorsComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn phải chọn 1 ngành");
                } else if (step1.schoolYearComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn phải chọn 1 năm học");
                } else {
                    projectTopicModel.id = step1.id.getText();
                    projectTopicModel.name = step1.name.getText();
                    projectTopicModel.description = step1.description.getText();
                    projectTopicModel.require = step1.require.getText();
                    CourseModel course = new CourseModel();
                    course.id = Services.getCourseId(step1.courseComboBox.getSelectedItem().toString());
                    course.name = step1.courseComboBox.getSelectedItem().toString();
                    projectTopicModel.course = course;

                    MajorsModel majors = new MajorsModel();
                    majors.majorsId = Services.getMajorsId(step1.majorsComboBox.getSelectedItem().toString());
                    majors.name = step1.majorsComboBox.getSelectedItem().toString();
                    projectTopicModel.majors = majors;

                    SchoolYearModel schoolYear = new SchoolYearModel();
                    schoolYear.id = Services.getSchoolYearId(step1.schoolYearComboBox.getSelectedItem().toString());
                    schoolYear.name = step1.schoolYearComboBox.getSelectedItem().toString();
                    projectTopicModel.schoolYear = schoolYear;

                    currentPanel.setVisible(false);
                    addProjectTopicForm.mainPanel.add(step2, BorderLayout.CENTER);
                    step2.setVisible(true);
                    currentPanel = step2;
                    Services.getTeacher(teacherTable);
                }
            }
        }
        );
        Services.addActionListener(step1.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                addProjectTopicForm.dispose();
            }
        }
        );

        String[] listMajors = Services.getMajorsList();
        String[] listCourse = Services.getCourseList("");
        String[] listSchoolYear = Services.getSchoolYearList();

        step1.majorsComboBox.setModel(
                new DefaultComboBoxModel<>(listMajors)
        );
        step1.courseComboBox.setModel(
                new DefaultComboBoxModel<>(listCourse)
        );
        step1.schoolYearComboBox.setModel(
                new DefaultComboBoxModel<>(listSchoolYear)
        );

        Services.addActionListener(step1.majorsComboBox,
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                String[] listCourse = null;
                if (step1.majorsComboBox.getSelectedIndex() == 0) {
                    listCourse = Services.getCourseList("");
                } else {
                    listCourse = Services.getCourseList(step1.majorsComboBox.getSelectedItem().toString());
                }
                step1.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
            }
        }
        );

        Services.addActionListener(step1.courseComboBox,
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                if (step1.courseComboBox.getSelectedIndex() != 0 && step1.majorsComboBox.getSelectedIndex() == 0) {
                    String courseName = step1.courseComboBox.getSelectedItem().toString();
                    step1.majorsComboBox.setSelectedItem(Services.setSelectedMajors("", step1.courseComboBox.getSelectedItem().toString()));
                    step1.courseComboBox.setSelectedItem(courseName);
                }
            }
        }
        );
    }

    private void step2ButtonActionListener() {
        ListSelectionModel selectionModel = step2.teacherTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = step2.teacherTable.getSelectedRow();
                    TeacherModel teacherModel = new TeacherModel();
                    if (selectedRow >= 0) {
                        teacherModel.id = (String) teacherTable.getValueAt(selectedRow, 0);
                        teacherModel.name = (String) teacherTable.getValueAt(selectedRow, 1);
                        teacherModel.academicRank = (String) teacherTable.getValueAt(selectedRow, 2);
                        teacherModel.majors = (String) teacherTable.getValueAt(selectedRow, 3);
                        teacherModel.phonenumber = (String) teacherTable.getValueAt(selectedRow, 4);
                        teacherModel.email = (String) teacherTable.getValueAt(selectedRow, 5);
                        projectTopicModel.teacher = teacherModel;
                        step2.teacherInforText.setText(projectTopicModel.teacher.academicRank + " " + projectTopicModel.teacher.name
                                + " Khoa: " + projectTopicModel.teacher.majors);
                    }
                }
            }
        });

        Services.addActionListener(step2.backButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.setVisible(false);
                addProjectTopicForm.mainPanel.add(step1, BorderLayout.CENTER);
                step1.setVisible(true);
                currentPanel = step1;
            }
        });
        Services.addActionListener(step2.nextButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectTopicModel.teacher == null) {
                    Services.showMess("Bạn phải chọn 1 giảng viên");
                } else {
                    setStep3TextField();
                    currentPanel.setVisible(false);
                    addProjectTopicForm.mainPanel.add(step3, BorderLayout.CENTER);
                    step3.setVisible(true);
                    currentPanel = step3;
                }
            }
        });

    }

    private void setStep3TextField() {
        step3.id.setText(projectTopicModel.id);
        step3.name.setText(projectTopicModel.name);
        step3.description.setText(projectTopicModel.description);
        step3.require.setText(projectTopicModel.require);
        step3.course.setText(projectTopicModel.course.name);
        step3.majors.setText(projectTopicModel.majors.name);
        step3.schoolYear.setText(projectTopicModel.schoolYear.name);
        step3.teacher.setText(projectTopicModel.teacher.academicRank + " " + projectTopicModel.teacher.name
                + " Khoa: " + projectTopicModel.teacher.majors);
    }

    private void step3ButtonActionListener() {
        Services.addActionListener(step3.backButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.setVisible(false);
                addProjectTopicForm.mainPanel.add(step2, BorderLayout.CENTER);
                step2.setVisible(true);
                currentPanel = step2;
                Services.getTeacher(teacherTable);
            }
        });

        Services.addActionListener(step3.finishButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SolrInputDocument document = new SolrInputDocument();
                String createdAt = Services.getCurrentTime();
                document.addField("type", "2");
                document.addField("id", projectTopicModel.id);
                document.addField("createdAt", createdAt);

                String[] contents;
                try {
                    contents = VietnameseAnalyzerServices.textToStrings(projectTopicModel.name);
                    for (String content : contents) {
                        document.addField("content", content);
                    }
                    SolrServices.indexToSolr(document);
                } catch (IOException | SolrServerException ex) {
                    Logger.getLogger(AddProjectTopicController.class.getName()).log(Level.SEVERE, null, ex);
                    Services.showMess("Có lỗi xảy ra");
                    return;
                }

                //insert into SQL server
                String[] columnsName = {
                    "id",
                    "name",
                    "description",
                    "require",
                    "teacher",
                    "course",
                    "schoolYear",
                    "topicStatus",
                    "createdBy",
                    "createdAt"
                };
                String[] values = {
                    Services.toSQLString(projectTopicModel.id),
                    Services.toSQLString(projectTopicModel.name, true),
                    Services.toSQLString(projectTopicModel.description, true),
                    Services.toSQLString(projectTopicModel.require, true),
                    Services.toSQLString(projectTopicModel.teacher.id),
                    Services.toSQLString(projectTopicModel.course.id),
                    Services.toSQLString(projectTopicModel.schoolYear.id),
                    Services.toSQLString("0"),
                    Services.toSQLString(User.id),
                    Services.toSQLString(Services.getCurrentTime())
                };

                if (Services.insertIntoDatabase("ProjectTopics", columnsName, values)) {
                    Services.showMess("Thêm thành công");
                    addProjectTopicForm.dispose();
                    Services.getNewProjectTopic(table);
                } else {
                    Services.showMess("Có lỗi xảy ra");
                }

            }
        });
        
        Services.addActionListener(step3.checkSimilar, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

}
