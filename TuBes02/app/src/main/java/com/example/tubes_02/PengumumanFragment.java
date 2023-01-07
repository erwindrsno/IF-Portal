package com.example.tubes_02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.databinding.FragmentPengumumanBinding;

public class PengumumanFragment extends Fragment {
    private FragmentPengumumanBinding binding;

    public PengumumanFragment(){

    }

    public static PengumumanFragment newInstance() {
        PengumumanFragment fragment = new PengumumanFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPengumumanBinding.inflate(inflater);
        View view = binding.getRoot();
        this.binding.btnBuatPengumuman.setOnClickListener(this::onClick);
        return view;
    }

    private void onClick(View view) {
        Bundle result = new Bundle();
        result.putString("page","buat_pengumuman");
        this.getParentFragmentManager().setFragmentResult("changePage", result);
    }


}
