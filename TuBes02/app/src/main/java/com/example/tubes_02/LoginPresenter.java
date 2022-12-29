package com.example.tubes_02;

public class LoginPresenter {
    protected LoginUI ui;
    protected User user;

    public LoginPresenter(LoginUI ui){
        this.ui = ui;
    }

    public void newUser(String email, String password, String role){
        this.user = new User(email,password,role);
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
