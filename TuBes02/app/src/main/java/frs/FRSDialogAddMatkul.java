package frs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.tubes_02.databinding.FrsDialogAddMatkulBinding;

import java.util.ArrayList;

public class FRSDialogAddMatkul extends DialogFragment implements UIMatkulEnrol {
    private FRSPresenter presenter;
    private FrsDialogAddMatkulBinding binding;
    private AdapterMatkulEnrol adapterMatkulEnrol;
    private String idTemp;
    private int academic_year_temp;

    public FRSDialogAddMatkul(){}

    public static  FRSDialogAddMatkul newInstance(String title, FRSPresenter presenter){
        FRSDialogAddMatkul fragment = new FRSDialogAddMatkul();
        fragment.presenter = presenter;
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FrsDialogAddMatkulBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        this.adapterMatkulEnrol = new AdapterMatkulEnrol(presenter, this);
        this.binding.lvListMatkulDialog.setAdapter(adapterMatkulEnrol);

        presenter.setMatkulEnroltoAdapterAwal();

        this.getParentFragmentManager().setFragmentResultListener("pilihMatkulEnrol", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String name = result.getString("namaMatkulE");
                String id = result.getString("idMatkulE");
                idTemp = id;
                binding.tvSelected.setText(name);
            }
        });

        binding.btnSearch.setOnClickListener(this::onClick);
        binding.tvSelected.setOnClickListener(this::onClick);

        return view;
    }

    private void onClick(View view) {
        if(view.getId()==binding.btnSearch.getId()){
            if(binding.etSearch.getText().toString().trim().equals("")){
                binding.etSearch.setError("Tidak boleh kosong!");
            }
            else if(!binding.etSearch.getText().toString().trim().equals("")){
                String hint = binding.etSearch.getText().toString();
                presenter.setMatkulEnroltoAdapterSearch(hint);
            }
        }
        if(view.getId()==binding.tvSelected.getId()){
            if(!binding.tvSelected.getText().toString().trim().equals("")){
                this.getParentFragmentManager().setFragmentResultListener("pilihMatkulEnrolYear", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        int academic_year = result.getInt("academic_year");
                        academic_year_temp = academic_year;
                        presenter.addEnrolAPI(idTemp, academic_year);
                    }
                });
                this.binding.etSearch.setText("");
                this.dismiss();
            }
        }
    }

    @Override
    public void updateListMatkulE(ArrayList<MataKuliahEnrol> matkulE) {
        this.adapterMatkulEnrol.update(matkulE);
    }
}
