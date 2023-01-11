package frs;

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
import com.example.tubes_02.databinding.FrsMainFragmentBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class FRSMainFragment extends Fragment implements UIFRSemester{
    private FrsMainFragmentBinding binding;
    private FRSPresenter presenter;
    private AdapterSemester adapterSemester;

    private FRSMainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FrsMainFragmentBinding.inflate(inflater);

        this.adapterSemester = new AdapterSemester(presenter, this);
        this.binding.listSemester.setAdapter(adapterSemester);

        //load data semester disini
        presenter.searchActiveYear();
        presenter.getSemestertoAdapter();
        Log.d("presenteractive", presenter.active_year+"");

        Menu menu = this.binding.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
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

    public static FRSMainFragment newInstance(String title, FRSPresenter presenter){
        FRSMainFragment fragment = new FRSMainFragment();
        fragment.presenter = presenter;
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateListSemester(ArrayList<Integer> semester) {
        this.adapterSemester.update(semester);
    }
}
