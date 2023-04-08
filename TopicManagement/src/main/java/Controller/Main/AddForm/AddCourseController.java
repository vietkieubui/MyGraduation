/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.AddForm;

import Model.CourseModel;
import Services.Services;
import View.Main.AddForm.AddCourseForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public class AddCourseController {

    DefaultTableModel table;
    AddCourseForm addCourseForm;

    public AddCourseController(DefaultTableModel table) {
        this.table = table;
        addCourseForm = new AddCourseForm();
        addFormButtonController();
        String[] listMajors = Services.getMajorsList();
        addCourseForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));
    }

    void addFormButtonController() {
        Services.addActionListener(addCourseForm.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addCourseForm.majorsComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn chưa chọn khoa!");
                } else {
                    CourseModel course = new CourseModel(addCourseForm.courseNameText.getText(), addCourseForm.courseDescriptionText.getText(), addCourseForm.studyTimeText.getText(), addCourseForm.majorsComboBox.getSelectedItem().toString());
                    String majorsId = Services.getMajorsId(course.majors);
                    String[] columnsName = {"name", "description", "studyTime", "majors"};
                    String[] values = {Services.toSQLString(course.name, true), Services.toSQLString(course.description, true), Services.toSQLString(course.studyTime), Services.toSQLString(majorsId)};
                    if (Services.insertIntoDatabase("Courses", columnsName, values)) {
                        Services.showMess("Thêm thành công!");
                        Services.getCourse(table);
                        addCourseForm.dispose();
                    }

                }
            }
        });
        Services.addActionListener(addCourseForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourseForm.dispose();
            }
        });
    }
}
