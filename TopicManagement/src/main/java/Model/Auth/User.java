/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Auth;

/**
 *
 * @author BVKieu
 */
public final class User {

    public static String id;
    public static String username;
    public static String name;
    public static String phonenumber;    
   
    public User() {
    }
    
    public static void setUser(String id, String username, String name, String phonenumber){
        User.id = id;
        User.username = username;
        User.name = name;
        User.phonenumber = phonenumber;
    }
}
