/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.AddForm;

import Model.ClassModel;
import Services.Services;
import View.Main.AddForm.AddClassForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public class AddClassController {

    DefaultTableModel table;
    AddClassForm addClassForm;

    public AddClassController(DefaultTableModel table) {
        this.table = table;
        addClassForm = new AddClassForm();
        addFormButtonActionListener();
        String[] listMajors = Services.getMajorsList();
        String[] listCourse = Services.getCourseList("");
        addClassForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));
        addClassForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
    }

    void addFormButtonActionListener() {
        Services.addActionListener(addClassForm.majorsComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] listCourse = null;
                if (addClassForm.majorsComboBox.getSelectedIndex() == 0) {
                    listCourse = Services.getCourseList("");
                } else {
                    listCourse = Services.getCourseList(addClassForm.majorsComboBox.getSelectedItem().toString());
                }
                addClassForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
            }
        });

        Services.addActionListener(addClassForm.courseComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addClassForm.courseComboBox.getSelectedIndex() != 0 && addClassForm.majorsComboBox.getSelectedIndex() == 0) {
                    String courseName = addClassForm.courseComboBox.getSelectedItem().toString();
                    addClassForm.majorsComboBox.setSelectedItem(Services.setSelectedMajors("", addClassForm.courseComboBox.getSelectedItem().toString()));
                    addClassForm.courseComboBox.setSelectedItem(courseName);
                }
            }
        });

        Services.addActionListener(addClassForm.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addClassForm.courseComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn chưa chọn khóa!");
                } else if (addClassForm.classNameText.getText().equals("")) {
                    Services.showMess("Tên lớp không được để trống!");
                } else {
                    ClassModel classModel = new ClassModel(addClassForm.classNameText.getText(),
                            addClassForm.classDescriptionText.getText(), addClassForm.courseComboBox.getSelectedItem().toString());
                    String courseId = Services.getCourseId(classModel.course);
                    Statement stm = null;
                    ResultSet rs = null;
                    String[] columnsName = {"name", "description", "course"};
                    String[] values = {Services.toSQLString(classModel.name, true), Services.toSQLString(classModel.description, true), Services.toSQLString(courseId)};
                    if (Services.insertIntoDatabase("Classes", columnsName, values)) {
                        Services.showMess("Thêm thành công!");
                        Services.getClass(table);
                        addClassForm.dispose();
                    }

                }
            }
        });
        Services.addActionListener(addClassForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addClassForm.dispose();
            }
        });
    }
}
