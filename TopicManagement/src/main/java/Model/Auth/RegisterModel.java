/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Auth;

/**
 *
 * @author BVKieu
 */
public class RegisterModel extends LoginModel {

    public String confirmPassword;
    public String name;
    public String phonenumber;

    public RegisterModel(String name, String phonenumber, String username, String password, String confirmPassword) {
        super(username, password);
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.phonenumber = phonenumber;
    }    
}
