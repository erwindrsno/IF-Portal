package pengumuman;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import Users.User;
import pengumuman.model.ListPengumuman;

public class PengumumanPresenter {
    protected ListPengumumanUI ui;
    protected Context context;
    protected User user;
    protected ArrayList<ListPengumuman> daftarPengumuman;

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

    public void executeAPI(){
        GetPengumuman task = new GetPengumuman(this.context,this);
        task.execute();
    }

    public void getListFromAPI(ArrayList<ListPengumuman> daftarPengumuman){
        this.daftarPengumuman = daftarPengumuman;
        Log.d("presenterSizeDaftarPengumuman",daftarPengumuman.size()+"");
        sendList();
    }

    public void sendList(){
        Log.d("updateToAdapter",true+"");
        this.ui.updateList(this.daftarPengumuman);
    }
}