/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author BVKieu
 */
public class ClassModel {
    public String id;
    public String name;
    public String description;
    public String course;

    public ClassModel(String name, String description, String course) {
        this.name = name;
        this.description = description;
        this.course = course;
    }
}
