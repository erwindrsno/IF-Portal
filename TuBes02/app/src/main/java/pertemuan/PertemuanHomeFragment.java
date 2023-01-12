package pertemuan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentPertemuanHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class PertemuanHomeFragment extends Fragment {
    private FragmentPertemuanHomeBinding binding;
    private PertemuanPresenter presenter;
    private FragmentManager fm;
    private PertemuanListAdapter adapter;
    private boolean isInInvitationList;

    public PertemuanHomeFragment() {
    }

    public static PertemuanHomeFragment newInstance(PertemuanPresenter presenter) {
        PertemuanHomeFragment fragment = new PertemuanHomeFragment();
        fragment.presenter = presenter;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inisialisasi atribut
        this.binding = FragmentPertemuanHomeBinding.inflate(inflater, container, false);
        this.fm = getParentFragmentManager();
        this.adapter = new PertemuanListAdapter(presenter);
        this.isInInvitationList = false;

        // Listener
        this.binding.fabTambah.setOnClickListener(this::onTambahClick);
        this.binding.tvMenuAttending.setOnClickListener(this::onAttendingClick);
        this.binding.tvMenuInvitation.setOnClickListener(this::onInvitationClick);

        this.binding.lvPertemuanDaftar.setOnItemClickListener(this::onItemClick);
        this.binding.lvPertemuanDaftar.setAdapter(this.adapter);

        this.presenter.getThisWeekAppointmentsFromServer();

        Menu menu = this.binding.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
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

    private void onInvitationClick(View view) {
        this.isInInvitationList = true;
        this.presenter.getInvitationsFromServer();
    }

    private void onAttendingClick(View view) {
        this.isInInvitationList = false;
        this.presenter.getThisWeekAppointmentsFromServer();
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String id = this.presenter.getPertemuanId(i);
        Bundle args = new Bundle();
        args.putString("id", id);
        if (isInInvitationList)
            args.putString("page", "detailUndangan");
        else args.putString("page", "detailPertemuan");
        fm.setFragmentResult("showDetail", args);
    }

    private void onTambahClick(View v) {
        Bundle result = new Bundle();
        result.putString("page", "tambah");
        this.fm.setFragmentResult("changePage", result);
    }

    public void updateListData(ArrayList<Pertemuan> pertemuanList) {
        this.adapter.updateData(pertemuanList);
    }
}
