package pengumuman;

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
import com.example.tubes_02.databinding.FragmentBuatPengumumanBinding;
import com.example.tubes_02.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class BuatPengumumanFragment extends Fragment implements View.OnClickListener{
    private FragmentBuatPengumumanBinding binding;
    private PengumumanPresenter presenter;

    public BuatPengumumanFragment(){
        //empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentBuatPengumumanBinding.inflate(inflater);

        this.binding.btnAddAnnouncement.setOnClickListener(this);

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
            String tags = this.binding.etInputJudul.getText().toString();
        }
    }

    public void hideAdminMenu(){

    }
}
