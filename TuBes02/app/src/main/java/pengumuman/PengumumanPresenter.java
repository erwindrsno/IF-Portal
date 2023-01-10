package pengumuman;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import Users.User;
import pengumuman.model.Pengumuman;

public class PengumumanPresenter {
    protected ListPengumumanUI ui;
    protected Context context;
    protected User user;
    protected ArrayList<Pengumuman> daftarPengumuman;

    public PengumumanPresenter(ListPengumumanUI ui, Context context){
        this.ui = ui;
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void executeGetPengumumanAPI(){
        GetPengumuman task = new GetPengumuman(this.context,this);
        task.execute();
    }

    public void executeGetIsiPengumumanAPI(Pengumuman pengumuman){
        GetIsiPengumuman task = new GetIsiPengumuman(this.context, this, pengumuman);
        task.execute();
    }

    public void getListFromAPI(ArrayList<Pengumuman> daftarPengumuman){
        this.daftarPengumuman = daftarPengumuman;
        Log.d("presenterSizeDaftarPengumuman",daftarPengumuman.size()+"");
        sendList();
    }

    public void sendList(){
        Log.d("updateToAdapter",true+"");
        this.ui.updateList(this.daftarPengumuman);
    }
}