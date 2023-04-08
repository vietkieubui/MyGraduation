/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.UpdateForm;

import Model.CourseModel;
import Services.Services;
import View.Main.UpdateForm.UpdateCourseForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public class UpdateCourseController {

    UpdateCourseForm updateCourseForm;
    DefaultTableModel table;
    CourseModel courseModel;

    public UpdateCourseController(DefaultTableModel table, CourseModel courseModel) {
        this.courseModel = courseModel;
        this.table = table;
        updateCourseForm = new UpdateCourseForm();
        updateCourseForm.idText.setText(courseModel.id);
        updateCourseForm.nameText.setText(courseModel.name);
        updateCourseForm.studyTimeText.setText(courseModel.studyTime);
        updateCourseForm.descriptionText.setText(courseModel.description);
        String[] listMajors = Services.getMajorsList();
        updateCourseForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));
        updateCourseForm.majorsComboBox.setSelectedItem(courseModel.majors);
        updateFormButtonController();
    }

    private void updateFormButtonController() {
        Services.addActionListener(updateCourseForm.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (updateCourseForm.majorsComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn chưa chọn khoa!");
                } else {
                    String majorsId = Services.getMajorsId(updateCourseForm.majorsComboBox.getSelectedItem().toString());
                    CourseModel course = new CourseModel(
                            updateCourseForm.nameText.getText(),
                            updateCourseForm.descriptionText.getText(),
                            updateCourseForm.studyTimeText.getText(),
                            majorsId
                    );
                    course.id = updateCourseForm.idText.getText();
                    String[] columnsName = {"name", "description", "studyTime", "majors"};
                    String[] values = {
                        Services.toSQLString(course.name, true),
                        Services.toSQLString(course.description, true),
                        Services.toSQLString(course.studyTime),
                        Services.toSQLString(course.majors),};
                    try {
                        if (Services.updateData("Courses", columnsName, values, "id=" + Services.toSQLString(course.id))) {
                            Services.showMess("Cập nhật thành công!");
                            Services.getCourse(table);
                            updateCourseForm.dispose();
                        }

                    } catch (Exception ex) {
                        Services.showMess(ex.toString());
                    }

                }
            }
        });
        Services.addActionListener(updateCourseForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCourseForm.dispose();
            }
        });

    }

}
