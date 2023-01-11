package drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentDrawerBinding;

public class DrawerFragment extends Fragment{
    private FragmentDrawerBinding binding;
    private MenuAdapter adapter;

    public DrawerFragment(){
        //Konstruktor harus kosong
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.binding = FragmentDrawerBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        this.adapter = new MenuAdapter(this,inflater);
        binding.lvMenu.setAdapter(adapter);
        this.adapter.addList("Home");
        this.adapter.addList("Settings");
        this.adapter.addList("Sign Out");
        return view;
    }
}