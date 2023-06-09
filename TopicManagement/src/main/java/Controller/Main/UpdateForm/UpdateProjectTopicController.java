/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.UpdateForm;

import Controller.Main.AddForm.AddProjectTopicController;
import Model.Auth.User;
import Model.CourseModel;
import Model.MajorsModel;
import Model.ProjectTopicModel;
import Model.SchoolYearModel;
import Model.StudentModel;
import Model.TeacherModel;
import Services.Services;
import Services.SolrServices;
import Services.VietnameseAnalyzerServices;
import View.Main.UpdateForm.UpdateProjectTopicForm;
import View.Main.UpdateForm.UpdateProjectTopicFormStep1;
import View.Main.UpdateForm.UpdateProjectTopicFormStep2;
import View.Main.UpdateForm.UpdateProjectTopicFormStep3;
import View.Main.UpdateForm.UpdateStudentForm;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class UpdateProjectTopicController {

    UpdateProjectTopicForm updateProjectTopicForm;
    DefaultTableModel table;
    ProjectTopicModel projectTopicModel;

    UpdateProjectTopicFormStep1 step1 = new UpdateProjectTopicFormStep1();
    UpdateProjectTopicFormStep2 step2 = new UpdateProjectTopicFormStep2();
    UpdateProjectTopicFormStep3 step3 = new UpdateProjectTopicFormStep3();
    DefaultTableModel teacherTable = (DefaultTableModel) step2.teacherTable.getModel();
    JPanel currentPanel;

    public UpdateProjectTopicController(DefaultTableModel table, ProjectTopicModel projectTopicModel) {
        this.table = table;
        this.projectTopicModel = projectTopicModel;
        updateProjectTopicForm = new UpdateProjectTopicForm();
        initialFormValue();
        step1ButtonActionListener();
        step2ButtonActionListener();
        step3ButtonActionListener();
        updateProjectTopicForm.mainPanel.setLayout(new BorderLayout());
        updateProjectTopicForm.mainPanel.add(step1, BorderLayout.CENTER);
        updateProjectTopicForm.mainPanel.add(step1);
        currentPanel = step1;
        step1.setVisible(true);
    }

    private void initialFormValue() {
        String[] listMajors = Services.getMajorsList();
        String[] listCourse = Services.getCourseList("");
        String[] listSchoolYear = Services.getSchoolYearList();

        step1.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors)
        );
        step1.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse)
        );
        step1.schoolYearComboBox.setModel(new DefaultComboBoxModel<>(listSchoolYear)
        );

        Services.addActionListener(step1.majorsComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        Services.addActionListener(step1.courseComboBox, new ActionListener() {
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

        step1.id.setText(projectTopicModel.id);
        step1.name.setText(projectTopicModel.name);
        step1.description.setText(projectTopicModel.description);
        step1.require.setText(projectTopicModel.require);
        step1.courseComboBox.setSelectedItem(projectTopicModel.course.name);
        step1.schoolYearComboBox.setSelectedItem(projectTopicModel.schoolYear.name);
        step2.teacherInforText.setText(projectTopicModel.teacher.academicRank + " " + projectTopicModel.teacher.name
                + " Khoa: " + projectTopicModel.teacher.majors);
    }

    private void step1ButtonActionListener() {
        Services.addActionListener(step1.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProjectTopicForm.dispose();
            }
        });
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
                    updateProjectTopicForm.mainPanel.add(step2, BorderLayout.CENTER);
                    step2.setVisible(true);
                    currentPanel = step2;
                    Services.getTeacher(teacherTable);
                }
            }
        });
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
                updateProjectTopicForm.mainPanel.add(step1, BorderLayout.CENTER);
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
                    updateProjectTopicForm.mainPanel.add(step3, BorderLayout.CENTER);
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
                updateProjectTopicForm.mainPanel.add(step2, BorderLayout.CENTER);
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
                String[] columnsName = {
                    "id",
                    "name",
                    "description",
                    "require",
                    "teacher",
                    "course",
                    "schoolYear",
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
                    Services.toSQLString(User.id),
                    Services.toSQLString(Services.getCurrentTime())
                };

                if(Services.updateData("ProjectTopics", columnsName, values, "id="+Services.toSQLString(projectTopicModel.id))){
                    Services.showMess("Cập nhật thành công!");
                    Services.getNewProjectTopic(table);
                    updateProjectTopicForm.dispose();
                }
            }
        });
    }

}
