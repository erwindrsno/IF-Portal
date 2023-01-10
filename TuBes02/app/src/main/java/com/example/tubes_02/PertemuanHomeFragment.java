package com.example.tubes_02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tubes_02.databinding.FragmentPertemuanHomeBinding;

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
