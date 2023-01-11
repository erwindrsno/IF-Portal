package frs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FrsPerSemesterBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class FRSperSemesterFragment extends Fragment implements UIMatkulSemester{

    private FrsPerSemesterBinding binding;
    private FRSPresenter presenter;
    private AdapterMatkulSemester adapterMatkulSemester;
    private FRSDialogAddMatkul dialogAddMatkul;
    private int positionSemester;
    private int academic_year;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private FRSperSemesterFragment(){}

    public static FRSperSemesterFragment newInstance(String title, FRSPresenter presenter, FRSDialogAddMatkul dialogAddMatkul){
        FRSperSemesterFragment fragment = new FRSperSemesterFragment();
        fragment.presenter = presenter;
        fragment.dialogAddMatkul = dialogAddMatkul;
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FrsPerSemesterBinding.inflate(inflater);

        Log.d("fragment frs persemester", "masuk");

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

        this.getParentFragmentManager().setFragmentResultListener("semesterPosition", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                positionSemester = result.getInt("position");
                academic_year = result.getInt("academic_year");
                binding.tvSemester.setText(academic_year+"");

                if(academic_year!=presenter.getActiveYear()){
                    binding.btnMatkulEnroll.setClickable(false);
                    Context context = presenter.context;
                    CharSequence text = "Bukan Semester Aktif!";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                Log.d("fragment per semester academic", academic_year+"");
                createAdapter(academic_year);
                presenter.setMatkulSemestertoAdapter(academic_year);
                fm = getParentFragmentManager();
                ft = fm.beginTransaction();
            }
        });

        binding.btnMatkulEnroll.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    private void createAdapter(int academic_year) {
        this.adapterMatkulSemester = new AdapterMatkulSemester(presenter,this, academic_year);
        binding.listMatkul.setAdapter(adapterMatkulSemester);
    }

    private void onClick(View view) {
        Log.d("fragment per semester academic on click", academic_year+"");
        if(view.getId()==binding.btnMatkulEnroll.getId()){
            Bundle result = new Bundle();
            result.putInt("academic_year", academic_year);
            this.getParentFragmentManager().setFragmentResult("pilihMatkulEnrolYear", result);
            dialogAddMatkul.show(fm, "FRSDialogAddMatkul");
        }
    }

    @Override
    public void updateListMatkul(ArrayList<MataKuliah> matkuls) {
        this.adapterMatkulSemester.update(matkuls);
    }
}
