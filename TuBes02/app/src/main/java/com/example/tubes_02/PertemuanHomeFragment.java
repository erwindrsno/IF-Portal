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

        // Listener
        this.binding.fabTambah.setOnClickListener(this::onTambahClick);


        this.binding.lvPertemuanDaftar.setOnItemClickListener(this::onItemClick);
        this.binding.lvPertemuanDaftar.setAdapter(this.adapter);

        return this.binding.getRoot();
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String id = this.presenter.getPertemuanId(i);
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("page", "detail");
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
