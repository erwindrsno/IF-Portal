package pengumuman;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.ListPengumumanBinding;

import java.util.ArrayList;
import java.util.List;

public class PengumumanAdapter extends BaseAdapter {
    private List<Pengumuman> pengumumanList;
    private ListPengumumanBinding binding;
    private ViewHolder viewHolder;
    private Activity activity;

    public PengumumanAdapter(Activity activity){
        this.activity = activity;
        this.pengumumanList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return pengumumanList.size();
    }

    @Override
    public Object getItem(int i) {
        return pengumumanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            this.binding = ListPengumumanBinding.inflate(activity.getLayoutInflater());
            view = binding.getRoot();
            viewHolder = new ViewHolder(activity, this, this.binding, i);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.updateView((Pengumuman) this.getItem(i));
        return view;
    }
}
class ViewHolder{
    public ViewHolder(Activity activity, PengumumanAdapter adapter, ListPengumumanBinding binding, int i){

    }
    public void updateView(Pengumuman pengumuman){

    }
}
