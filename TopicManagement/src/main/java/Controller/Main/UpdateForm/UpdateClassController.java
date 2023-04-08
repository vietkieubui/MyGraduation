package Controller.Main.UpdateForm;

import Model.ClassModel;
import Services.Services;
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
public class UpdateClassController {

    UpdateClassForm updateClassForm;
    DefaultTableModel table;
    ClassModel classModel;

    public UpdateClassController(DefaultTableModel table, ClassModel classModel) {
        this.table = table;
        this.classModel = classModel;

        updateClassForm = new UpdateClassForm();
        updateFormButtonController();
        updateClassForm.idText.setText(classModel.id);
        updateClassForm.nameText.setText(classModel.name);
        updateClassForm.descriptionText.setText(classModel.description);
        String[] listMajors = Services.getMajorsList();
        updateClassForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));
        String[] listCourse = Services.getCourseList("");
        updateClassForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
        updateClassForm.courseComboBox.setSelectedItem(classModel.course);
    }

    private void updateFormButtonController() {
        Services.addActionListener(updateClassForm.majorsComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] listCourse = null;
                if (updateClassForm.majorsComboBox.getSelectedIndex() == 0) {
                    listCourse = Services.getCourseList("");
                } else {
                    listCourse = Services.getCourseList(updateClassForm.majorsComboBox.getSelectedItem().toString());
                }
                updateClassForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
            }
        });
        Services.addActionListener(updateClassForm.courseComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (updateClassForm.courseComboBox.getSelectedIndex() != 0 && updateClassForm.majorsComboBox.getSelectedIndex() == 0) {
                    String courseName = updateClassForm.courseComboBox.getSelectedItem().toString();
                    updateClassForm.majorsComboBox.setSelectedItem(Services.setSelectedMajors("", updateClassForm.courseComboBox.getSelectedItem().toString()));
                    updateClassForm.courseComboBox.setSelectedItem(courseName);
                }
            }
        });
        Services.addActionListener(updateClassForm.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (updateClassForm.courseComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn chưa chọn khoá!");
                } else {
                    String courseId = Services.getCourseId(updateClassForm.courseComboBox.getSelectedItem().toString());
                    ClassModel classMd = new ClassModel(
                            updateClassForm.nameText.getText(),
                            updateClassForm.descriptionText.getText(),
                            courseId
                    );
                    classMd.id = updateClassForm.idText.getText();
                    String[] columnsName = {"name", "description", "course"};
                    String[] values = {
                        Services.toSQLString(classMd.name, true),
                        Services.toSQLString(classMd.description, true),                        
                        Services.toSQLString(classMd.course)};
                    try {
                        if (Services.updateData("Classes", columnsName, values, "id=" + Services.toSQLString(classMd.id))) {
                            Services.showMess("Cập nhật thành công!");
                            Services.getClass(table);
                            updateClassForm.dispose();
                        }
                    } catch (Exception ex) {
                        Services.showMess(ex.toString());
                    }
                }
            }
        });
        Services.addActionListener(updateClassForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClassForm.dispose();
            }
        });
    }

}
