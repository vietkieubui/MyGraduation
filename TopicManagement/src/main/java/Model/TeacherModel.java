/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author BVKieu
 */
public class TeacherModel {

    public String id;
    public String name;
    public String academicRank;
    public String majors;
    public String phonenumber;
    public String email;

    public TeacherModel(String name, String academicRank, String majors, String phonenumber, String email) {
        this.name = name;
        this.academicRank = academicRank;
        this.majors = majors;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public TeacherModel() {

    }

}
