/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.UpdateForm;

import Model.TeacherModel;
import Services.Services;
import View.Main.UpdateForm.UpdateTeacherForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public class UpdateTeacherController {

    UpdateTeacherForm updateTeacherForm;
    DefaultTableModel table;
    TeacherModel teacherModel;

    public UpdateTeacherController(DefaultTableModel table, TeacherModel teacherModel) {
        this.table = table;
        this.teacherModel = teacherModel;

        updateTeacherForm = new UpdateTeacherForm();
        updateFormButtonController();
        updateTeacherForm.idText.setText(teacherModel.id);
        updateTeacherForm.nameText.setText(teacherModel.name);
        updateTeacherForm.academicRank.setText(teacherModel.academicRank);
        String[] listMajors = Services.getMajorsList();
        updateTeacherForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));
        updateTeacherForm.majorsComboBox.setSelectedItem(teacherModel.majors);
        updateTeacherForm.phonenumberText.setText(teacherModel.phonenumber);
        updateTeacherForm.emailText.setText(teacherModel.email);
    }

    private void updateFormButtonController() {
        Services.addActionListener(updateTeacherForm.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (updateTeacherForm.majorsComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn chưa chọn khoa!");
                } else if (!Services.isValidPhoneNumber(updateTeacherForm.phonenumberText.getText())) {
                    Services.showMess("Số điện thoại không hợp lệ!");
                } else if (!Services.isValidEmail(updateTeacherForm.emailText.getText())) {
                    Services.showMess("Email không hợp lệ!");
                } else {
                    String majorsId = Services.getMajorsId(updateTeacherForm.majorsComboBox.getSelectedItem().toString());
                    TeacherModel teacher = new TeacherModel(
                            updateTeacherForm.nameText.getText(),
                            updateTeacherForm.academicRank.getText(),
                            majorsId,
                            updateTeacherForm.phonenumberText.getText(),
                            updateTeacherForm.emailText.getText()
                    );
                    teacher.id = updateTeacherForm.idText.getText();

                    String[] columnsName = {"name", "academicRank", "majors", "phonenumber", "email"};
                    String[] values = {
                        Services.toSQLString(teacher.name, true),
                        Services.toSQLString(teacher.academicRank, true),
                        Services.toSQLString(teacher.majors),
                        Services.toSQLString(teacher.phonenumber),
                        Services.toSQLString(teacher.email)
                    };
                    try {
                        if (Services.updateData("Teachers", columnsName, values, "id=" + Services.toSQLString(teacher.id))) {
                            Services.showMess("Cập nhật thành công!");
                            Services.getTeacher(table);
                            updateTeacherForm.dispose();
                        }
                    } catch (Exception ex) {
                        Services.showMess(ex.toString());
                    }

                }

            }
        }
        );
        Services.addActionListener(updateTeacherForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                updateTeacherForm.dispose();
            }
        }
        );
    }

}
