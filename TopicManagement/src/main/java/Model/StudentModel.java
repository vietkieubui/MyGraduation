/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author BVKieu
 */
public class StudentModel {
    public String id;
    public String name;
    public String gender;
    public String birthday;
    public String classId;
    public String phonenumber;
    public String email;

    public StudentModel(String id, String name, String gender, String birthday, String classId, String phonenumber, String email) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.classId = classId;
        this.phonenumber = phonenumber;
        this.email = email;
    }
}
