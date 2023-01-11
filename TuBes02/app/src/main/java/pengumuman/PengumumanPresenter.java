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
    protected String page;
    protected ArrayList<String> strPage;
    protected boolean hasNext;
    protected int position;

    public PengumumanPresenter(ListPengumumanUI ui, Context context){
        this.ui = ui;
        this.context = context;
        this.hasNext = false;
        this.position = 0;
        this.strPage = new ArrayList<>();
    }

    public Context getContext(){
        return this.context;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void executeGetPengumumanAPI(boolean isForward){
        if(!this.hasNext){
            GetPengumuman task1 = new GetPengumuman(this.context,this);
            task1.execute();
        }
        else{
            if(isForward){
                GetPengumuman task1 = new GetPengumuman(this.context, this, strPage.get(this.position));
                task1.execute();
                this.position++;
            }
            else{
                GetPengumuman task1 = new GetPengumuman(this.context, this, strPage.get(this.position));
                task1.execute();
                this.position--;
            }
        }
    }

    public void executeGetPengumumanFilterSearch(String preference, String text, boolean isForwardFilterSearch){
        if(!this.hasNext){
            GetPengumumanFilterSearch task1 = new GetPengumumanFilterSearch(this.context,this, preference, text);
            task1.execute();
        }
        else{
            if(isForwardFilterSearch){
                GetPengumumanFilterSearch task1 = new GetPengumumanFilterSearch(this.context, this, preference, text, strPage.get(this.position));
                task1.execute();
                this.position++;
            }
            else{
                GetPengumumanFilterSearch task1 = new GetPengumumanFilterSearch(this.context, this, preference, text, strPage.get(this.position));
                task1.execute();
                this.position--;
            }
        }
    }

    public User getUser(){
        return this.user;
    }

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

    public void addNextPage(String nextPage){
        this.strPage.add(nextPage);
        this.hasNext = true;
    }

    public void setNextPageFalse(boolean valid){
        this.hasNext = false;
    }

    public void clearPengumumanList(){
        this.daftarPengumuman.clear();
        Log.d("masuk clear",true+"");
    }

    public void sendList(){
        this.ui.updateList(this.daftarPengumuman);
    }

    public void sendPengumuman(Pengumuman pengumuman){
        this.ui.updateDialogView(pengumuman);
    }
}