package Services;

import Model.Auth.RegisterModel;
import Model.Auth.User;
import Model.CourseModel;
import Model.DocumentModel;
import Model.MajorsModel;
import Model.ProjectTopicModel;
import Model.SchoolYearModel;
import Model.StudentModel;
import Model.TeacherModel;
import static Services.ConnectDatabase.cnn;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.security.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author BVKieu
 */
public final class Services {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public static void addActionListener(JButton btn, ActionListener al) {
        btn.addActionListener(al);
    }

    public static void addActionListener(JLabel lb, MouseListener ml) {
        lb.addMouseListener(ml);
    }

    public static void addActionListener(JComboBox cbb, ActionListener al) {
        cbb.addActionListener(al);
    }

    public static boolean login(String username, String password) {
        String hashedPassword = Services.hashPassword(password);
        String sql = "SELECT * FROM Users WHERE username = " + toSQLString(username) + " AND password = " + toSQLString(hashedPassword);
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                User.setUser(rs.getString("id"), rs.getString(3), rs.getString(2), rs.getString(4));
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean register(String name, String username, String phonenumber, String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Mật khẩu không trùng khớp!");
            return false;
        }
        if (checkExist("Users", "username", username)) {
            showMess("Tên đăng nhập đã được sử dụng!");
            return false;
        } else {
            String hashedPassword = Services.hashPassword(password);
            RegisterModel registerModel = new RegisterModel(name, phonenumber, username, hashedPassword, confirmPassword);

            String[] columnsName = {"name", "username", "phonenumber", "password"};
            String[] values = {Services.toSQLString(registerModel.name, true), Services.toSQLString(registerModel.username), Services.toSQLString(registerModel.phonenumber), Services.toSQLString(registerModel.password)};
            try {
                if (Services.insertIntoDatabase("Users", columnsName, values)) {
                    Services.showMess("Đăng ký thành công");
                    return true;
                }
            } catch (Exception ex) {
            }
        }
        return false;
    }

    public static boolean insertIntoDatabase(String tableName, String[] columnsName, String[] values) {
        String columnsString = "";
        String valuesString = "";
        for (int i = 0; i < columnsName.length; i++) {
            columnsString += columnsName[i];
            if (i + 1 < columnsName.length) {
                columnsString += ",";
            }
        }
        for (int i = 0; i < values.length; i++) {
            valuesString += values[i];
            if (i + 1 < values.length) {
                valuesString += ",";
            }
        }
        String sql = "INSERT INTO " + tableName + "(" + columnsString + ")" + "VALUES(" + valuesString + ")";
        System.out.println(sql);
        try {
            Statement stm = cnn.createStatement();
            stm.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
            showMess(ex.toString());
            return false;
        }
    }

    public static boolean updateData(String tableName, String[] columnsName, String[] values, String condition) {
        String valuesString = "";
        int length = columnsName.length;
        for (int i = 0; i < length; i++) {
            valuesString = valuesString + columnsName[i] + "=" + values[i];
            if (i + 1 < columnsName.length) {
                valuesString += ",";
            }
        }

        String sql = "UPDATE " + tableName + " SET " + valuesString + " WHERE " + condition;
        System.out.println(sql);
        try {
            Statement stm = cnn.createStatement();
            stm.executeUpdate(sql);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
            showMess(ex.toString());
            return false;
        }
    }

    public static boolean deleteData(String tableName, String condition) {
        String sql = "DELETE " + tableName + " WHERE " + condition;
        System.out.println(sql);
        try {
            Statement stm = cnn.createStatement();
            stm.executeUpdate(sql);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
            showMess(ex.toString());
            return false;
        }
    }

    public static boolean checkExist(String tableName, String column, String value) {
        boolean flag = false;
        String sql = "SELECT * FROM " + tableName + " where " + column + " = " + toSQLString(value);
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                flag = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    public static String toSQLString(String s, boolean... vietnamese) {
        if (vietnamese.length > 0) {
            if (vietnamese[0]) {
                return "N'" + s + "'";
            }
        }
        return "'" + s + "'";
    }

    public static String hashPassword(String password) {
        String hashedPassword = "";
        try {
            byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfPassword);
            BigInteger no = new BigInteger(1, theMD5digest);
            hashedPassword = no.toString(16);
            return hashedPassword;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashedPassword;
    }

    public static void showMess(String st) {
        JOptionPane.showMessageDialog(null, st);
    }

    public static int showConfirmDialog(String st) {
        return JOptionPane.showConfirmDialog(null, st);
    }

    /**
     *
     * get ComboBox data
     */
    public static String[] getMajorsList() {
        String sql = "SELECT DISTINCT Majors.name FROM Majors "
                + "ORDER BY Majors.name";

        String[] listMajors = null;
        ArrayList<String> list = new ArrayList<String>();
        list.add("--Chọn khoa--");

        try {
            Statement stm = ConnectDatabase.cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Services.showMess("Lỗi đọc dữ liệu từ SQL Server!");
        }
        listMajors = list.toArray(new String[list.size()]);
        return listMajors;
    }

    public static String[] getCourseList(String majorsName) {
        String sql = "SELECT*FROM Courses "
                + "ORDER BY Courses.name";
        if (!majorsName.equals("")) {
            String majorsId = getMajorsId(majorsName);
            sql = "SELECT * FROM Courses "
                    + "WHERE majors = " + toSQLString(majorsId) + " ORDER BY Courses.name";
        }
        String[] courseList = null;
        ArrayList<String> list = new ArrayList<String>();
        list.add("--Chọn khóa--");

        try {
            Statement stm = ConnectDatabase.cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Services.showMess("Lỗi đọc dữ liệu từ SQL Server!");
        }
        courseList = list.toArray(new String[list.size()]);
        return courseList;
    }

    public static String[] getClassList(String majorsName, String courseName) {
        String sql = "SELECT DISTINCT Classes.name FROM Classes, Courses, Majors "
                + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors "
                + "ORDER BY Classes.name";
        if (!majorsName.equals("") && courseName.equals("")) {
            String majorsId = getMajorsId(majorsName);
            sql = "SELECT DISTINCT Classes.name FROM Classes, Courses, Majors "
                    + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Majors.id = " + toSQLString(majorsId) + " "
                    + "ORDER BY Classes.name";
        } else if (majorsName.equals("") && !courseName.equals("")) {
            String courseId = getCourseId(courseName);
            sql = "SELECT DISTINCT Classes.name FROM Classes, Courses, Majors "
                    + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Courses.id = " + toSQLString(courseId) + " "
                    + "ORDER BY Classes.name";
        } else if (!majorsName.equals("") && !courseName.equals("")) {
            String courseId = getCourseId(courseName);
            String majorsId = getMajorsId(majorsName);
            sql = "SELECT DISTINCT Classes.name FROM Classes, Courses, Majors "
                    + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors "
                    + "and Courses.id = " + toSQLString(courseId) + " and Majors.id = " + toSQLString(majorsId) + " "
                    + "ORDER BY Classes.name";
        }
        String[] courseList = null;
        ArrayList<String> list = new ArrayList<String>();
        list.add("--Chọn lớp--");

        try {
            Statement stm = ConnectDatabase.cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Services.showMess("Lỗi đọc dữ liệu từ SQL Server!");
        }
        courseList = list.toArray(new String[list.size()]);
        return courseList;
    }

    public static String[] getSchoolYearList() {
        String sql = "SELECT DISTINCT SchoolYears.name FROM SchoolYears "
                + "ORDER BY SchoolYears.name";

        String[] listSchoolYear = null;
        ArrayList<String> list = new ArrayList<String>();
        list.add("--Chọn năm học--");

        try {
            Statement stm = ConnectDatabase.cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Services.showMess("Lỗi đọc dữ liệu từ SQL Server!");
        }
        listSchoolYear = list.toArray(new String[list.size()]);
        return listSchoolYear;
    }

    public static String setSelectedMajors(String className, String courseName) {
        String majors = "";
        String sql = "SELECT DISTINCT Majors.name FROM Classes, Courses, Majors "
                + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors";
        if (!className.equals("") && courseName.equals("")) {
            sql = "SELECT DISTINCT Majors.name FROM Classes, Courses, Majors "
                    + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Classes.name = " + toSQLString(className);
        } else if (className.equals("") && !courseName.equals("")) {
            sql = "SELECT DISTINCT Majors.name FROM Courses, Majors "
                    + "WHERE Majors.id = Courses.majors and Courses.name = " + toSQLString(courseName);
        } else if (!className.equals("") && !courseName.equals("")) {
            sql = "SELECT DISTINCT Majors.name FROM Classes, Courses, Majors "
                    + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Courses.name = " + toSQLString(courseName) + " and Classes.name = " + toSQLString(className);
        }
        try {
            Statement stm = ConnectDatabase.cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                majors = rs.getString("name");
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Services.showMess("Lỗi đọc dữ liệu từ SQL Server!");
        }
        return majors;
    }

    public static String setSelectedCourses(String className) {
        String course = "";
        String sql = "SELECT DISTINCT Courses.name FROM Classes, Courses, Majors "
                + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors "
                + "ORDER BY Courses.name";
        if (!className.equals("")) {
            sql = "SELECT DISTINCT Courses.name FROM Classes, Courses, Majors "
                    + "WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Classes.name = " + toSQLString(className) + " "
                    + "ORDER BY Courses.name";
        }
        try {
            Statement stm = ConnectDatabase.cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                course = rs.getString("name");
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Services.showMess("Lỗi đọc dữ liệu từ SQL Server!");
        }
        return course;
    }

    /**
     *
     * get ComboBox data ID
     */
    public static String getMajorsId(String name) {
        String majorsId = "";
        String getMajorsIdSQL = "SELECT * FROM Majors";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = ConnectDatabase.cnn.createStatement();
            rs = stm.executeQuery(getMajorsIdSQL);
            while (rs.next()) {
                if (rs.getString("name").equals(name)) {
                    majorsId = rs.getString("id");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Services.showMess("SQL Error!");
        }
        return majorsId;
    }

    public static String getCourseId(String name) {
        String majorsId = "";
        String getCourseIdSQL = "SELECT * FROM Courses";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = ConnectDatabase.cnn.createStatement();
            rs = stm.executeQuery(getCourseIdSQL);
            while (rs.next()) {
                if (rs.getString("name").equals(name)) {
                    majorsId = rs.getString("id");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Services.showMess("SQL Error!");
        }
        return majorsId;
    }

    public static String getClassId(String name) {
        String classId = "";
        String getClassIdSQL = "SELECT * FROM Classes";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = ConnectDatabase.cnn.createStatement();
            rs = stm.executeQuery(getClassIdSQL);
            while (rs.next()) {
                if (rs.getString("name").equals(name)) {
                    classId = rs.getString("id");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Services.showMess("SQL Error!");
        }
        return classId;
    }

    public static String getSchoolYearId(String name) {
        String schoolYearId = "";
        String getSchoolYearIdSQL = "SELECT * FROM SchoolYears WHERE name=" + toSQLString(name);
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = ConnectDatabase.cnn.createStatement();
            rs = stm.executeQuery(getSchoolYearIdSQL);
            while (rs.next()) {
                schoolYearId = rs.getString("id");
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Services.showMess("SQL Error!");
        }
        return schoolYearId;
    }

    /**
     *
     * @get table data
     */
    public static void getSchoolYears(DefaultTableModel schoolYearTable) {
        schoolYearTable.setRowCount(0);
        String sql = "SELECT * FROM SchoolYears ORDER BY name ASC";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                schoolYearTable.addRow(new Object[]{rs.getString("id"), rs.getString("name")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getMajors(DefaultTableModel majorsTable) {
        majorsTable.setRowCount(0);
        String sql = "SELECT * FROM Majors ORDER BY name ASC";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                majorsTable.addRow(new Object[]{rs.getString("majorsId"), rs.getString("name"), rs.getString("description")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getCourse(DefaultTableModel courseTable) {
        courseTable.setRowCount(0);
        String sql = "SELECT Courses.id, Courses.name, Courses.description, Majors.name as majors, Courses.studyTime FROM Courses, Majors WHERE Courses.majors = Majors.id ORDER BY name ASC";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                courseTable.addRow(new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("majors"), rs.getString("description"), rs.getString("studyTime")});
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getClass(DefaultTableModel classTable) {
        classTable.setRowCount(0);
        String sql = "SELECT Classes.id, Classes.name, Courses.name as course, Majors.name as majors, Courses.studyTime, Classes.description FROM Classes, Courses, Majors WHERE Classes.course = Courses.id and Courses.majors = Majors.id ORDER BY Courses.studyTime, name";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                classTable.addRow(new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("course"), rs.getString("majors"), rs.getString("studyTime"), rs.getString("description")});
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getTeacher(DefaultTableModel teacherTable) {
        teacherTable.setRowCount(0);
        String sql = "SELECT Teachers.id, Teachers.name, Teachers.academicRank, Majors.name as majors, Teachers.phonenumber, Teachers.email "
                + "FROM Teachers, Majors WHERE Teachers.majors = Majors.id ORDER BY Teachers.name";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                teacherTable.addRow(new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("academicRank"), rs.getString("majors"), rs.getString("phonenumber"), rs.getString("email")});
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void getStudent(DefaultTableModel studentTable) {
        studentTable.setRowCount(0);
        String sql = "SELECT Students.id, Students.name, Students.gender, Students.birthday, Classes.name as class, Students.phonenumber, Students.email FROM Students, Classes WHERE Students.class = Classes.id \n"
                + "GROUP BY Classes.name, Students.id, Students.name, Students.gender, Students.birthday,Students.phonenumber, Students.email\n"
                + "ORDER BY  Classes.name, Students.id";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                studentTable.addRow(new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("gender"), rs.getString("birthday"), rs.getString("class"), rs.getString("phonenumber"), rs.getString("email")});
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void getNotAssignStudent(DefaultTableModel studentTable) {
        studentTable.setRowCount(0);
        String sql = "SELECT Students.id, Students.name, Students.gender, Students.birthday, Students.class, Students.phonenumber, Students.email\n"
                + "FROM(\n"
                + "	SELECT Students.id, Students.name, Students.gender, Students.birthday, Classes.name as class, Students.phonenumber, Students.email \n"
                + "	FROM Students, Classes \n"
                + "	WHERE Students.class = Classes.id \n"
                + ") as Students\n"
                + "LEFT JOIN ProjectTopics On ProjectTopics.student = Students.id\n"
                + "WHERE ProjectTopics.student IS NULL\n"
                + "GROUP BY Students.class, Students.id, Students.name, Students.gender, Students.birthday,Students.phonenumber, Students.email\n"
                + "ORDER BY  Students.class, Students.id";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                studentTable.addRow(new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("gender"), rs.getString("birthday"), rs.getString("class"), rs.getString("phonenumber"), rs.getString("email")});
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getNewProjectTopic(DefaultTableModel projectTopicTable) {
        projectTopicTable.setRowCount(0);
        String sql = "SELECT ProjectTopics.id, \n"
                + "ProjectTopics.name, \n"
                + "ProjectTopics.description, \n"
                + "ProjectTopics.require, \n"
                + "TeachersInfor.id as teacherId,\n"
                + "TeachersInfor.name as teacherName,\n"
                + "TeachersInfor.academicRank as teacherAcademicRank,\n"
                + "TeachersInfor.majorsName as teacherMajors,\n"
                + "Courses.name as course,\n"
                + "Majors.name as majors,\n"
                + "SchoolYears.name as schoolYear,\n"
                + "ProjectTopics.createdAt,\n"
                + "Users.id as userId,\n"
                + "Users.name as createdBy  \n"
                + "FROM ProjectTopics, Courses, Majors, SchoolYears, Users, (\n"
                + "SELECT Teachers.id, Teachers.name, Teachers.academicRank, Teachers.majors as majorsId, majors.name as majorsName \n"
                + "FROM Teachers, Majors\n"
                + "WHERE Teachers.majors = Majors.id\n"
                + ") as TeachersInfor\n"
                + "WHERE ProjectTopics.teacher = TeachersInfor.id\n"
                + "and ProjectTopics.student IS NULL\n"
                + "and Courses.id = ProjectTopics.course \n"
                + "and Courses.majors = Majors.id \n"
                + "and SchoolYears.id = ProjectTopics.schoolYear\n"
                + "and Users.id = ProjectTopics.createdBy\n"
                + "ORDER BY ProjectTopics.id ASC";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                projectTopicTable.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("require"),
                    "",
                    "",
                    rs.getString("teacherAcademicRank") + ". " + rs.getString("teacherName"),
                    rs.getString("course"),
                    rs.getString("majors"),
                    rs.getString("schoolYear"),
                    rs.getString("createdAt"),
                    rs.getString("createdBy")
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getDoingProjectTopic(DefaultTableModel projectTopicTable) {
        projectTopicTable.setRowCount(0);
        String sql = "SELECT DISTINCT ProjectTopics.id, \n"
                + "ProjectTopics.name, \n"
                + "ProjectTopics.description, \n"
                + "ProjectTopics.require, \n"
                + "Students.id as studentId,\n"
                + "Students.name as studentName,	\n"
                + "TeachersInfor.id as teacherId,\n"
                + "TeachersInfor.name as teacherName,\n"
                + "TeachersInfor.academicRank as teacherAcademicRank,\n"
                + "TeachersInfor.majorsName as teacherMajors,\n"
                + "Courses.name as course,\n"
                + "Majors.name as majors,\n"
                + "SchoolYears.name as schoolYear,\n"
                + "ProjectTopics.createdAt,\n"
                + "Users.id as userId,\n"
                + "Users.name as createdBy  \n"
                + "FROM ProjectTopics, Courses, Majors, SchoolYears, Students, Users, (\n"
                + "SELECT Teachers.id, Teachers.name, Teachers.academicRank, Teachers.majors as majorsId, majors.name as majorsName \n"
                + "FROM Teachers, Majors\n"
                + "WHERE Teachers.majors = Majors.id\n"
                + ") as TeachersInfor\n"
                + "WHERE ProjectTopics.teacher = TeachersInfor.id\n"
                + "and Courses.id = ProjectTopics.course \n"
                + "and Courses.majors = Majors.id \n"
                + "AND Students.id = ProjectTopics.student\n"
                + "and SchoolYears.id = ProjectTopics.schoolYear\n"
                + "and Users.id = ProjectTopics.createdBy\n"
                + "ORDER BY ProjectTopics.id ASC";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                projectTopicTable.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("require"),
                    rs.getString("studentId"),
                    rs.getString("studentName"),
                    rs.getString("teacherAcademicRank") + ". " + rs.getString("teacherName"),
                    rs.getString("course"),
                    rs.getString("majors"),
                    rs.getString("schoolYear"),
                    rs.getString("createdAt"),
                    rs.getString("createdBy")
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * validate form
     */
    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Matcher matcher = PHONE_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }

    //Time
    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS dd-MM-yyyy");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }

    //Test SQL
    public static void testInsertIntoDatabase(String tableName, String[] columnsName, String[] values) {
        String columnsString = "";
        String valuesString = "";
        for (int i = 0; i < columnsName.length; i++) {
            columnsString += columnsName[i];
            if (i + 1 < columnsName.length) {
                columnsString += ",";
            }
        }
        for (int i = 0; i < values.length; i++) {
            valuesString += values[i];
            if (i + 1 < values.length) {
                valuesString += ",";
            }
        }
        String sql = "INSERT INTO " + tableName + "(" + columnsString + ")" + "VALUES(" + valuesString + ")";
        System.out.println(sql);
    }

    public static void testUpdateData(String tableName, String[] columnsName, String[] values, String condition) {
        String valuesString = "";
        int length = columnsName.length;
        for (int i = 0; i < length; i++) {
            valuesString = valuesString + columnsName[i] + "=" + values[i];
            if (i + 1 < columnsName.length) {
                valuesString += ",";
            }
        }

        String sql = "UPDATE " + tableName + " SET " + valuesString + " WHERE " + condition;
        System.out.println(sql);
    }

    public static void testDeleteData(String tableName, String condition) {
        String sql = "DELETE " + tableName + " WHERE " + condition;
        System.out.println(sql);
    }

    //GET MODEL
    public static ProjectTopicModel getProjectTopicInfo(String projectTopicId) {
        ProjectTopicModel projectTopic = new ProjectTopicModel();
        String sql = "SELECT ProjectTopics.id,\n"
                + "ProjectTopics.name,\n"
                + "ProjectTopics.description,\n"
                + "ProjectTopics.require,\n"
                + "TeachersInfor.id as teacherId,\n"
                + "TeachersInfor.name as teacherName,\n"
                + "TeachersInfor.academicRank as teacherAcademicRank,\n"
                + "TeachersInfor.phonenumber as teacherPhoneNumber,\n"
                + "TeachersInfor.majorsName as teacherMajors,\n"
                + "TeachersInfor.email as teacherEmail,\n"
                + "Courses.id as courseId,\n"
                + "Courses.name as courseName,\n"
                + "Majors.id as majorsId,\n"
                + "Majors.name as majorsName,\n"
                + "SchoolYears.id as schoolYearId,\n"
                + "SchoolYears.name as schoolYearName\n"
                + "FROM ProjectTopics, Courses, Majors, SchoolYears, (\n"
                + "SELECT Teachers.*, Teachers.majors as majorsId, majors.name as majorsName \n"
                + "FROM Teachers, Majors\n"
                + "WHERE Teachers.majors = Majors.id\n"
                + ") as TeachersInfor\n"
                + "WHERE ProjectTopics.id=" + toSQLString(projectTopicId) + " \n"
                + "and ProjectTopics.teacher = TeachersInfor.id\n"
                + "and ProjectTopics.course = Courses.id\n"
                + "and ProjectTopics.schoolYear = SchoolYears.id\n"
                + "and Courses.majors = Majors.id";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                TeacherModel teacherModel = new TeacherModel();
                teacherModel.id = rs.getString("teacherId");
                teacherModel.name = rs.getString("teacherName");
                teacherModel.academicRank = rs.getString("teacherAcademicRank");
                teacherModel.majors = rs.getString("teacherMajors");
                teacherModel.phonenumber = rs.getString("teacherPhoneNumber");
                teacherModel.email = rs.getString("teacherEmail");

                CourseModel courseModel = new CourseModel();
                courseModel.id = rs.getString("courseId");
                courseModel.name = rs.getString("courseName");

                MajorsModel majorsModel = new MajorsModel();
                majorsModel.majorsId = rs.getString("majorsId");
                majorsModel.name = rs.getString("majorsName");

                SchoolYearModel schoolYearModel = new SchoolYearModel();
                schoolYearModel.id = rs.getString("schoolYearId");
                schoolYearModel.name = rs.getString("schoolYearName");

                projectTopic.id = rs.getString("id");
                projectTopic.name = rs.getString("name");
                projectTopic.description = rs.getString("description");
                projectTopic.require = rs.getString("require");
                projectTopic.teacher = teacherModel;
                projectTopic.course = courseModel;
                projectTopic.majors = majorsModel;
                projectTopic.schoolYear = schoolYearModel;
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projectTopic;
    }

    public static ProjectTopicModel getProjectTopicWithStudentInfo(String projectTopicId) {
        ProjectTopicModel projectTopic = new ProjectTopicModel();
        String sql = "SELECT ProjectTopics.id,\n"
                + "ProjectTopics.name,\n"
                + "ProjectTopics.description,\n"
                + "ProjectTopics.require,\n"
                + "Students.id as studentId,\n"
                + "Students.name as studentName,\n"
                + "TeachersInfor.id as teacherId,\n"
                + "TeachersInfor.name as teacherName,\n"
                + "TeachersInfor.academicRank as teacherAcademicRank,\n"
                + "TeachersInfor.phonenumber as teacherPhoneNumber,\n"
                + "TeachersInfor.majorsName as teacherMajors,\n"
                + "TeachersInfor.email as teacherEmail,\n"
                + "Courses.id as courseId,\n"
                + "Courses.name as courseName,\n"
                + "Majors.id as majorsId,\n"
                + "Majors.name as majorsName,\n"
                + "SchoolYears.id as schoolYearId,\n"
                + "SchoolYears.name as schoolYearName\n"
                + "FROM ProjectTopics, Courses, Majors, SchoolYears, Students, (\n"
                + "SELECT Teachers.*, Teachers.majors as majorsId, majors.name as majorsName\n"
                + "FROM Teachers, Majors\n"
                + "WHERE Teachers.majors = Majors.id\n"
                + ") as TeachersInfor\n"
                + "WHERE ProjectTopics.id=" + toSQLString(projectTopicId) + "\n"
                + "and ProjectTopics.student = Students.id\n"
                + "and ProjectTopics.teacher = TeachersInfor.id\n"
                + "and ProjectTopics.course = Courses.id\n"
                + "and ProjectTopics.schoolYear = SchoolYears.id\n"
                + "and Courses.majors = Majors.id";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                StudentModel studentModel = new StudentModel();
                studentModel.id = rs.getString("studentId");
                studentModel.name = rs.getString("studentName");

                TeacherModel teacherModel = new TeacherModel();
                teacherModel.id = rs.getString("teacherId");
                teacherModel.name = rs.getString("teacherName");
                teacherModel.academicRank = rs.getString("teacherAcademicRank");
                teacherModel.majors = rs.getString("teacherMajors");
                teacherModel.phonenumber = rs.getString("teacherPhoneNumber");
                teacherModel.email = rs.getString("teacherEmail");

                CourseModel courseModel = new CourseModel();
                courseModel.id = rs.getString("courseId");
                courseModel.name = rs.getString("courseName");

                MajorsModel majorsModel = new MajorsModel();
                majorsModel.majorsId = rs.getString("majorsId");
                majorsModel.name = rs.getString("majorsName");

                SchoolYearModel schoolYearModel = new SchoolYearModel();
                schoolYearModel.id = rs.getString("schoolYearId");
                schoolYearModel.name = rs.getString("schoolYearName");

                projectTopic.id = rs.getString("id");
                projectTopic.name = rs.getString("name");
                projectTopic.description = rs.getString("description");
                projectTopic.require = rs.getString("require");
                projectTopic.student = studentModel;
                projectTopic.teacher = teacherModel;
                projectTopic.course = courseModel;
                projectTopic.majors = majorsModel;
                projectTopic.schoolYear = schoolYearModel;
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projectTopic;
    }

    public static DocumentModel getDocumentModel(String topicId, String documentType) {
        DocumentModel documentModel = new DocumentModel();
        String sql = "SELECT * FROM Documents WHERE Documents.topic =" + toSQLString(topicId) + " and Documents.type =" + toSQLString(documentType);
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                documentModel.id = rs.getString("id");
                documentModel.topic = rs.getString("topic");
                documentModel.path = rs.getString("path");
                documentModel.type = rs.getString("type");
                documentModel.createdAt = rs.getString("createdAt");
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documentModel;
    }

    public static String[] getSummaryDocumentIdArray() {
        String[] ids = null;
        ArrayList<String> arrayList = new ArrayList<>();
        String sql = "Select * from Documents WHERE type='0'";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                arrayList.add(rs.getString("id"));
            }
            ids = arrayList.toArray(new String[arrayList.size()]);
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }
    
    public static String[] getFinalDocumentIdArray() {
        String[] ids = null;
        ArrayList<String> arrayList = new ArrayList<>();
        String sql = "Select * from Documents WHERE type='0'";
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                arrayList.add(rs.getString("id"));
            }
            ids = arrayList.toArray(new String[arrayList.size()]);
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    public static DocumentModel getDocumentModelById(String documentId) {
        DocumentModel documentModel = new DocumentModel();
        String sql = "SELECT * FROM Documents WHERE Documents.id =" + toSQLString(documentId);
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                documentModel.id = rs.getString("id");
                documentModel.topic = rs.getString("topic");
                documentModel.path = rs.getString("path");
                documentModel.type = rs.getString("type");
                documentModel.createdAt = rs.getString("createdAt");
            }
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documentModel;
    }

    public static boolean checkDocumentExist(String documentId) {
        String sql = "SELECT * FROM Documents WHERE Documents.id =" + toSQLString(documentId);
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean checkStringExistInArray(String word, String[] arrStrings) {
        boolean found = false;
        for (String str : arrStrings) {
            if (str.equals(word)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static boolean checkExistSimilarPercent(String documentId) {
        String sql = "Select * from Similar WHERE document1=" + toSQLString(documentId) + "OR document2=" + toSQLString(documentId);
        try {
            Statement stm = cnn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
