/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.AddForm;

import Model.StudentModel;
import Services.Services;
import View.Main.AddForm.AddSchoolYearForm;
import View.Main.AddForm.AddStudentForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public class AddStudentController {

    AddStudentForm addStudentForm;
    DefaultTableModel table;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public AddStudentController(DefaultTableModel table) {
        this.table = table;
        addStudentForm = new AddStudentForm();
        String[] listMajors = Services.getMajorsList();
        addStudentForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));
        String[] listCourse = Services.getCourseList("");
        addStudentForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
        String[] listClass = Services.getClassList("", "");
        addStudentForm.classComboBox.setModel(new DefaultComboBoxModel<>(listClass));

        addFormButtonController();
    }

    void addFormButtonController() {
        Services.addActionListener(addStudentForm.majorsComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] listCourse = null;
                String[] listClass = null;
                if (addStudentForm.majorsComboBox.getSelectedIndex() == 0) {
                    listCourse = Services.getCourseList("");
                    listClass = Services.getClassList("", "");
                } else {
                    listCourse = Services.getCourseList(addStudentForm.majorsComboBox.getSelectedItem().toString());
                    listClass = Services.getClassList(addStudentForm.majorsComboBox.getSelectedItem().toString(), "");
                }
                addStudentForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
                addStudentForm.classComboBox.setModel(new DefaultComboBoxModel<>(listClass));
            }
        });

        Services.addActionListener(addStudentForm.courseComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] listClass = null;
                if (addStudentForm.courseComboBox.getSelectedIndex() == 0
                        && addStudentForm.majorsComboBox.getSelectedIndex() == 0) {
                    listClass = Services.getClassList("", "");
                } else if (addStudentForm.courseComboBox.getSelectedIndex() == 0
                        && addStudentForm.majorsComboBox.getSelectedIndex() != 0) {
                    listClass = Services.getClassList(addStudentForm.majorsComboBox.getSelectedItem().toString(), "");
                } else if (addStudentForm.courseComboBox.getSelectedIndex() != 0) {
                    String courseName = addStudentForm.courseComboBox.getSelectedItem().toString();
                    addStudentForm.majorsComboBox.setSelectedItem(Services.setSelectedMajors("", courseName));
                    addStudentForm.courseComboBox.setSelectedItem(courseName);
                    listClass = Services.getClassList(addStudentForm.majorsComboBox.getSelectedItem().toString(), courseName);

                }
                addStudentForm.classComboBox.setModel(new DefaultComboBoxModel<>(listClass));
            }
        });

        Services.addActionListener(addStudentForm.classComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addStudentForm.classComboBox.getSelectedIndex() != 0) {
                    String className = addStudentForm.classComboBox.getSelectedItem().toString();
                    addStudentForm.majorsComboBox.setSelectedItem(Services.setSelectedMajors(className, ""));
                    addStudentForm.courseComboBox.setSelectedItem(Services.setSelectedCourses(className));
                    addStudentForm.classComboBox.setSelectedItem(className);
                }
            }
        });

        Services.addActionListener(addStudentForm.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addStudentForm.phonenumberText.getText().equals("")
                        || sdf.format(addStudentForm.birthday.getDate()).equals("")
                        || addStudentForm.classComboBox.getSelectedIndex() == 0
                        || addStudentForm.genderComboBox.getSelectedIndex() == 0
                        || addStudentForm.idText.getText().equals("")
                        || addStudentForm.emailText.getText().equals("")
                        || addStudentForm.nameText.getText().equals("")) {
                    Services.showMess("Bạn phải điền đầy đủ thông tin!");
                } else {
                    if (!Services.isValidPhoneNumber(addStudentForm.phonenumberText.getText())) {
                        Services.showMess("Số điện thoại không hợp lệ!");
                    } else if (!Services.isValidEmail(addStudentForm.emailText.getText())) {
                        Services.showMess("Email không hợp lệ!");
                    } else {
                        StudentModel student = new StudentModel(addStudentForm.idText.getText(), addStudentForm.nameText.getText(),
                                addStudentForm.genderComboBox.getSelectedItem().toString(), sdf.format(addStudentForm.birthday.getDate()),
                                addStudentForm.classComboBox.getSelectedItem().toString(), addStudentForm.phonenumberText.getText(),
                                addStudentForm.emailText.getText());
                        student.classId = Services.getClassId(student.classId);
                        String[] columnsName = {"id", "name", "gender", "birthday", "class", "phonenumber", "email"};
                        String[] values = {
                            Services.toSQLString(student.id), Services.toSQLString(student.name, true),
                            Services.toSQLString(student.gender, true), Services.toSQLString(student.birthday, false),
                            Services.toSQLString(student.classId), Services.toSQLString(student.phonenumber),
                            Services.toSQLString(student.email)};
                        if (Services.insertIntoDatabase("Students", columnsName, values)) {
                            Services.showMess("Thêm thành công!");
                            Services.getStudent(table);
                            addStudentForm.dispose();
                        }

                    }

                }

            }
        });

        Services.addActionListener(addStudentForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentForm.dispose();
            }
        });
    }
}
