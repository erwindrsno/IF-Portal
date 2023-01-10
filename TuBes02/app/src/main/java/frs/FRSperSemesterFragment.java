package frs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.tubes_02.databinding.FrsPerSemesterBinding;

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

        this.getParentFragmentManager().setFragmentResultListener("semesterPosition", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                positionSemester = result.getInt("position");
                academic_year = result.getInt("academic_year");
                binding.tvSemester.setText(academic_year+"");
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
