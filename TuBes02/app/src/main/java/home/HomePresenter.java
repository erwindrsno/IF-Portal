package home;

import android.util.Log;

import Users.User;

public class HomePresenter {
    User user;
    boolean isAdmin;
    HomeUI ui;

    public HomePresenter(User user, HomeUI ui){
        this.user = user;
        this.isAdmin = false;
        this.ui = ui;
    }

    public void checkUserRole(){
        if(this.user.getRole().equals("admin")){
            this.isAdmin = true;
        }
    }

    public void toHideView(){
        if(!this.isAdmin){
//            this.ui.hideView();
            this.ui.hideMenu();
            Log.d("hideMenu",true+"");
        }
    }
}
