package pengumuman;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentBuatPengumumanBinding;
import com.example.tubes_02.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import pengumuman.model.Pengumuman;
import pengumuman.model.Tag;

public class BuatPengumumanFragment extends Fragment implements View.OnClickListener,  AdapterView.OnItemSelectedListener{
    private FragmentBuatPengumumanBinding binding;
    private PengumumanPresenter presenter;
    private TagAdapter adapter;
    private Tag tagTitle;

    public BuatPengumumanFragment(){
        //empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentBuatPengumumanBinding.inflate(inflater);

        this.adapter = new TagAdapter(this.presenter, inflater,this);

        this.binding.dropdownTags.setAdapter(this.adapter);

        this.binding.dropdownTags.setOnItemSelectedListener(this);

        this.binding.btnAddAnnouncement.setOnClickListener(this);

        this.presenter.getTagsAPI();

        return this.binding.getRoot();
    }

    public static BuatPengumumanFragment newInstance(PengumumanPresenter presenter){
        BuatPengumumanFragment fragment = new BuatPengumumanFragment();
        fragment.presenter = presenter;
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if(this.binding.btnAddAnnouncement.getId() == view.getId()){
            String title = this.binding.etInputJudul.getText().toString();
            String content = this.binding.etInputDeskripsi.getText().toString();
            Log.d("judul",title);
            Log.d("isi",content);
            Log.d("tag",this.tagTitle.getTag());

            this.presenter.addPengumuman(title,content,this.tagTitle.getId());
        }
    }

    public void updateTagToAdapter(ArrayList<Tag> tagList) {
        for (int i = 0; i < tagList.size(); i++) {
            this.adapter.addList(tagList.get(i));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == this.binding.dropdownTags.getId()){
            this.tagTitle = (Tag) adapterView.getItemAtPosition(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
