package Controller.Main.UpdateForm;

import Model.ClassModel;
import Services.Services;
import View.Main.UpdateForm.AssignTopicForm;
import View.Main.UpdateForm.UpdateClassForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
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
    ;
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
    }

    private void initValues() {
        Services.getNotAssignStudent(studentTable);
        assignTopicForm.id.setText(topicId);
        assignTopicForm.name.setText(topicName);
    }

}
