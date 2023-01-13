package pengumuman;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentPengumumanBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import pengumuman.model.Pengumuman;

public class PengumumanFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private FragmentPengumumanBinding binding;
    private ListPengumumanAdapter adapter;
    private PengumumanPresenter presenter;
    private DialogFragmentKontenPengumuman dialogFragment;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String preference;
    private boolean useFilter;
    private boolean isAdmin;
    public PengumumanFragment(){
    }

    public static PengumumanFragment newInstance(PengumumanPresenter presenter, DialogFragmentKontenPengumuman dialogFragment) {
        PengumumanFragment fragment = new PengumumanFragment();
        fragment.presenter = presenter;
        fragment.dialogFragment = dialogFragment;
        fragment.useFilter = false;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentPengumumanBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(this.binding.etFilter.getWindowToken(), 0);

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

        this.binding.btnPengumumanBack.setOnClickListener(this);
        this.binding.btnPengumumanForward.setOnClickListener(this);
        this.binding.btnFilter.setOnClickListener(this);
        this.binding.spinner.setOnItemSelectedListener(this);

        this.isAdmin = this.presenter.isAdmin();
        if(this.isAdmin){
            this.binding.fabPengumuman.setOnClickListener(this);
        }
        else{
            this.binding.fabPengumuman.setVisibility(View.GONE);
        }

        FragmentManager fm = this.getParentFragmentManager();
        this.getParentFragmentManager().setFragmentResultListener("openDialogKonten", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result){
                String judul = result.getString("title");
                String isi = result.getString("content");
                String id = result.getString("id");

                dialogFragment.setTitle(judul);
                dialogFragment.setContent(isi);
                dialogFragment.setId(id);
                dialogFragment.show(fm,"message");
            }
        });

        Menu menu = this.binding.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
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
        String id = pengumuman.getId();
        result.putString("id",id);
        result.putString("title",judul);
        result.putString("content", isi);
        this.getParentFragmentManager().setFragmentResult("openDialogKonten",result);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == this.binding.btnPengumumanBack.getId()){
            if(!this.useFilter){
                this.presenter.clearPengumumanList();
                this.adapter.clearList();
                this.presenter.executeGetPengumumanAPI(false);
                Log.d("masuk back",true+"");
            }
            else{
                this.presenter.clearPengumumanList();
                this.adapter.clearList();
                this.presenter.executeGetPengumumanFilterSearch(this.preference, this.binding.etFilter.getText().toString(),false);
            }
        }
        else if(view.getId() == this.binding.btnPengumumanForward.getId()){
            if(!this.useFilter){
                this.presenter.clearPengumumanList();
                this.adapter.clearList();
                this.presenter.executeGetPengumumanAPI(true);
                Log.d("masuk forward",true+"");
            }
            else{
                this.presenter.clearPengumumanList();
                this.adapter.clearList();
                this.presenter.executeGetPengumumanFilterSearch(this.preference, this.binding.etFilter.getText().toString(),true);
            }
        }
        else if(view.getId() == this.binding.btnFilter.getId()){
            this.useFilter = true;
            this.presenter.clearPengumumanList();
            this.adapter.clearList();
            this.presenter.executeGetPengumumanFilterSearch(this.preference, this.binding.etFilter.getText().toString(), false);
        }
        else if(view.getId() == this.binding.fabPengumuman.getId()){
            Bundle result = new Bundle();
            result.putString("page","buat_pengumuman");
            this.getParentFragmentManager().setFragmentResult("changePage",result);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == this.binding.spinner.getId()){
            this.preference = adapterView.getItemAtPosition(i).toString().toLowerCase();
            Log.d("preference",this.preference);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
