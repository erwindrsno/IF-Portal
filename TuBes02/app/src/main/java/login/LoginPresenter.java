package login;

import android.content.Context;
import android.util.Log;

import Users.Admin;
import Users.Dosen;
import Users.Mahasiswa;
import Users.User;

public class LoginPresenter {
    protected LoginUI ui;
    protected User user;
    protected Context context;

    public LoginPresenter(LoginUI ui, Context context){
        this.ui = ui;
        this.context = context;
    }

    public void newUser(String email, String password, String role, boolean valid){
        if(valid){
            if(role.equalsIgnoreCase("admin")){
                this.user = new Admin(email,password,role);
            }
            else if(role.equalsIgnoreCase("dosen")){
                this.user = new Dosen(email,password,role);
            }
            else if(role.equalsIgnoreCase("mahasiswa")){
                this.user = new Mahasiswa(email,password,role);
            }
            PostAuthenticate authTask = new PostAuthenticate(this.context, this);
            authTask.execute(this.getUser());
        }
    }

    public void validateUser(String email, String password, String role){
        //cek apakah email dan password terdapat kosong
        //jika tidak ada, maka object user akan dibuat
        if(email != null && password != null && role != null){
            this.newUser(email,password,role,true);
        }
        else{
            this.ui.updateViewForInputValidation();
        }
    }

    public User getUser(){
        return this.user;
    }

    public void setUserToken(String token){
        this.user.setToken(token);
    }

    public void authSuccess(){
        this.ui.updateViewFromAPI(true,"berhasil");
    }

    public void authFailed(){
        this.ui.updateViewFromAPI(false, "wrongCredentials");
    }

    public void timeOutError(){
        this.ui.updateViewFromAPI(false, "timeOutError");
    }
}
