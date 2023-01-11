package pengumuman;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.ItemListDaftarPengumumanBinding;

import java.util.ArrayList;

import Users.User;
import pengumuman.model.Pengumuman;
import pengumuman.model.Tag;

public class ListPengumumanAdapter extends BaseAdapter {
    private ArrayList<Pengumuman> daftarPengumuman;
    private ItemListDaftarPengumumanBinding binding;
    private ViewHolder viewHolder;
    private User user;
    private PengumumanPresenter presenter;
    private LayoutInflater inflater;
    private PengumumanFragment fragment;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

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
    public Pengumuman getItem(int i) {
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
            viewHolder = new ViewHolder(this.binding, this.presenter, i);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.updateView(this.daftarPengumuman.get(i));

        return view;
    }

    public void addList(Pengumuman pengumuman){
        this.daftarPengumuman.add(pengumuman);
        this.notifyDataSetChanged();
    }

    private class ViewHolder implements View.OnClickListener{
        protected ItemListDaftarPengumumanBinding binding;
        protected PengumumanPresenter presenter;
        protected int position;

        public ViewHolder(ItemListDaftarPengumumanBinding binding, PengumumanPresenter presenter, int position){
            this.binding= binding;
            this.presenter = presenter;
            this.position = position;
            this.binding.layoutDaftarPengumuman.setOnClickListener(this);
        }

        public void updateView(Pengumuman daftarPengumuman){
            this.binding.tvTitleDaftarPengumuman.setText(daftarPengumuman.getTitle());
            Log.d("id",daftarPengumuman.getId());
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
                //sharedpref
//                Context context = getActivity();
//                sharedPref = context.getSharedPreferences(presenter.getUser().getEmail(), Context.MODE_PRIVATE);
//                editor = sharedPref.edit();
//                editor.putString(id,"true");
//                editor.apply();

                //sharedpref
                Log.d("masukdialogdaftarpengumuman",true+"");
//                this.presenter.executeGetIsiPengumumanAPI(getItem(this.position));
                this.presenter.sendPengumuman(getItem(this.position));
            }
        }
    }
}
