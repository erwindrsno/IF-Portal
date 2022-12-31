package com.example.tubes_02;

import Users.Admin;
import Users.Dosen;
import Users.Mahasiswa;
import Users.User;

public class LoginPresenter {
    protected LoginUI ui;
    protected User user;

    public LoginPresenter(LoginUI ui){
        this.ui = ui;
    }

    public void newUser(String email, String password, String role){
        if(role.equalsIgnoreCase("admin")){
            this.user = new Admin(email,password,role);
        }
        else if(role.equalsIgnoreCase("dosen")){
            this.user = new Dosen(email,password,role);
        }
        else if(role.equalsIgnoreCase("mahasiswa")){
            this.user = new Mahasiswa(email,password,role);
        }
    }

    public User getUser(){
        return this.user;
    }

    public void setUserToken(String token){
        this.user.setToken(token);
    }

    public void authSuccess(){
        this.ui.updateView(true);
    }

    public void authFailed(){
        this.ui.updateView(false);
    }
}
