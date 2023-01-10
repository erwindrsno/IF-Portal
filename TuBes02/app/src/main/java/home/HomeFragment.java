package home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private FragmentHomeBinding binding;

    public HomeFragment(){
        //empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentHomeBinding.inflate(inflater);
        this.binding.btnCoba.setOnClickListener(this);

        Menu menu = this.binding.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        this.binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle result = new Bundle();
                if(item.getItemId() == R.id.nav_btn_home){
                    result.putString("activity","home");
                }
                if(item.getItemId() == R.id.nav_btn_frs_prs){
                    result.putString("activity","frs/prs");
                }
                else if(item.getItemId() == R.id.nav_btn_pengumuman){
                    result.putString("activity","pengumuman");
                }
                else if(item.getItemId() == R.id.nav_btn_pertemuan){
                    result.putString("activity","pertemuan");
                }
                getParentFragmentManager().setFragmentResult("changeActivity",result);
                return true;
            }
        });
        return this.binding.getRoot();
    }

    public static HomeFragment newInstance(String title){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnCoba.getId()){
            Bundle result = new Bundle();
            result.putString("page","exit");
            this.getParentFragmentManager().setFragmentResult("changePage",result);
        }
    }
}
