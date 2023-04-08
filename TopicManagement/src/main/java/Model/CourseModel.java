/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author BVKieu
 */
public class CourseModel {

    public String id;
    public String name;
    public String description;
    public String studyTime;
    public String majors;

    public CourseModel() {
    }

    public CourseModel(String name, String description, String studyTime, String majors) {
        this.name = name;
        this.description = description;
        this.studyTime = studyTime;
        this.majors = majors;
    }

}
