package frs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FrsDosenFragmentBinding;
import com.google.android.material.navigation.NavigationBarView;

public class FRSDosenFragment extends Fragment {

    FrsDosenFragmentBinding binding;

    private FRSDosenFragment(){}

    public static FRSDosenFragment newInstance(String title){
        FRSDosenFragment fragment = new FRSDosenFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FrsDosenFragmentBinding.inflate(inflater);

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

        return binding.getRoot();
    }
}
