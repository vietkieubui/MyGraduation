/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main;

import Controller.Main.AddForm.AddClassController;
import Controller.Main.AddForm.AddCourseController;
import Controller.Main.AddForm.AddMajorsController;
import Controller.Main.AddForm.AddProjectTopicController;
import Controller.Main.AddForm.AddSchoolYearController;
import Controller.Main.AddForm.AddStudentController;
import Controller.Main.AddForm.AddTeacherController;
import Controller.Main.UpdateForm.AssignTopicController;
import Controller.Main.UpdateForm.SubmitDocumentController;
import Controller.Main.UpdateForm.UpdateClassController;
import Controller.Main.UpdateForm.UpdateCourseController;
import Controller.Main.UpdateForm.UpdateMajorsController;
import Controller.Main.UpdateForm.UpdateProjectTopicController;
import Controller.Main.UpdateForm.UpdateSchoolYearController;
import Controller.Main.UpdateForm.UpdateStudentController;
import Controller.Main.UpdateForm.UpdateTeacherController;
import Model.ClassModel;
import Model.CourseModel;
import Model.MajorsModel;
import Model.ProjectTopicModel;
import Model.SchoolYearModel;
import Model.StudentModel;
import Model.TeacherModel;
import Services.MultiLineCellRenderer;
import Services.Services;
import View.Main.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public final class MainController {

    public MainView mainView;

    ClassManagementPanel classManagementPanel = new ClassManagementPanel();
    CourseManagementPanel courseManagementPanel = new CourseManagementPanel();
    MajorsManagementPanel majorsManagementPanel = new MajorsManagementPanel();
    ProjectManagementPanel projectManagementPanel = new ProjectManagementPanel();
    SchoolYearManagementPanel schoolYearManagementPanel = new SchoolYearManagementPanel();
    StudentManagementPanel studentManagementPanel = new StudentManagementPanel();
    TeacherManagementPanel teacherManagementPanel = new TeacherManagementPanel();
    JPanel currentPanel;

    /**
     * Table
     */
    DefaultTableModel schoolYearTable = (DefaultTableModel) schoolYearManagementPanel.schoolYearTable.getModel();
    DefaultTableModel majorsTable = (DefaultTableModel) majorsManagementPanel.majorsTable.getModel();
    DefaultTableModel courseTable = (DefaultTableModel) courseManagementPanel.courseTable.getModel();
    DefaultTableModel classTable = (DefaultTableModel) classManagementPanel.classTable.getModel();
    DefaultTableModel teacherTable = (DefaultTableModel) teacherManagementPanel.teacherTable.getModel();
    DefaultTableModel studentTable = (DefaultTableModel) studentManagementPanel.studentTable.getModel();
    DefaultTableModel projectTopicTable = (DefaultTableModel) projectManagementPanel.projectTopicTable.getModel();

    public MainController() {
        this.mainView = new MainView();
        setupTable();
        mainView.mainPanel.setLayout(new BorderLayout());
        mainView.mainPanel.add(projectManagementPanel, BorderLayout.NORTH);
        mainView.mainPanel.add(projectManagementPanel);
        Services.getNewProjectTopic(projectTopicTable);
        projectManagementPanel.setVisible(true);
        currentPanel = projectManagementPanel;
        mainView.setVisible(true);
        initValues();
        mainViewButtonActionListener();
        classPanelActionListener();
        coursePanelActionListener();
        majorsPanelActionListener();
        schoolYearPanelActionListener();
        teacherPanelActionListener();
        studentPanelActionListener();
        projectPanelActionListenter();
    }

    public void mainViewButtonActionListener() {
        Services.addActionListener(mainView.projectTopicButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.setVisible(false);
                mainView.mainPanel.add(projectManagementPanel, BorderLayout.NORTH);
                projectManagementPanel.setVisible(true);
                currentPanel = projectManagementPanel;
            }
        });

        Services.addActionListener(mainView.studentButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Services.getStudent(studentTable);
                currentPanel.setVisible(false);
                mainView.mainPanel.add(studentManagementPanel);
                studentManagementPanel.setVisible(true);
                currentPanel = studentManagementPanel;
            }
        });

        Services.addActionListener(mainView.teacherButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Services.getTeacher(teacherTable);
                currentPanel.setVisible(false);
                mainView.mainPanel.add(teacherManagementPanel);
                teacherManagementPanel.setVisible(true);
                currentPanel = teacherManagementPanel;
            }
        });

        Services.addActionListener(mainView.classButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Services.getClass(classTable);
                currentPanel.setVisible(false);
                mainView.mainPanel.add(classManagementPanel);
                classManagementPanel.setVisible(true);
                currentPanel = classManagementPanel;
            }
        });

        Services.addActionListener(mainView.courseButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Services.getCourse(courseTable);
                currentPanel.setVisible(false);
                mainView.mainPanel.add(courseManagementPanel);
                courseManagementPanel.setVisible(true);
                currentPanel = courseManagementPanel;
            }
        });

        Services.addActionListener(mainView.schoolYearButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Services.getSchoolYears(schoolYearTable);
                currentPanel.setVisible(false);
                mainView.mainPanel.add(schoolYearManagementPanel);
                schoolYearManagementPanel.setVisible(true);
                currentPanel = schoolYearManagementPanel;
            }
        });

        Services.addActionListener(mainView.majorsButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Services.getMajors(majorsTable);
                currentPanel.setVisible(false);
                mainView.mainPanel.add(majorsManagementPanel);
                majorsManagementPanel.setVisible(true);
                currentPanel = majorsManagementPanel;
            }
        });
    }

    private void projectPanelActionListenter() {
        Services.addActionListener(projectManagementPanel.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddProjectTopicController(projectTopicTable);
            }
        });

        Services.addActionListener(projectManagementPanel.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = projectManagementPanel.projectTopicTable.getSelectedRow();
                    String id = (String) projectTopicTable.getValueAt(updateRow, 0);
                    ProjectTopicModel projectTopicModel = Services.getProjectTopicInfo(id);
                    new UpdateProjectTopicController(projectTopicTable, projectTopicModel);

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });

        Services.addActionListener(projectManagementPanel.deleteButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deleteRow = projectManagementPanel.projectTopicTable.getSelectedRow();
                    String projectTopicId = (String) projectTopicTable.getValueAt(deleteRow, 0);
                    switch (Services.showConfirmDialog("Bạn có chắc chắn muốn xóa đề tài có mã: " + projectTopicId)) {
                        case 0:
                            if (Services.deleteData("ProjectTopics", "id=" + Services.toSQLString(projectTopicId))) {
                                Services.showMess("Xóa thành công");
                                Services.getNewProjectTopic(projectTopicTable);
                            }
                            break;
                        default:
                            System.out.println("Cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });

        Services.addActionListener(projectManagementPanel.assignButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int assignRow = projectManagementPanel.projectTopicTable.getSelectedRow();
                    String id = (String) projectTopicTable.getValueAt(assignRow, 0);
                    String name = (String) projectTopicTable.getValueAt(assignRow, 1);
                    new AssignTopicController(projectTopicTable, id,name);

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
        
        Services.addActionListener(projectManagementPanel.submitDocumentButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = projectManagementPanel.projectTopicTable.getSelectedRow();
                    String id = (String) projectTopicTable.getValueAt(updateRow, 0);
                    ProjectTopicModel projectTopicModel = Services.getProjectTopicWithStudentInfo(id);
                    new SubmitDocumentController(projectTopicModel);

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
                
            }
        });
    }

    public void studentPanelActionListener() {
        Services.addActionListener(studentManagementPanel.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddStudentController(studentTable);
            }
        });

        Services.addActionListener(studentManagementPanel.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = studentManagementPanel.studentTable.getSelectedRow();
                    StudentModel studentModel = new StudentModel(
                            (String) studentTable.getValueAt(updateRow, 0),
                            (String) studentTable.getValueAt(updateRow, 1),
                            (String) studentTable.getValueAt(updateRow, 2),
                            (String) studentTable.getValueAt(updateRow, 3),
                            (String) studentTable.getValueAt(updateRow, 4),
                            (String) studentTable.getValueAt(updateRow, 5),
                            (String) studentTable.getValueAt(updateRow, 6)
                    );
                    new UpdateStudentController(studentTable, studentModel);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });

        Services.addActionListener(studentManagementPanel.deleteButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deleteRow = studentManagementPanel.studentTable.getSelectedRow();
                    String studentId = (String) studentTable.getValueAt(deleteRow, 0);
                    String studentName = (String) studentTable.getValueAt(deleteRow, 1);
                    switch (Services.showConfirmDialog("Bạn có chắc chắn muốn xóa sinh viên " + studentName + ". Mã SV: " + studentId)) {
                        case 0:
                            if (Services.deleteData("Students", "id=" + Services.toSQLString(studentId))) {
                                Services.showMess("Xóa thành công");
                                Services.getStudent(studentTable);
                            }
                            break;
                        default:
                            System.out.println("Cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
    }

    public void teacherPanelActionListener() {
        Services.addActionListener(teacherManagementPanel.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddTeacherController(teacherTable);
            }
        });
        Services.addActionListener(teacherManagementPanel.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = teacherManagementPanel.teacherTable.getSelectedRow();
                    TeacherModel teacherModel = new TeacherModel(
                            (String) teacherTable.getValueAt(updateRow, 1),
                            (String) teacherTable.getValueAt(updateRow, 2),
                            (String) teacherTable.getValueAt(updateRow, 3),
                            (String) teacherTable.getValueAt(updateRow, 4),
                            (String) teacherTable.getValueAt(updateRow, 5)
                    );

                    teacherModel.id = (String) teacherTable.getValueAt(updateRow, 0);
                    new UpdateTeacherController(teacherTable, teacherModel);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
        Services.addActionListener(teacherManagementPanel.deleteButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deleteRow = teacherManagementPanel.teacherTable.getSelectedRow();
                    String teacherId = (String) teacherTable.getValueAt(deleteRow, 0);
                    String teacherName = (String) teacherTable.getValueAt(deleteRow, 1);
                    switch (Services.showConfirmDialog("Bạn có chắc chắn muốn xóa giảng viên " + teacherName + ". Mã GV: " + teacherId)) {
                        case 0:
                            if (Services.deleteData("Teachers", "id=" + Services.toSQLString(teacherId))) {
                                Services.showMess("Xóa thành công");
                                Services.getTeacher(teacherTable);
                            }
                            break;
                        default:
                            System.out.println("Cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
    }

    public void classPanelActionListener() {
        Services.addActionListener(classManagementPanel.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddClassController(classTable);
            }
        });
        Services.addActionListener(classManagementPanel.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = classManagementPanel.classTable.getSelectedRow();
                    ClassModel classModel = new ClassModel(
                            (String) classTable.getValueAt(updateRow, 1),
                            (String) classTable.getValueAt(updateRow, 5),
                            (String) classTable.getValueAt(updateRow, 2)
                    );
                    classModel.id = (String) classTable.getValueAt(updateRow, 0);
                    new UpdateClassController(classTable, classModel);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
        Services.addActionListener(classManagementPanel.deleteButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deleteRow = classManagementPanel.classTable.getSelectedRow();
                    String classId = (String) classTable.getValueAt(deleteRow, 0);
                    String className = (String) classTable.getValueAt(deleteRow, 1);
                    switch (Services.showConfirmDialog("Bạn có chắc chắn muốn xóa lớp " + className + ". ID: " + classId)) {
                        case 0:
                            if (Services.deleteData("Classes", "id=" + Services.toSQLString(classId))) {
                                Services.showMess("Xóa thành công");
                                Services.getClass(classTable);
                            }
                            break;
                        default:
                            System.out.println("Cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
    }

    public void coursePanelActionListener() {
        Services.addActionListener(courseManagementPanel.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCourseController(courseTable);
            }
        });
        Services.addActionListener(courseManagementPanel.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = courseManagementPanel.courseTable.getSelectedRow();
                    CourseModel courseModel = new CourseModel(
                            (String) courseTable.getValueAt(updateRow, 1),
                            (String) courseTable.getValueAt(updateRow, 3),
                            (String) courseTable.getValueAt(updateRow, 4),
                            (String) courseTable.getValueAt(updateRow, 2)
                    );
                    courseModel.id = (String) courseTable.getValueAt(updateRow, 0);
                    new UpdateCourseController(courseTable, courseModel);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }

            }
        });
        Services.addActionListener(courseManagementPanel.deleteButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deleteRow = courseManagementPanel.courseTable.getSelectedRow();
                    String courseId = (String) courseTable.getValueAt(deleteRow, 0);
                    String courseName = (String) courseTable.getValueAt(deleteRow, 1);
                    switch (Services.showConfirmDialog("Bạn có chắc chắn muốn xóa khóa " + courseName + ". ID: " + courseId)) {
                        case 0:
                            if (Services.deleteData("Courses", "id=" + Services.toSQLString(courseId))) {
                                Services.showMess("Xóa thành công");
                                Services.getCourse(courseTable);
                            }
                            break;
                        default:
                            System.out.println("Cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
    }

    public void majorsPanelActionListener() {
        Services.addActionListener(majorsManagementPanel.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddMajorsController(majorsTable);
            }
        });
        Services.addActionListener(majorsManagementPanel.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = majorsManagementPanel.majorsTable.getSelectedRow();
                    MajorsModel majorsModel = new MajorsModel(
                            (String) majorsTable.getValueAt(updateRow, 0),
                            (String) majorsTable.getValueAt(updateRow, 1),
                            (String) majorsTable.getValueAt(updateRow, 2));
                    new UpdateMajorsController(majorsTable, majorsModel);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
        Services.addActionListener(majorsManagementPanel.deleteButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deleteRow = majorsManagementPanel.majorsTable.getSelectedRow();
                    String majorsId = (String) majorsTable.getValueAt(deleteRow, 0);
                    String majorsName = (String) majorsTable.getValueAt(deleteRow, 1);
                    switch (Services.showConfirmDialog("Bạn có chắc chắn muốn xóa khoa " + majorsName + ". ID: " + majorsId)) {
                        case 0:
                            if (Services.deleteData("Majors", "majorsId=" + Services.toSQLString(majorsId))) {
                                Services.showMess("Xóa thành công");
                                Services.getMajors(majorsTable);
                            }
                            break;
                        default:
                            System.out.println("Cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });

    }

    public void schoolYearPanelActionListener() {
        Services.addActionListener(schoolYearManagementPanel.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddSchoolYearController(schoolYearTable);
            }
        });
        Services.addActionListener(schoolYearManagementPanel.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int updateRow = schoolYearManagementPanel.schoolYearTable.getSelectedRow();
                    SchoolYearModel schoolYearModel = new SchoolYearModel((String) schoolYearTable.getValueAt(updateRow, 1));
                    schoolYearModel.id = (String) schoolYearTable.getValueAt(updateRow, 0);
                    new UpdateSchoolYearController(schoolYearTable, schoolYearModel);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }

            }
        });
        Services.addActionListener(schoolYearManagementPanel.deleteButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deleteRow = schoolYearManagementPanel.schoolYearTable.getSelectedRow();
                    String schoolYearId = (String) schoolYearTable.getValueAt(deleteRow, 0);
                    String schoolYearName = (String) schoolYearTable.getValueAt(deleteRow, 1);
                    switch (Services.showConfirmDialog("Bạn có chắc chắn muốn xóa năm học " + schoolYearName + ". ID: " + schoolYearId)) {
                        case 0:
                            if (Services.deleteData("SchoolYears", "id=" + Services.toSQLString(schoolYearId))) {
                                Services.showMess("Xóa thành công");
                                Services.getSchoolYears(schoolYearTable);
                            }
                            break;
                        default:
                            System.out.println("Cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Services.showMess("Bạn phải chọn 1 hàng!");
                }
            }
        });
    }

    public void setupTable() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        schoolYearManagementPanel.schoolYearTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        projectManagementPanel.projectTopicTable.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
        projectManagementPanel.projectTopicTable.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
        projectManagementPanel.projectTopicTable.getColumnModel().getColumn(3).setCellRenderer(new MultiLineCellRenderer());
    }

    private void initValues() {
        if (projectManagementPanel.statusComboBox.getSelectedIndex() == 0) {
            projectManagementPanel.assignButton.setEnabled(true);
            projectManagementPanel.submitDocumentButton.setEnabled(false);
        }
        Services.addActionListener(projectManagementPanel.statusComboBox, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (projectManagementPanel.statusComboBox.getSelectedIndex() == 0) {
                    projectManagementPanel.assignButton.setEnabled(true);
                    projectManagementPanel.submitDocumentButton.setEnabled(false);
                    Services.getNewProjectTopic(projectTopicTable);
                } else {
                    projectManagementPanel.assignButton.setEnabled(false);
                    projectManagementPanel.submitDocumentButton.setEnabled(true);
                    if (projectManagementPanel.statusComboBox.getSelectedIndex() == 1) {
                        Services.getDoingProjectTopic(projectTopicTable);
                    }
                }
            }
        });
    }
}
