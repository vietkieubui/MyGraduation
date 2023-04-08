/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.UpdateForm;

import Model.StudentModel;
import Services.Services;
import View.Main.UpdateForm.UpdateStudentForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public class UpdateStudentController {

    UpdateStudentForm updateStudentForm;
    DefaultTableModel table;
    StudentModel studentModel;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public UpdateStudentController(DefaultTableModel table, StudentModel studentModel) {
        this.table = table;
        this.studentModel = studentModel;

        updateStudentForm = new UpdateStudentForm();
        updateFormButtonController();
        updateStudentForm.idText.setText(studentModel.id);
        updateStudentForm.nameText.setText(studentModel.name);
        updateStudentForm.genderComboBox.setSelectedItem(studentModel.gender);
        updateStudentForm.birthday.setDateFormatString("dd-MM-yyyy");
        try {
            updateStudentForm.birthday.setDate(sdf.parse(studentModel.birthday));
        } catch (ParseException ex) {
            Logger.getLogger(UpdateStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] listMajors = Services.getMajorsList();
        updateStudentForm.majorsComboBox.setModel(new DefaultComboBoxModel<>(listMajors));

        String[] listCourse = Services.getCourseList("");
        updateStudentForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));

        String[] listClass = Services.getClassList("", "");
        updateStudentForm.classComboBox.setModel(new DefaultComboBoxModel<>(listClass));
        updateStudentForm.classComboBox.setSelectedItem(studentModel.classId);

        updateStudentForm.phonenumberText.setText(studentModel.phonenumber);
        updateStudentForm.emailText.setText(studentModel.email);

    }

    private void updateFormButtonController() {
        Services.addActionListener(updateStudentForm.majorsComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] listCourse = null;
                String[] listClass = null;
                if (updateStudentForm.majorsComboBox.getSelectedIndex() == 0) {
                    listCourse = Services.getCourseList("");
                    listClass = Services.getClassList("", "");
                } else {
                    listCourse = Services.getCourseList(updateStudentForm.majorsComboBox.getSelectedItem().toString());
                    listClass = Services.getClassList(updateStudentForm.majorsComboBox.getSelectedItem().toString(), "");
                }
                updateStudentForm.courseComboBox.setModel(new DefaultComboBoxModel<>(listCourse));
                updateStudentForm.classComboBox.setModel(new DefaultComboBoxModel<>(listClass));
            }
        });

        Services.addActionListener(updateStudentForm.courseComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] listClass = null;
                if (updateStudentForm.courseComboBox.getSelectedIndex() == 0
                        && updateStudentForm.majorsComboBox.getSelectedIndex() == 0) {
                    listClass = Services.getClassList("", "");
                } else if (updateStudentForm.courseComboBox.getSelectedIndex() == 0
                        && updateStudentForm.majorsComboBox.getSelectedIndex() != 0) {
                    listClass = Services.getClassList(updateStudentForm.majorsComboBox.getSelectedItem().toString(), "");
                } else if (updateStudentForm.courseComboBox.getSelectedIndex() != 0) {
                    String courseName = updateStudentForm.courseComboBox.getSelectedItem().toString();
                    updateStudentForm.majorsComboBox.setSelectedItem(Services.setSelectedMajors("", courseName));
                    updateStudentForm.courseComboBox.setSelectedItem(courseName);
                    listClass = Services.getClassList(updateStudentForm.majorsComboBox.getSelectedItem().toString(), courseName);

                }
                updateStudentForm.classComboBox.setModel(new DefaultComboBoxModel<>(listClass));
            }
        });

        Services.addActionListener(updateStudentForm.classComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (updateStudentForm.classComboBox.getSelectedIndex() != 0) {
                    String className = updateStudentForm.classComboBox.getSelectedItem().toString();
                    updateStudentForm.majorsComboBox.setSelectedItem(Services.setSelectedMajors(className, ""));
                    updateStudentForm.courseComboBox.setSelectedItem(Services.setSelectedCourses(className));
                    updateStudentForm.classComboBox.setSelectedItem(className);
                }
            }
        });

        Services.addActionListener(updateStudentForm.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (updateStudentForm.phonenumberText.getText().equals("")
                        || sdf.format(updateStudentForm.birthday.getDate()).equals("")
                        || updateStudentForm.classComboBox.getSelectedIndex() == 0
                        || updateStudentForm.genderComboBox.getSelectedIndex() == 0
                        || updateStudentForm.idText.getText().equals("")
                        || updateStudentForm.emailText.getText().equals("")
                        || updateStudentForm.nameText.getText().equals("")) {
                    Services.showMess("Bạn phải điền đầy đủ thông tin!");
                } else {
                    if (!Services.isValidPhoneNumber(updateStudentForm.phonenumberText.getText())) {
                        Services.showMess("Số điện thoại không hợp lệ!");
                    } else if (!Services.isValidEmail(updateStudentForm.emailText.getText())) {
                        Services.showMess("Email không hợp lệ!");
                    } else {
                        StudentModel student = new StudentModel(
                                updateStudentForm.idText.getText(), updateStudentForm.nameText.getText(),
                                updateStudentForm.genderComboBox.getSelectedItem().toString(),
                                sdf.format(updateStudentForm.birthday.getDate()),
                                updateStudentForm.classComboBox.getSelectedItem().toString(),
                                updateStudentForm.phonenumberText.getText(),
                                updateStudentForm.emailText.getText());
                        student.classId = Services.getClassId(student.classId);
                        String[] columnsName = {"name", "gender", "birthday", "class", "phonenumber", "email"};
                        String[] values = {
                            Services.toSQLString(student.name, true),
                            Services.toSQLString(student.gender, true),
                            Services.toSQLString(student.birthday, false),
                            Services.toSQLString(student.classId),
                            Services.toSQLString(student.phonenumber),
                            Services.toSQLString(student.email)};
                        if (Services.updateData("Students", columnsName, values, "id=" + Services.toSQLString(student.id))) {
                            Services.showMess("Cập nhật thành công!");
                            Services.getStudent(table);
                            updateStudentForm.dispose();
                        }

                    }
                }
            }
        });
        Services.addActionListener(updateStudentForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudentForm.dispose();
            }
        });
    }


}
