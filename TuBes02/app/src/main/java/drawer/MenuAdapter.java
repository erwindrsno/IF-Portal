package drawer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tubes_02.databinding.ItemListMenuBinding;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    private ArrayList<String> listMenu;
    private ItemListMenuBinding binding;
    private DrawerFragment drawerFragment;
    private LayoutInflater inflater;

    public MenuAdapter(DrawerFragment drawerFragment, LayoutInflater inflater){
        this.listMenu = new ArrayList<>();
        this.drawerFragment = drawerFragment;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.listMenu.size();
    }

    @Override
    public String getItem(int i) {
        return this.listMenu.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            binding = ItemListMenuBinding.inflate(this.inflater);
            view = binding.getRoot();
            viewHolder = new ViewHolder(binding);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.updateView(this.getItem(i));
        return view;
    }

    public void addList(String str){
        this.listMenu.add(str);
        this.notifyDataSetChanged();
    }

    private class ViewHolder implements View.OnClickListener{
        protected ItemListMenuBinding binding;

        public ViewHolder(ItemListMenuBinding binding){
            this.binding= binding;
        }

        public void updateView(String menu){
            this.binding.tvMenu.setText(menu);
            this.binding.tvMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            Bundle result = new Bundle();
            Log.d("debugMasukOnClick","masukOnClick");
            if(view.getId() == this.binding.tvMenu.getId()){
                if(this.binding.tvMenu.getText().equals("My Profile")){
                    Log.d("masuk menu my profile","true");
//                    result.putString("page","homeFragment");
                }
                else if(binding.tvMenu.getText().equals("Settings")){
                    Log.d("masuk menu settings","true");
//                    result.putString("page","pertemuanFragment");
                }
                else if(binding.tvMenu.getText().equals("Sign Out")){
                    Log.d("masuk menu sign out","true");
                    result.putString("page","exit");
                }
                drawerFragment.getParentFragmentManager().setFragmentResult("changePage",result);
            }
        }
    }
}
