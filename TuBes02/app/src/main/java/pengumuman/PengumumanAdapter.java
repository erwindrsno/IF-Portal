package pengumuman;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.ListPengumumanBinding;

import java.util.ArrayList;
import java.util.List;

import Users.User;

public class PengumumanAdapter extends BaseAdapter {
    private ArrayList<Pengumuman> pengumumanList;
    private ListPengumumanBinding binding;
    private ViewHolder viewHolder;
    private User user;
    private PengumumanPresenter presenter;
    private LayoutInflater inflater;
    private PengumumanFragment fragment;

    public PengumumanAdapter(PengumumanPresenter presenter, LayoutInflater inflater, PengumumanFragment fragment){
        this.presenter = presenter;
        this.pengumumanList = new ArrayList<>();
        this.inflater = inflater;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return this.pengumumanList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.pengumumanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            this.binding = ListPengumumanBinding.inflate(this.inflater);
            view = this.binding.getRoot();
            viewHolder = new ViewHolder(view, this.binding, i, this.fragment);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.updateView(this.pengumumanList.get(i));

        return view;
    }

    private class ViewHolder{
        public ViewHolder(View view, ListPengumumanBinding binding, int i, PengumumanFragment fragment){

        }
        public void updateView(Pengumuman pengumuman){

        }
    }
}
