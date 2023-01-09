package pengumuman;

import android.content.Context;

import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import Users.User;

public class PengumumanPresenter {
    protected PengumumanUI ui;
    protected Context context;
    protected User user;
    protected ArrayList<Pengumuman> arrListPengumuman;

    public PengumumanPresenter(PengumumanUI ui, Context context){
        this.ui = ui;
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void executeAPI(){
        GetPengumuman task = new GetPengumuman(this.context,this);
        task.execute(this.user);
    }

    public void setArrayListPengumuman(ArrayList<Pengumuman> arrListPengumuman){
        this.arrListPengumuman = arrListPengumuman;
    }
}