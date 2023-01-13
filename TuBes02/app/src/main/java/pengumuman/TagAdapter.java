package pengumuman;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.ItemListDaftarPengumumanBinding;
import com.example.tubes_02.databinding.ItemListTagsBinding;

import java.util.ArrayList;

import Users.User;
import pengumuman.model.Pengumuman;
import pengumuman.model.Tag;

public class TagAdapter extends BaseAdapter {
    private ArrayList<Tag> tagList;
    private ItemListTagsBinding binding;
    private ViewHolder viewHolder;
    private User user;
    private PengumumanPresenter presenter;
    private LayoutInflater inflater;

    public TagAdapter(PengumumanPresenter presenter, LayoutInflater inflater, BuatPengumumanFragment fragment){
        this.presenter = presenter;
        this.tagList = new ArrayList<>();
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.tagList.size();
    }

    @Override
    public Tag getItem(int i) {
        return this.tagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            this.binding = ItemListTagsBinding.inflate(this.inflater);
            view = this.binding.getRoot();
            viewHolder = new ViewHolder(this.binding, this.presenter, i);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.updateView(this.tagList.get(i));

        return view;
    }

    public void addList(Tag tag){
        this.tagList.add(tag);
        this.notifyDataSetChanged();
    }

    public void clearList(){
        this.tagList.clear();
        this.notifyDataSetChanged();
    }

    private class ViewHolder implements View.OnClickListener{
        protected ItemListTagsBinding binding;
        protected PengumumanPresenter presenter;
        protected int position;

        public ViewHolder(ItemListTagsBinding binding, PengumumanPresenter presenter, int position){
            this.presenter = presenter;
            this.position = position;
            this.binding = binding;
        }

        public void updateView(Tag tag){
            this.binding.tvTag.setText(tag.getTag());
//            this.binding.tvTitleDaftarPengumuman.setText(tagList.getTitle());
//            Log.d("id",tagList.getId());
//            ArrayList<Tag> listTags = tagList.getTags();
//            String tags = "";
//            for (int i = 0; i < listTags.size(); i++) {
//                if(i == listTags.size() - 1){
//                    tags += listTags.get(i).getTag();
//                }
//                else{
//                    tags += listTags.get(i).getTag()+", ";
//                }
//            }
//            this.binding.tvTags.setText(tags);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
