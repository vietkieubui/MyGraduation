package Controller.Main.UpdateForm;

import Model.ClassModel;
import Model.StudentModel;
import Services.Services;
import View.Main.UpdateForm.AssignTopicForm;
import View.Main.UpdateForm.UpdateClassForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author BVKieu
 */
public class AssignTopicController {

    AssignTopicForm assignTopicForm = new AssignTopicForm();
    DefaultTableModel projectTopicTable;
    String topicId;
    String topicName;
    DefaultTableModel studentTable = (DefaultTableModel) assignTopicForm.studentTable.getModel();

    public AssignTopicController(DefaultTableModel projectTopicTable, String topicId, String topicName) {
        this.projectTopicTable = projectTopicTable;
        this.topicId = topicId;
        this.topicName = topicName;
        assignTopicFormButtonController();
        initValues();
    }

    private void assignTopicFormButtonController() {
        Services.addActionListener(assignTopicForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignTopicForm.dispose();
            }
        });
        Services.addActionListener(assignTopicForm.assignButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (assignTopicForm.studentInfo.getText().equals("")) {
                    Services.showMess("Bạn chưa chọn sinh viên");
                } else {
                    try {
                        int updateRow = assignTopicForm.studentTable.getSelectedRow();
                        String studentId = (String) studentTable.getValueAt(updateRow, 0);

                        String[] columnsName = {"student"};
                        String[] values = {Services.toSQLString(studentId)};
                        if (Services.updateData("ProjectTopics", columnsName, values, "id=" + Services.toSQLString(topicId))) {
                            Services.showMess("Giao đồ án thành công!");
                            Services.getNewProjectTopic(projectTopicTable);
                            assignTopicForm.dispose();
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.toString());
                        Services.showMess("Bạn phải chọn 1 hàng!");
                    }

                }
            }
        });
    }

    private void initValues() {
        Services.getNotAssignStudent(studentTable);
        assignTopicForm.id.setText(topicId);
        assignTopicForm.name.setText(topicName);
        ListSelectionModel selectionModel = assignTopicForm.studentTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = assignTopicForm.studentTable.getSelectedRow();
                    StudentModel studentModel = new StudentModel();
                    if (selectedRow >= 0) {
                        studentModel.id = (String) studentTable.getValueAt(selectedRow, 0);
                        studentModel.name = (String) studentTable.getValueAt(selectedRow, 1);

                        studentModel.phonenumber = (String) studentTable.getValueAt(selectedRow, 4);
                        studentModel.email = (String) studentTable.getValueAt(selectedRow, 5);
//                        projectTopicModel.teacher = teacherModel;
                        assignTopicForm.studentInfo.setText(studentModel.id + " - " + studentModel.name);
                    }
                }
            }
        });
    }

}
