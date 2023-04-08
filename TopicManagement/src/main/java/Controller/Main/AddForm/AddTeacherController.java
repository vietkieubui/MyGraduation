/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.AddForm;

import Model.CourseModel;
import Model.TeacherModel;
import Services.Services;
import View.Main.AddForm.AddTeacherForm;
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
public class AddTeacherController {

    DefaultTableModel table;
    AddTeacherForm addTeacherForm;

    public AddTeacherController(DefaultTableModel table) {
        this.table = table;
        addTeacherForm = new AddTeacherForm();
        addFormButtonController();
        String[] listMajors = Services.getMajorsList();
        addTeacherForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));
    }

    void addFormButtonController() {
        Services.addActionListener(addTeacherForm.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addTeacherForm.majorsComboBox.getSelectedIndex() == 0) {
                    Services.showMess("Bạn chưa chọn khoa!");
                }else if(!Services.isValidPhoneNumber(addTeacherForm.phonenumberText.getText())){
                    Services.showMess("Số điện thoại không hợp lệ!");
                }else if(!Services.isValidEmail(addTeacherForm.emailText.getText())){
                    Services.showMess("Email không hợp lệ!");
                }
                else {
                    TeacherModel teacher = new TeacherModel(addTeacherForm.nameText.getText(),
                            addTeacherForm.academicRank.getText(), addTeacherForm.majorsComboBox.getSelectedItem().toString(), addTeacherForm.phonenumberText.getText(), addTeacherForm.emailText.getText());
                    String majorsId = Services.getMajorsId(teacher.majors);
                    Statement stm = null;
                    ResultSet rs = null;
                    String[] columnsName = {"name", "academicRank", "majors", "phonenumber", "email"};
                    String[] values = {Services.toSQLString(teacher.name, true), Services.toSQLString(teacher.academicRank, true),
                        Services.toSQLString(majorsId), Services.toSQLString(teacher.phonenumber), Services.toSQLString(teacher.email)};
                    if (Services.insertIntoDatabase("Teachers", columnsName, values)) {
                        Services.showMess("Thêm thành công!");
                        Services.getTeacher(table);
                        addTeacherForm.dispose();
                    }
                }
            }
        });
        Services.addActionListener(addTeacherForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTeacherForm.dispose();
            }
        });
    }

}
