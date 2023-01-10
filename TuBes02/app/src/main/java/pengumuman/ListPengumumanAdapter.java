package pengumuman;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.ItemListDaftarPengumumanBinding;
import com.example.tubes_02.databinding.ItemListMenuBinding;
import com.example.tubes_02.databinding.ItemListDaftarPengumumanBinding;

import java.util.ArrayList;
import java.util.List;

import Users.User;
import pengumuman.model.ListPengumuman;
import pengumuman.model.Tag;

public class ListPengumumanAdapter extends BaseAdapter {
    private ArrayList<ListPengumuman> daftarPengumuman;
    private ItemListDaftarPengumumanBinding binding;
    private ViewHolder viewHolder;
    private User user;
    private PengumumanPresenter presenter;
    private LayoutInflater inflater;
    private PengumumanFragment fragment;

    public ListPengumumanAdapter(PengumumanPresenter presenter, LayoutInflater inflater, PengumumanFragment fragment){
        this.presenter = presenter;
        this.daftarPengumuman = new ArrayList<>();
        this.inflater = inflater;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return this.daftarPengumuman.size();
    }

    @Override
    public Object getItem(int i) {
        return this.daftarPengumuman.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            this.binding = ItemListDaftarPengumumanBinding.inflate(this.inflater);
            view = this.binding.getRoot();
            viewHolder = new ViewHolder(this.binding);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.updateView(this.daftarPengumuman.get(i));

        return view;
    }

    public void addList(ListPengumuman listPengumuman){
        this.daftarPengumuman.add(listPengumuman);
        this.notifyDataSetChanged();
    }

//    public void update(ArrayList<ListPengumuman> daftarPengumuman){
//        this.daftarPengumuman = daftarPengumuman;
//    }

    private class ViewHolder implements View.OnClickListener{
        protected ItemListDaftarPengumumanBinding binding;

        public ViewHolder(ItemListDaftarPengumumanBinding binding){
            this.binding= binding;

            this.binding.layoutDaftarPengumuman.setOnClickListener(this);
        }

        public void updateView(ListPengumuman daftarPengumuman){
            this.binding.tvTitleDaftarPengumuman.setText(daftarPengumuman.getTitle());
            ArrayList<Tag> listTags = daftarPengumuman.getTags();
            String tags = "";
            for (int i = 0; i < listTags.size(); i++) {
                if(i == listTags.size() - 1){
                    tags += listTags.get(i).getTag();
                }
                else{
                    tags += listTags.get(i).getTag()+", ";
                }
            }
            this.binding.tvTags.setText(tags);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == this.binding.layoutDaftarPengumuman.getId()){
                Log.d("masukdialogdaftarpengumuman",true+"");
            }
        }
    }
}
