package pengumuman;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentPengumumanBinding;

import java.util.ArrayList;

import pengumuman.model.Pengumuman;

public class PengumumanFragment extends Fragment{
    private FragmentPengumumanBinding binding;
    private ListPengumumanAdapter adapter;
    private PengumumanPresenter presenter;
    private DialogFragmentKontenPengumuman dialogFragment;

    public PengumumanFragment(){
    }

    public static PengumumanFragment newInstance(PengumumanPresenter presenter, DialogFragmentKontenPengumuman dialogFragment) {
        PengumumanFragment fragment = new PengumumanFragment();
        fragment.presenter = presenter;
        fragment.dialogFragment = dialogFragment;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentPengumumanBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
//        this.binding.btnBuatPengumuman.setOnClickListener(this::onClick);\

        //adapter dropdown pilih search by title/ filter by tag
        String [] filter = getResources().getStringArray(R.array.sort);
        ArrayAdapter dropdownAdapter = new ArrayAdapter(this.presenter.getContext(), android.R.layout.simple_spinner_item, filter);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.binding.spinner.setAdapter(dropdownAdapter);
        //adapter dropdown

        //adapter
        this.adapter = new ListPengumumanAdapter(this.presenter, inflater,this);
        this.binding.listPengumuman.setAdapter(this.adapter);
        //adapter

        FragmentManager fm = this.getParentFragmentManager();
        this.getParentFragmentManager().setFragmentResultListener("openDialogKonten", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result){
                String judul = result.getString("title");
                String isi = result.getString("content");
                Log.d("fragmentresultlistener judul",judul);
                Log.d("fragmentresultlistener isi",isi);
                dialogFragment.setTitle(judul);
                dialogFragment.setContent(isi);
                dialogFragment.show(fm,"message");
            }
        });

        return view;
    }

//    private void onClick(View view) {
//        Bundle result = new Bundle();
//        result.putString("page","buat_pengumuman");
//        this.getParentFragmentManager().setFragmentResult("changePage", result);
//    }

    public void updateListToAdapter(ArrayList<Pengumuman> daftarPengumuman) {
        for (int i = 0; i < daftarPengumuman.size(); i++) {
            this.adapter.addList(daftarPengumuman.get(i));
        }
    }

    public void openDialog(Pengumuman pengumuman){
        Bundle result = new Bundle();
        String judul = pengumuman.getTitle();
        String isi = pengumuman.getContent();
        result.putString("title",judul);
        result.putString("content", isi);
        this.getParentFragmentManager().setFragmentResult("openDialogKonten",result);
    }
}
