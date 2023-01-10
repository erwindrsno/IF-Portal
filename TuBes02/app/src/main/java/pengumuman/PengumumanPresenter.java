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
        GetPengumuman task1 = new GetPengumuman(this.context,this);
        task1.execute();
    }

//    public void executeGetIsiPengumumanAPI(Pengumuman pengumuman){
//        GetIsiPengumuman task2 = new GetIsiPengumuman(this.context, this, pengumuman);
//        task2.execute();
//    }

    public void getListFromAPI(ArrayList<Pengumuman> daftarPengumuman){
        this.daftarPengumuman = daftarPengumuman;
        executeGetContent();
    }
    public void executeGetContent(){
        //add content ke dalam pengumuman
        for (int i = 0; i < this.daftarPengumuman.size(); i++) {
            GetIsiPengumuman task2 = new GetIsiPengumuman(this.context, this, this.daftarPengumuman.get(i));
            task2.execute();
            try{
                Log.d("pengumuman isinya", this.daftarPengumuman.get(i).getContent());
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        sendList();
    }

    public void sendList(){
        this.ui.updateList(this.daftarPengumuman);
    }

    public void sendPengumuman(Pengumuman pengumuman){
        this.ui.updateDialogView(pengumuman);
    }
}